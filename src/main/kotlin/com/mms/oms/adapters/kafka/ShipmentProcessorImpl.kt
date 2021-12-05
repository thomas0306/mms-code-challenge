package com.mms.oms.adapters.kafka

import com.mms.oms.config.kafka.KafkaProcessor
import com.mms.oms.domain.model.Shipment
import com.mms.oms.domain.model.ShipmentStatus.DELIVERED
import com.mms.oms.domain.model.ShipmentStatus.DELIVERY_FAILED
import com.mms.oms.domain.model.ShipmentStatus.NOTIFIED
import com.mms.oms.domain.service.OrderService
import com.mms.oms.domain.service.ShipmentService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory

class ShipmentProcessorImpl : KafkaProcessor<String, String>, KoinComponent {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val shipmentService: ShipmentService by inject()
    private val shipmentProducer: ShipmentProducerImpl by inject()
    private val orderService: OrderService by inject()

    override suspend fun process(record: ConsumerRecord<String, String>) {
        logger.info("Processing shipment [${record.key()}]")
        val shipment = Json.decodeFromString<Shipment>(record.value())
        when (val status = shipment.status) {
            NOTIFIED -> shipmentService.persistShipment(shipment)
            DELIVERY_FAILED, DELIVERED -> shipmentService.updateShipment(shipment)
            else -> logger.error("Shipment status [$status] not implemented")
        }

        logger.info("Processed shipment [${record.key()}]")
    }

    override suspend fun recover(record: ConsumerRecord<String, String>) {
        val shipment = Json.decodeFromString<Shipment>(record.value())
        shipmentProducer.produceDLT(shipment)
    }
}
