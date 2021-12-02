package com.mms.oms.adapters.rest.model

import com.mms.oms.config.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class Order(
    @Serializable(with = InstantSerializer::class)
    val orderDate: Instant,
    val customerData: CustomerData,
    val cart: Cart,
    val tenant: String,
)
