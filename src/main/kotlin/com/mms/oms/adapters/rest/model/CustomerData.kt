package com.mms.oms.adapters.rest.model

import com.mms.oms.config.EMAIL_REGEX
import com.mms.oms.config.PHONE_NUMBER_REGEX
import kotlinx.serialization.Serializable
import org.valiktor.Validator
import org.valiktor.constraints.ContainsAll
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.matches
import org.valiktor.validate

fun <E> Validator<E>.Property<Iterable<Address>?>.hasValidAddressRoles() =
    this.validate(ContainsAll(listOf(AddressRole.DELIVERY, AddressRole.BILLING))) { addresses ->
        val roles = addresses?.map { it.role }?.flatten() ?: listOf()
        !(roles.size != 2 || roles.toSet().size < roles.size)
    }

@Serializable
data class CustomerData(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val emailAddress: String,
    val addresses: List<Address> = listOf(),
) {
    init {
        validate(this) {
            validate(CustomerData::firstName).hasSize(1, 30)
            validate(CustomerData::lastName).hasSize(1, 30)
            validate(CustomerData::phoneNumber).matches(PHONE_NUMBER_REGEX).hasSize(1, 30)
            validate(CustomerData::emailAddress).matches(EMAIL_REGEX).hasSize(1, 50)
            validate(CustomerData::addresses)
                .isNotEmpty()
                .hasSize(1, 2)
                .hasValidAddressRoles()
        }
    }
}
