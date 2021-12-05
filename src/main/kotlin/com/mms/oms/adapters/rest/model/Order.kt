package com.mms.oms.adapters.rest.model

import com.mms.oms.config.serialization.InstantSerializer
import kotlinx.serialization.Serializable
import org.valiktor.functions.hasSize
import org.valiktor.functions.isGreaterThan
import org.valiktor.validate
import java.time.Instant
import java.time.temporal.ChronoUnit

@Serializable
data class Order(
    @Serializable(with = InstantSerializer::class)
    val orderDate: Instant,
    val customerData: CustomerData,
    val cart: Cart,
    val tenant: String,
) {
    init {
        validate(this) {
            validate(Order::orderDate)
                .isGreaterThan(Instant.now().minus(365, ChronoUnit.DAYS))
            validate(Order::customerData)
            validate(Order::tenant).hasSize(2, 2)
        }
    }
}
