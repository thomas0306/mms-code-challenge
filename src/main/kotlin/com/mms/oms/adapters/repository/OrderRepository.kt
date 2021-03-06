package com.mms.oms.adapters.repository

import com.mms.oms.domain.model.OrderStatus
import com.mms.oms.domain.model.PaymentStatus
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant
import java.util.UUID

object OrderRepository : IdTable<UUID>("order") {
    override val id = uuid("id").entityId()
    val tenant = char("tenant", 2)
    val status = enumerationByName("status", 30, OrderStatus::class)
    val paymentStatus = enumerationByName("payment_status", 30, PaymentStatus::class)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at").clientDefault { Instant.now() }
}
