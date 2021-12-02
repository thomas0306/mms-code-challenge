package com.mms.oms.domain.service

import com.mms.oms.adapters.kafka.OrderProducer
import com.mms.oms.adapters.repository.CartRepository
import com.mms.oms.adapters.repository.ItemRepository
import com.mms.oms.adapters.repository.OrderRepository
import com.mms.oms.domain.model.Order
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class OrderServiceImpl : OrderService, KoinComponent {

    private val orderProducer: OrderProducer by inject()

    override suspend fun createOrder(order: Order) {
        orderProducer.produce(order)
    }

    override suspend fun persistOrder(order: Order) = transaction {
        OrderRepository.insert {
            it[OrderRepository.id] = order.id
            it[OrderRepository.status] = order.status
            it[OrderRepository.createdAt] = order.createdAt
            it[OrderRepository.updatedAt] = order.updatedAt
        }

        order.cart.also { c ->
            CartRepository.insert {
                it[CartRepository.id] = c.id
                it[CartRepository.currency] = c.currency
                it[CartRepository.totalPrice] = c.totalPrice.toFloat()
                it[CartRepository.discountedPrice] = c.discountedPrice.toFloat()
                it[CartRepository.shippingPrice] = c.shippingPrice.toFloat()
                it[CartRepository.orderId] = order.id
                it[CartRepository.createdAt] = c.createdAt
                it[CartRepository.updatedAt] = c.updatedAt
            }
        }

        order.cart.items.forEach { i ->
            ItemRepository.insert {
                it[ItemRepository.id] = i.id
                it[ItemRepository.itemId] = i.itemId
                it[ItemRepository.unitPrice] = i.unitPrice.toFloat()
                it[ItemRepository.quantity] = i.quantity
                it[ItemRepository.createdAt] = i.createdAt
                it[ItemRepository.updatedAt] = i.updatedAt
                it[ItemRepository.cartId] = order.cart.id
            }
        }

        return@transaction
    }
}
