package com.mms.oms.adapters.repository

import com.mms.oms.domain.model.ShipmentStatus
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant
import java.util.UUID

object ShipmentRepository : IdTable<UUID>("shipment") {
    override val id = uuid("id").entityId()
    val carrier = varchar("carrier", 30)
    val trackingNumber = varchar("tracking_number", 30)
    val status = enumerationByName("status", 30, ShipmentStatus::class)
    val orderId = uuid("order_id")
    val estimatedDeliveryAt = timestamp("estimated_delivery_at")
    val actualDeliveryAt = timestamp("actual_delivery_at")
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at").clientDefault { Instant.now() }
}
