package com.mms.oms.adapters.repository

import com.mms.oms.config.expose.array
import com.mms.oms.domain.model.AddressRole
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.EnumerationNameColumnType
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant
import java.util.UUID

object AddressRepository : IdTable<UUID>("address") {
    override val id = uuid("id").entityId()
    val roles = array<AddressRole>("roles", EnumerationNameColumnType(AddressRole::class, 20))
    val street = varchar("street", 50)
    val streetNumber = varchar("street_number", 10)
    val zipCode = varchar("zip_code", 15)
    var city = varchar("city", 50)
    var country = varchar("country", 50)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at").clientDefault { Instant.now() }
    val customerDataId = uuid("customer_data_id").uniqueIndex().references(CustomerDataRepository.id)
}
