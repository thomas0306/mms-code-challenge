package com.mms.oms.adapters.rest.model

import com.mms.oms.config.serialization.BigDecimalSerializer
import kotlinx.serialization.Serializable
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isPositiveOrZero
import org.valiktor.validate
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
    init {
        validate(this) {
            validate(Cart::items).isNotEmpty()
            validate(Cart::totalPrice).isPositiveOrZero()
            validate(Cart::discountedPrice).isPositiveOrZero()
            validate(Cart::shippingPrice).isPositiveOrZero()
            validate(Cart::currency).hasSize(3, 3)
        }
    }
}
