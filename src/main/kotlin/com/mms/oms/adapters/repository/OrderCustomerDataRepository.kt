package com.mms.oms.adapters.repository

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant
import java.util.UUID

object OrderCustomerDataRepository : IdTable<UUID>("order_customer_data") {
    override val id = uuid("id").entityId()
    val orderId = uuid("order_id").uniqueIndex().references(OrderRepository.id)
    val customerDataId = uuid("customer_data_id").references(CustomerDataRepository.id)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at").clientDefault { Instant.now() }
}
