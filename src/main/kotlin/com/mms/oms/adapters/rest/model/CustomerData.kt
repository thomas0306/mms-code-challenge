package com.mms.oms.adapters.rest.model

import kotlinx.serialization.Serializable

@Serializable
data class CustomerData(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val emailAddress: String,
    val addresses: List<Address>,
)
