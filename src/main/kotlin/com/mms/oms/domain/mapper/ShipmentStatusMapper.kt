package com.mms.oms.domain.mapper

import com.mms.oms.domain.model.OrderStatus
import com.mms.oms.domain.model.ShipmentStatus

object ShipmentStatusMapper {
    fun toOrderStatus(status: ShipmentStatus) = when (status) {
        ShipmentStatus.DELIVERED -> OrderStatus.CLOSED
        ShipmentStatus.DELIVERY_FAILED -> OrderStatus.FULFILLMENT_FAILED
        else -> throw UnsupportedOperationException("ShipmentStatus $status is currently not implemented")
    }
}
