package com.mms.oms.domain.mapper

import com.mms.oms.adapters.rest.model.Payment
import com.mms.oms.adapters.rest.model.PaymentResponse
import java.util.UUID

typealias DomainPayment = com.mms.oms.domain.model.Payment

object PaymentMapper {
    fun toDomain(payment: Payment) = DomainPayment(
        transactionId = payment.transactionId,
        pspReference = payment.pspReference,
        status = PaymentStatusMapper.from(payment.eventCode),
        currency = payment.currency,
        amount = payment.amount,
        createdAt = payment.createdAt,
        orderId = UUID.fromString(payment.merchantReference),
    )

    fun toApi(domainPayment: DomainPayment) = PaymentResponse(
        id = domainPayment.id,
        transactionId = domainPayment.transactionId,
    )
}
