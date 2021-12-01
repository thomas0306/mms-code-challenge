package com.mms.oms.adapters.rest.model

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val role: List<AddressRole> = listOf(AddressRole.DELIVERY, AddressRole.BILLING),
    val street: String,
    val streetNumber: String,
    val zipCode: String,
    val city: String,
    val country: String,
)

enum class AddressRole {
    DELIVERY,
    BILLING
}
