package com.mms.oms.domain.mapper

import com.mms.oms.adapters.rest.model.Address
import java.time.Instant

typealias DomainAddress = com.mms.oms.domain.model.Address

object AddressMapper {
    fun toDomain(address: Address, orderDate: Instant) = DomainAddress(
        roles = address.roles.map { AddressRoleMapper.toDomain(it) }.toSet(),
        createdAt = orderDate,
        street = address.street,
        streetNumber = address.streetNumber,
        zipCode = address.zipCode,
        city = address.city,
        country = address.country,
    )

}
