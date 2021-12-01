package com.mms.oms.adapters.database

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant
import java.util.UUID

object Orders : IdTable<UUID>("order") {
    override val id = uuid("id").entityId()
    val status = varchar("status", 30)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at").clientDefault { Instant.now() }
}
