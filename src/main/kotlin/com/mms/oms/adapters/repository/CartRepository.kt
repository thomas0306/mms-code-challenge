package com.mms.oms.adapters.repository

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant
import java.util.UUID

object CartRepository : IdTable<UUID>("cart") {
    override val id = uuid("id").entityId()
    val currency = char("currency", 3)
    val totalPrice = float("total_price")
    val discountedPrice = float("discounted_price")
    val shippingPrice = float("shipping_price")
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at").clientDefault { Instant.now() }
    val orderId = uuid("order_id").uniqueIndex().references(OrderRepository.id)
}
