package com.mms.oms.domain.service

import com.mms.oms.adapters.database.Orders
import com.mms.oms.domain.model.Order
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class OrderService {

    suspend fun createOrder(order: Order) = transaction {
        Orders.insert {
            it[id] = order.id
            it[status] = order.status.name
            it[createdAt] = order.createdAt
            it[updatedAt] = order.updatedAt
        }
    }
}
