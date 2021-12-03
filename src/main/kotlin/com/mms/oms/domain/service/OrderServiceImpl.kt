package com.mms.oms.domain.service

import com.mms.oms.adapters.kafka.OrderProducer
import com.mms.oms.adapters.repository.CartRepository
import com.mms.oms.adapters.repository.ItemRepository
import com.mms.oms.adapters.repository.OrderRepository
import com.mms.oms.domain.model.Order
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Instant

class OrderServiceImpl : OrderService, KoinComponent {

    private val orderProducer: OrderProducer by inject()

    override suspend fun submitOrder(order: Order) {
        orderProducer.produce(order)
    }

    override suspend fun persistOrder(order: Order) = transaction {
        OrderRepository.insert {
            it[id] = order.id
            it[tenant] = order.tenant
            it[status] = order.status
            it[paymentStatus] = order.paymentStatus
            it[createdAt] = order.createdAt
            it[updatedAt] = order.updatedAt
        }

        order.cart?.also { c ->
            CartRepository.insert {
                it[id] = c.id
                it[currency] = c.currency
                it[totalPrice] = c.totalPrice.toFloat()
                it[discountedPrice] = c.discountedPrice.toFloat()
                it[shippingPrice] = c.shippingPrice.toFloat()
                it[orderId] = order.id
                it[createdAt] = c.createdAt
                it[updatedAt] = c.updatedAt
            }
        }

        order.cart?.items?.forEach { i ->
            ItemRepository.insert {
                it[id] = i.id
                it[itemId] = i.itemId
                it[unitPrice] = i.unitPrice.toFloat()
                it[quantity] = i.quantity
                it[createdAt] = i.createdAt
                it[updatedAt] = i.updatedAt
                it[cartId] = order.cart.id
            }
        }

        return@transaction
    }

    override suspend fun updateOrder(
        order: Order
    ) = newSuspendedTransaction {
        OrderRepository.update({ OrderRepository.id eq order.id }) {
            order.status.apply {
                it[status] = this
            }

            order.paymentStatus.apply {
                it[paymentStatus] = this
            }

            it[updatedAt] = Instant.now()
        }

        return@newSuspendedTransaction
    }
}
