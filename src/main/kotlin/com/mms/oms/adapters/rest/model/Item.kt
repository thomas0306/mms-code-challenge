package com.mms.oms.adapters.rest.model

import com.mms.oms.config.serialization.BigDecimalSerializer
import kotlinx.serialization.Serializable
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isPositive
import org.valiktor.functions.isPositiveOrZero
import org.valiktor.validate
import java.math.BigDecimal

@Serializable
data class Item(
    val itemId: String,
    @Serializable(with = BigDecimalSerializer::class)
    val unitPrice: BigDecimal,
    val quantity: Int,
) {
    init {
        validate(this) {
            validate(Item::itemId).isNotEmpty().hasSize(4, 20)
            validate(Item::unitPrice).isPositiveOrZero()
            validate(Item::quantity).isPositive()
        }
    }
}
