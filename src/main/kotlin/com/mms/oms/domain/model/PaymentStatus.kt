package com.mms.oms.domain.model

enum class PaymentStatus {
    AWAIT_PAYMENT_INFO,
    AUTHORIZED,
    PROCESSING,
    COLLECTED,
    FAILURE,
    REFUNDED,
}
