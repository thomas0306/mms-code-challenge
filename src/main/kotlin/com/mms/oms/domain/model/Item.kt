package com.mms.oms.domain.model

import com.mms.oms.config.serialization.BigDecimalSerializer
import com.mms.oms.config.serialization.InstantSerializer
import com.mms.oms.config.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Serializable
data class Item(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant,
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant = Instant.now(),
    val itemId: String,
    @Serializable(with = BigDecimalSerializer::class)
    val unitPrice: BigDecimal,
    val quantity: Int,
)
