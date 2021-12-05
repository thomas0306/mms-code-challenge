package com.mms.oms.domain.model

import com.mms.oms.config.serialization.InstantSerializer
import com.mms.oms.config.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

@Serializable
data class Address(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant,
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant = Instant.now(),
    val roles: Set<AddressRole> = setOf(AddressRole.DELIVERY, AddressRole.BILLING),
    val street: String,
    val streetNumber: String,
    val zipCode: String,
    val city: String,
    val country: String,
)
