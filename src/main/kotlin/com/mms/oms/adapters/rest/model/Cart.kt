package com.mms.oms.adapters.rest.model

import com.mms.oms.config.DecimalAsStringSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class Cart(
    val currency: String,
    @Serializable(with = DecimalAsStringSerializer::class)
    val totalPrice: BigDecimal,
    @Serializable(with = DecimalAsStringSerializer::class)
    val discountedPrice: BigDecimal,
    @Serializable(with = DecimalAsStringSerializer::class)
    val shippingPrice: BigDecimal,

    val items: List<Item> = listOf(),
)
