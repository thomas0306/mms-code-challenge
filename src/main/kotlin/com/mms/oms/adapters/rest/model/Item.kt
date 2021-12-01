package com.mms.oms.adapters.rest.model

import com.mms.oms.config.DecimalAsStringSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class Item(
    val itemId: String,
    @Serializable(with = DecimalAsStringSerializer::class)
    val unitPrice: BigDecimal,
    val quantity: Int,
)
