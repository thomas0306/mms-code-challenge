package com.mms.oms.domain.model

import com.mms.oms.config.serialization.BigDecimalSerializer
import com.mms.oms.config.serialization.InstantSerializer
import com.mms.oms.config.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Serializable
data class Payment(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),
    val transactionId: String,
    val pspReference: String,
    val status: PaymentStatus,
    val currency: String,
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant,
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant = Instant.now(),
    @Serializable(with = UUIDSerializer::class)
    val orderId: UUID,
)
