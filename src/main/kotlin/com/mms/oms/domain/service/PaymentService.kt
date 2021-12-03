package com.mms.oms.domain.service

import com.mms.oms.domain.model.Payment
import java.util.UUID

interface PaymentService {
    suspend fun submitPayment(payment: Payment)
    suspend fun processPayment(payment: Payment)
    suspend fun maybeConcludesPayment(orderId: UUID)
}

