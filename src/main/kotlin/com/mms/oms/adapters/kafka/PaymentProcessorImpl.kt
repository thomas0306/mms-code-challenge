package com.mms.oms.adapters.kafka

import com.mms.oms.config.kafka.KafkaProcessor
import com.mms.oms.domain.model.Payment
import com.mms.oms.domain.service.PaymentService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory

class PaymentProcessorImpl : KafkaProcessor<String, String>, KoinComponent {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val paymentService: PaymentService by inject()
    private val paymentProducer: PaymentProducer by inject()

    override suspend fun process(record: ConsumerRecord<String, String>) {
        logger.info("Processing payment [${record.key()}]")
        val payment = Json.decodeFromString<Payment>(record.value())
        paymentService.processPayment(payment)
        logger.info("Processed payment [${payment.id}]")
    }

    override suspend fun recover(record: ConsumerRecord<String, String>) {
        val payment = Json.decodeFromString<Payment>(record.value())
        paymentProducer.produceDLT(payment)
    }
}
