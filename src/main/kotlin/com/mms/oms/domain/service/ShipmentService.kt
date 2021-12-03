package com.mms.oms.domain.service

import com.mms.oms.domain.model.Order
import com.mms.oms.domain.model.Shipment

interface ShipmentService {
    suspend fun createShipment(order: Order)
    suspend fun persistShipment(shipment: Shipment)
    suspend fun updateShipment(shipment: Shipment)
}
