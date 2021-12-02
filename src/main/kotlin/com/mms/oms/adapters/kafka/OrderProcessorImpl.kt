package com.mms.oms.adapters.kafka

import com.mms.oms.config.kafka.KafkaProcessor
import com.mms.oms.domain.model.Order
import com.mms.oms.domain.service.OrderService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory

class OrderProcessorImpl : KafkaProcessor<String, String>, KoinComponent {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val orderService: OrderService by inject()

    override suspend fun process(record: ConsumerRecord<String, String>) {
        logger.info("Processing ${record.key()}...")
        val order = Json.decodeFromString<Order>(record.value())
        orderService.persistOrder(order)
    }
}
