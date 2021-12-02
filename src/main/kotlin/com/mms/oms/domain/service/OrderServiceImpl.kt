package com.mms.oms.domain.service

import com.mms.oms.adapters.database.Orders
import com.mms.oms.domain.model.Order
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class OrderServiceImpl : OrderService {

    override suspend fun createOrder(order: Order) = transaction {
        Orders.insert {
            it[Orders.id] = order.id
            it[Orders.status] = order.status.name
            it[Orders.createdAt] = order.createdAt
            it[Orders.updatedAt] = order.updatedAt
        }

        return@transaction
    }
}
