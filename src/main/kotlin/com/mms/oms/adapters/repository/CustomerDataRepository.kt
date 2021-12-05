package com.mms.oms.adapters.repository

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant
import java.util.UUID

object CustomerDataRepository : IdTable<UUID>("customer_data") {
    override val id = uuid("id").entityId()
    val firstName = varchar("first_name", 30)
    val lastName = varchar("last_name", 30)
    val phoneNumber = varchar("phone_number", 30)
    val emailAddress = varchar("email_address", 50)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at").clientDefault { Instant.now() }
}
