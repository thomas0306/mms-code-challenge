package com.mms.oms.adapters.rest.model

import com.mms.oms.config.serialization.BigDecimalSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class Cart(
    val currency: String,
    @Serializable(with = BigDecimalSerializer::class)
    val totalPrice: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val discountedPrice: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val shippingPrice: BigDecimal,

    val items: List<Item> = listOf(),
) {

}
