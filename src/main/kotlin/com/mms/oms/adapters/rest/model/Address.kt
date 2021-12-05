package com.mms.oms.adapters.rest.model

import kotlinx.serialization.Serializable
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotEmpty
import org.valiktor.validate

@Serializable
data class Address(
    val roles: Set<AddressRole> = setOf(AddressRole.DELIVERY, AddressRole.BILLING),
    val street: String,
    val streetNumber: String,
    val zipCode: String,
    val city: String,
    val country: String,
) {
    init {
        validate(this) {
            validate(Address::roles).isNotEmpty().hasSize(1, 2)
            validate(Address::street).hasSize(2, 50)
            validate(Address::streetNumber).hasSize(1, 10)
            validate(Address::zipCode).hasSize(5, 15)
            validate(Address::city).hasSize(2, 50)
            validate(Address::country).hasSize(2, 50)
        }
    }
}

enum class AddressRole {
    DELIVERY,
    BILLING
}
