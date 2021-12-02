package com.mms.oms.domain.service

import com.mms.oms.adapters.database.Orders
import com.mms.oms.adapters.kafka.OrderProducer
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
        Orders.insert {
            it[Orders.id] = order.id
            it[Orders.status] = order.status.name
            it[Orders.createdAt] = order.createdAt
            it[Orders.updatedAt] = order.updatedAt
        }

        return@transaction
    }
}
