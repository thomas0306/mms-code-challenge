package com.mms.oms.adapters.kafka

import com.mms.oms.domain.model.Order
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory

class OrderProducerImpl : KoinComponent {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val kafkaProducer: KafkaProducer<String, String> by inject()

    fun produce(order: Order) {
        order.run {
            val orderRecord = ProducerRecord(
                "order-event",
                id.toString(),
                Json.encodeToString(this)
            )
            logger.info("Producing order event [$id], status [$status]")
            kafkaProducer.send(orderRecord)
        }
    }

    fun produceDLT(order: Order) {
        order.run {
            val orderRecord = ProducerRecord(
                "order-event-dlt",
                id.toString(),
                Json.encodeToString(this)
            )
            logger.info("Producing order DLT event [$id], status [$status]")
            kafkaProducer.send(orderRecord)
        }
    }
}
