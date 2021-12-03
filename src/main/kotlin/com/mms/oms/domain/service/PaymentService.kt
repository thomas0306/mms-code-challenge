package com.mms.oms.domain.service

import com.mms.oms.domain.model.Payment

interface PaymentService {
    suspend fun submitPayment(payment: Payment)
    suspend fun processPayment(payment: Payment)
}

