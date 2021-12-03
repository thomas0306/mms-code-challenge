package com.mms.oms.adapters.kafka

import com.mms.oms.domain.model.Payment
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory

class PaymentProducerImpl : KoinComponent {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val kafkaProducer: KafkaProducer<String, String> by inject()

    fun produce(payment: Payment) {
        payment.run {
            val paymentRecord = ProducerRecord(
                "payment-event",
                id.toString(),
                Json.encodeToString(this)
            )
            logger.info("Producing payment event [$id], status [$status] and order [$orderId]")
            kafkaProducer.send(paymentRecord)
        }
    }

    fun produceDLT(payment: Payment) {
        payment.run {
            val paymentRecord = ProducerRecord(
                "payment-event-dlt",
                // use order id to persist processing sequence of payments
                id.toString(),
                Json.encodeToString(this)
            )
            logger.info("Producing payment DLT event [$id], status [$status] and order [$orderId]")
            kafkaProducer.send(paymentRecord)
        }
    }
}
