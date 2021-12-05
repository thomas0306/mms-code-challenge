package com.mms.oms.domain.mapper

import com.mms.oms.adapters.rest.model.AddressRole

typealias DomainAddressRole = com.mms.oms.domain.model.AddressRole

object AddressRoleMapper {
    fun toDomain(addressRole: AddressRole) = when (addressRole) {
        AddressRole.DELIVERY -> DomainAddressRole.DELIVERY
        AddressRole.BILLING -> DomainAddressRole.BILLING
    }
}
