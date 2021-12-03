package com.mms.oms.domain.mapper

import com.mms.oms.adapters.rest.model.EventCode
import com.mms.oms.domain.model.PaymentStatus

object PaymentStatusMapper {
    fun from(eventCode: EventCode) = when(eventCode) {
        EventCode.AUTHORISATION -> PaymentStatus.AUTHORIZED
        else -> throw UnsupportedOperationException("Event code [$eventCode] is currently not supported")
    }
}
