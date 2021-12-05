package com.mms.oms.adapters.kafka

import com.mms.oms.config.kafka.KafkaProcessor
import com.mms.oms.domain.model.Order
import com.mms.oms.domain.model.OrderStatus.CLOSED
import com.mms.oms.domain.model.OrderStatus.CREATED
import com.mms.oms.domain.model.OrderStatus.FULFILLMENT_FAILED
import com.mms.oms.domain.model.OrderStatus.IN_FULFILLMENT
import com.mms.oms.domain.model.OrderStatus.PAID
import com.mms.oms.domain.service.OrderService
import com.mms.oms.domain.service.ShipmentService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory

class OrderProcessorImpl : KafkaProcessor<String, String>, KoinComponent {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val orderService: OrderService by inject()
    private val orderProducer: OrderProducerImpl by inject()
    private val shipmentService: ShipmentService by inject()

    override suspend fun process(record: ConsumerRecord<String, String>) {
        logger.info("Processing order [${record.key()}]")
        val order = Json.decodeFromString<Order>(record.value())
        when (order.status) {
            CREATED -> orderService.persistOrder(order)
            PAID -> {
                orderService.updateOrder(order)
                shipmentService.createShipment(order)
            }
            IN_FULFILLMENT, CLOSED, FULFILLMENT_FAILED -> orderService.updateOrder(order)
        }

        logger.info("Processed order [${record.key()}]")
    }

    override suspend fun recover(record: ConsumerRecord<String, String>) {
        val order = Json.decodeFromString<Order>(record.value())
        orderProducer.produceDLT(order)
    }
}
