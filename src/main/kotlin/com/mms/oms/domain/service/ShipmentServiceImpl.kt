package com.mms.oms.domain.service

import com.mms.oms.adapters.kafka.OrderProducerImpl
import com.mms.oms.adapters.kafka.ShipmentProducerImpl
import com.mms.oms.adapters.repository.ShipmentRepository
import com.mms.oms.config.extension.randomNumberSuffix
import com.mms.oms.domain.mapper.ShipmentStatusMapper
import com.mms.oms.domain.model.Order
import com.mms.oms.domain.model.OrderStatus
import com.mms.oms.domain.model.Shipment
import com.mms.oms.domain.model.ShipmentStatus
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

class ShipmentServiceImpl : ShipmentService, KoinComponent {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val orderService: OrderService by inject()
    private val orderProducer: OrderProducerImpl by inject()
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

        // TODO handle multiple shipment for single order?
        markOrderAsInFulfillment(shipment)

        return@newSuspendedTransaction
    }

    private suspend fun markOrderAsInFulfillment(shipment: Shipment) {
        val order = orderService.getOrder(shipment.orderId)
        val updatedOrder = order.copy(
            status = OrderStatus.IN_FULFILLMENT
        )

        orderProducer.produce(updatedOrder)
    }

    override suspend fun updateShipment(shipment: Shipment) = newSuspendedTransaction {
        ShipmentRepository.update({ ShipmentRepository.trackingNumber eq shipment.trackingNumber }) {
            shipment.status.apply {
                it[status] = shipment.status
            }

            it[updatedAt] = Instant.now()
        }

        val order = orderService.getOrder(shipment.orderId)
        val updatedOrder = order.copy(
            status = ShipmentStatusMapper.toOrderStatus(shipment.status)
        )

        orderProducer.produce(updatedOrder)

        return@newSuspendedTransaction
    }
}
