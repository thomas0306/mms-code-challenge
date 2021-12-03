package com.mms.oms.domain.service

import com.mms.oms.adapters.kafka.ShipmentProducerImpl
import com.mms.oms.adapters.repository.ShipmentRepository
import com.mms.oms.config.extension.randomNumberSuffix
import com.mms.oms.domain.model.Order
import com.mms.oms.domain.model.Shipment
import com.mms.oms.domain.model.ShipmentStatus
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

class ShipmentServiceImpl : ShipmentService, KoinComponent {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val shipmentProducer: ShipmentProducerImpl by inject()

    override suspend fun createShipment(order: Order) {
        logger.info("[TODO] Sending order [${order.id}] for shipment...")
        val oneWeekLater = LocalDate.now().plusWeeks(1).atStartOfDay().toInstant(ZoneOffset.UTC)

        val shipment = Shipment(
            orderId = order.id,
            carrier = "DHL",
            createdAt = Instant.now(),
            estimatedDeliveryAt = oneWeekLater,
            status = ShipmentStatus.NOTIFIED,
            trackingNumber = "DE".randomNumberSuffix(8)
        )

        shipmentProducer.produce(shipment)
    }

    override suspend fun persistShipment(shipment: Shipment) = newSuspendedTransaction {
        ShipmentRepository.insert {
            it[id] = shipment.id
            it[carrier] = shipment.carrier
            it[trackingNumber] = shipment.trackingNumber
            it[status] = shipment.status
            it[orderId] = shipment.orderId
            it[estimatedDeliveryAt] = shipment.estimatedDeliveryAt
            shipment.actualDeliveryAt?.apply {
                it[actualDeliveryAt] = this
            }
            it[createdAt] = shipment.createdAt
            it[updatedAt] = shipment.updatedAt
        }

        return@newSuspendedTransaction
    }

    override suspend fun updateShipment(shipment: Shipment) = newSuspendedTransaction {
        TODO("updateShipment not implemented")
    }
}
