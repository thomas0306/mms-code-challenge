package com.mms.oms.adapters.kafka

import com.mms.oms.domain.model.Shipment
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory

class ShipmentProducerImpl : KoinComponent {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val kafkaProducer: KafkaProducer<String, String> by inject()

    fun produce(shipment: Shipment) {
        shipment.run {
            val shipmentRecord = ProducerRecord(
                "shipment-event",
                id.toString(),
                Json.encodeToString(this)
            )
            logger.info("Producing shipment event [$id], status [$status] and order [$orderId]")
            kafkaProducer.send(shipmentRecord)
        }
    }

    fun produceDLT(shipment: Shipment) {
        shipment.run {
            val shipmentRecord = ProducerRecord(
                "shipment-event-dlt",
                id.toString(),
                Json.encodeToString(this)
            )
            logger.info("Producing payment DLT event [$id], status [$status] and order [$orderId]")
            kafkaProducer.send(shipmentRecord)
        }
    }
}
