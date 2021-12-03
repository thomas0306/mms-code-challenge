package com.mms.oms.adapters.rest.model

import com.mms.oms.config.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class PaymentResponse(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val transactionId: String,
)
