package com.mms.oms.domain.model

import com.mms.oms.config.InstantSerializer
import com.mms.oms.config.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

@Serializable
data class Order(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),
    val status: Status,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant,
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant = Instant.now(),
)

enum class Status {
    CREATED,
    PAID,
    IN_FULFILLMENT,
    CLOSED,
}
