package com.mms.oms.domain.service

import com.mms.oms.adapters.kafka.OrderProducer
import com.mms.oms.adapters.kafka.PaymentProducer
import com.mms.oms.adapters.repository.CartRepository
import com.mms.oms.adapters.repository.OrderRepository
import com.mms.oms.adapters.repository.PaymentRepository
import com.mms.oms.domain.mapper.OrderMapper
import com.mms.oms.domain.model.OrderStatus
import com.mms.oms.domain.model.Payment
import com.mms.oms.domain.model.PaymentStatus
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory
import java.util.UUID

class PaymentServiceImpl : PaymentService, KoinComponent {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val paymentProducer: PaymentProducer by inject()
    private val orderService: OrderService by inject()
    private val orderProducer: OrderProducer by inject()

    override suspend fun submitPayment(payment: Payment) {
        paymentProducer.produce(payment)
    }

    override suspend fun processPayment(payment: Payment) = newSuspendedTransaction {
        PaymentRepository.insert {
            it[id] = payment.id
            it[transactionId] = payment.transactionId
            it[pspReference] = payment.pspReference
            it[status] = payment.status
            it[currency] = payment.currency
            it[amount] = payment.amount.toFloat()
            it[createdAt] = payment.createdAt
            it[updatedAt] = payment.updatedAt
            it[orderId] = payment.orderId
        }

        CartRepository.select {
            CartRepository.orderId eq payment.orderId
        }.singleOrNull()?.let {
            performPostActions(
                payment.orderId,
                it[CartRepository.totalPrice]
            )
        }

        return@newSuspendedTransaction
    }

    private suspend fun performPostActions(orderId: UUID, orderTotalPrice: Float) {
        val authorizedSum = getAuthorizedSum(orderId)
        val isFullAmountAuthorized = authorizedSum >= orderTotalPrice
        if (isFullAmountAuthorized) {
            logger.debug(
                "Order [$orderId] is fully authorized, " +
                    "total price [$orderTotalPrice] " +
                    "and authorized sum [$authorizedSum]"
            )
            markOrderAsPaidAndAuthorized(orderId)
        }
    }

    private suspend fun markOrderAsPaidAndAuthorized(
        orderId: UUID
    ) {
        OrderRepository
            .select { OrderRepository.id eq orderId }
            .single()
            .let { OrderMapper.toDomain(it) }
            .run { copy(status = OrderStatus.PAID, paymentStatus = PaymentStatus.AUTHORIZED) }
            .let { orderProducer.produce(it) }
    }

    private fun getAuthorizedSum(orderId: UUID) = PaymentRepository.select {
        (PaymentRepository.orderId eq orderId) and
            (PaymentRepository.status eq PaymentStatus.AUTHORIZED)
    }.map {
        it[PaymentRepository.amount]
    }.sum()
}
