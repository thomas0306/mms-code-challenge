package com.mms.oms.adapters.rest.model

import com.mms.oms.config.serialization.BigDecimalSerializer
import com.mms.oms.config.serialization.InstantSerializer
import kotlinx.serialization.Serializable
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotEmpty
import org.valiktor.validate
import java.math.BigDecimal
import java.time.Instant

@Serializable
data class Payment(
    val transactionId: String,
    val merchantReference: String,
    val pspReference: String,
    val eventCode: EventCode,
    val currency: String,
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant,
) {
    init {
        validate(this) {
            validate(Payment::transactionId).isNotEmpty().hasSize(5, 30)
            validate(Payment::merchantReference).isNotEmpty().hasSize(5, 36)
            validate(Payment::pspReference).isNotEmpty().hasSize(6, 16)
            validate(Payment::currency).isNotEmpty().hasSize(3, 3)
        }
    }
}
