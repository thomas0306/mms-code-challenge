package com.mms.oms.adapters.repository

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant
import java.util.UUID

object ItemRepository : IdTable<UUID>("item") {
    override val id = uuid("id").entityId()
    val itemId = varchar("item_id", 20)
    val unitPrice = float("unit_price")
    val quantity = integer("quantity")
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at").clientDefault { Instant.now() }
    val cartId = uuid("cart_id").uniqueIndex().references(CartRepository.id)
}
