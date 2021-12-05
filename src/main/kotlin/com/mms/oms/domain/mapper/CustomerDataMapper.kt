package com.mms.oms.domain.mapper

import com.mms.oms.adapters.rest.model.CustomerData
import java.time.Instant

typealias DomainCustomerData = com.mms.oms.domain.model.CustomerData

object CustomerDataMapper {
    fun toDomain(customerData: CustomerData, orderDate: Instant) = DomainCustomerData(
        createdAt = orderDate,
        firstName = customerData.firstName,
        lastName = customerData.lastName,
        phoneNumber = customerData.phoneNumber,
        emailAddress = customerData.emailAddress,
        addresses = customerData.addresses.map { AddressMapper.toDomain(it, orderDate) }
    )
}
