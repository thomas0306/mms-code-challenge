package com.mms.oms.adapters.rest.model

import com.mms.oms.config.DateAsStringSerializer
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Order(
    @Serializable(with = DateAsStringSerializer::class)
    val orderDate: Date,
    val customerData: CustomerData,
    val cart: Cart,
)
