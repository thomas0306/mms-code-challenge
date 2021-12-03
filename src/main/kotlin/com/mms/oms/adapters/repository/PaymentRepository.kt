package com.mms.oms.adapters.repository

import com.mms.oms.domain.model.PaymentStatus
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant
import java.util.UUID

object PaymentRepository : IdTable<UUID>("payment") {
    override val id = uuid("id").entityId()
    val transactionId = varchar("transaction_id", 30)
    val pspReference = varchar("psp_reference", 20)
    val status = enumerationByName("status", 30, PaymentStatus::class)
    val currency = char("currency", 3)
    val amount = float("amount")
    val orderId = uuid("order_id")
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at").clientDefault { Instant.now() }
}
