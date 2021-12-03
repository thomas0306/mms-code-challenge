package com.mms.oms.adapters.rest.model

import com.mms.oms.config.serialization.BigDecimalSerializer
import com.mms.oms.config.serialization.InstantSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.Instant

@Serializable
data class Payment(
    val transactionId: String,
    val merchantReference: String,
    val pspReference: String,
    val eventCode: EventCode,
    val currency: String,
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant,
)
