package com.mms.oms.domain.model

import com.mms.oms.config.serialization.InstantSerializer
import com.mms.oms.config.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

@Serializable
data class Shipment(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),
    val carrier: String,
    val trackingNumber: String,
    val status: ShipmentStatus,
    @Serializable(with = UUIDSerializer::class)
    val orderId: UUID,
    @Serializable(with = InstantSerializer::class)
    val estimatedDeliveryAt: Instant,
    @Serializable(with = InstantSerializer::class)
    val actualDeliveryAt: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant,
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant = Instant.now(),
)
