package com.mms.oms.domain.service

import com.mms.oms.adapters.kafka.PaymentProducer
import com.mms.oms.adapters.repository.CartRepository
import com.mms.oms.adapters.repository.PaymentRepository
import com.mms.oms.domain.model.Payment
import com.mms.oms.domain.model.PaymentStatus
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory

class PaymentServiceImpl : PaymentService, KoinComponent {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val paymentProducer: PaymentProducer by inject()

    override suspend fun submitPayment(payment: Payment) {
        paymentProducer.produce(payment)
    }

    override suspend fun processPayment(payment: Payment) = transaction {
        PaymentRepository.insert {
            it[PaymentRepository.id] = payment.orderId
            it[PaymentRepository.transactionId] = payment.transactionId
            it[PaymentRepository.pspReference] = payment.pspReference
            it[PaymentRepository.status] = payment.status
            it[PaymentRepository.currency] = payment.currency
            it[PaymentRepository.amount] = payment.amount.toFloat()
            it[PaymentRepository.createdAt] = payment.createdAt
            it[PaymentRepository.updatedAt] = payment.updatedAt
            it[PaymentRepository.orderId] = payment.orderId
        }

        val cart = CartRepository.select {
            CartRepository.orderId eq payment.orderId
        }.singleOrNull()?.let { cart ->
            maybeScheduleShipment(cart, payment)
        }

        return@transaction
    }

    private fun maybeScheduleShipment(
        cart: ResultRow,
        payment: Payment
    ) {
        val orderTotalPrice = cart[CartRepository.totalPrice]
        val authorizedAmount = getAuthorizedSum(payment)
        if (authorizedAmount >= orderTotalPrice) {
            logger.info("Let's schedule the order")
        }
    }

    private fun getAuthorizedSum(payment: Payment) = PaymentRepository.select {
        (PaymentRepository.orderId eq payment.orderId) and
            (PaymentRepository.status eq PaymentStatus.AUTHORIZED)
    }.map {
        it[PaymentRepository.amount]
    }.sum()
}
