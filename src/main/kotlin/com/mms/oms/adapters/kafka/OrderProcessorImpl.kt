package com.mms.oms.adapters.kafka

import com.mms.oms.config.kafka.KafkaProcessor
import com.mms.oms.domain.model.Order
import com.mms.oms.domain.model.OrderStatus
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
    private val orderProducer: OrderProducer by inject()

    override suspend fun process(record: ConsumerRecord<String, String>) {
        logger.info("Processing order [${record.key()}]")
        val order = Json.decodeFromString<Order>(record.value())
        when (order.status) {
            OrderStatus.CREATED -> orderService.persistOrder(order)
            OrderStatus.PAID -> {
                orderService.updateOrder(order)
//                shipmentService.scheduleFulfillment(order)
            }
        }

        logger.info("Processed order [${record.key()}]")
    }

    override suspend fun recover(record: ConsumerRecord<String, String>) {
        val order = Json.decodeFromString<Order>(record.value())
        orderProducer.produceDLT(order)
    }
}
