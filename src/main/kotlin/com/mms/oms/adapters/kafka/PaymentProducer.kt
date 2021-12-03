package com.mms.oms.adapters.kafka

import com.mms.oms.domain.model.Payment
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory

class PaymentProducer : KoinComponent {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val kafkaProducer: KafkaProducer<String, String> by inject()

    fun produce(payment: Payment) {
        val paymentRecord = ProducerRecord(
            "payment-event",
            // use order id to persist processing sequence of payments
            payment.orderId.toString(),
            Json.encodeToString(payment)
        )
        logger.info("Producing payment event [${payment.id}], status [${payment.status}] and order [${payment.orderId}]")
        kafkaProducer.send(paymentRecord)
    }

    fun produceDLT(payment: Payment) {
        val paymentRecord = ProducerRecord(
            "payment-event-dlt",
            // use order id to persist processing sequence of payments
            payment.orderId.toString(),
            Json.encodeToString(payment)
        )
        logger.info("Producing payment DLT event [${payment.id}], status [${payment.status}] and order [${payment.orderId}]")
        kafkaProducer.send(paymentRecord)
    }
}
