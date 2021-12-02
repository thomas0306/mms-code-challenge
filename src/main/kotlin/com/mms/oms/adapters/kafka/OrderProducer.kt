package com.mms.oms.adapters.kafka

import com.mms.oms.domain.model.Order
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory

class OrderProducer : KoinComponent {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val kafkaProducer: KafkaProducer<String, String> by inject()

    fun produce(order: Order) {
        val orderRecord = ProducerRecord(
            "order-event",
            order.id.toString(),
            Json.encodeToString(order)
        )
        logger.info("Producing order event [${order.id}], status [${order.status}]")
        kafkaProducer.send(orderRecord)
    }
}
