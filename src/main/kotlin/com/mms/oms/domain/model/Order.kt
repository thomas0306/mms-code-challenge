package com.mms.oms.domain.model

import com.mms.oms.config.serialization.InstantSerializer
import com.mms.oms.config.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

@Serializable
data class Order(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant,
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant = Instant.now(),
    val tenant: String,
    val status: OrderStatus,
    val paymentStatus: PaymentStatus,
    val cart: Cart?,
)
