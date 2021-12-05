package com.mms.oms.domain.mapper

import com.mms.oms.adapters.repository.ShipmentRepository
import com.mms.oms.domain.model.Shipment
import org.jetbrains.exposed.sql.ResultRow

object ShipmentMapper {
    fun toDomain(row: ResultRow) = Shipment(
        orderId = row[ShipmentRepository.orderId],
        carrier = row[ShipmentRepository.carrier],
        createdAt = row[ShipmentRepository.createdAt],
        updatedAt = row[ShipmentRepository.updatedAt],
        estimatedDeliveryAt = row[ShipmentRepository.estimatedDeliveryAt],
        status = row[ShipmentRepository.status],
        trackingNumber = row[ShipmentRepository.trackingNumber],
    )
}
