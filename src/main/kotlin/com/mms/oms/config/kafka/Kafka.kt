package com.mms.oms.config.kafka

import com.mms.oms.adapters.kafka.OrderProcessorImpl
import com.mms.oms.adapters.kafka.PaymentProcessorImpl
import com.mms.oms.adapters.kafka.ShipmentProcessorImpl
import io.ktor.application.Application
import io.ktor.application.install
import org.koin.ktor.ext.inject

fun Application.configureKafka() {
    val orderProcessor: OrderProcessorImpl by inject()
    val paymentProcessor: PaymentProcessorImpl by inject()
    val shipmentProcessor: ShipmentProcessorImpl by inject()

    install(BackgroundJob.BackgroundJobFeature()) {
        name = "order-event-consumer"
        job = buildConsumer<String, String>(
            environment,
            "order-event",
            orderProcessor,
            4
        )
    }

    install(BackgroundJob.BackgroundJobFeature()) {
        name = "payment-event-consumer"
        job = buildConsumer<String, String>(
            environment,
            "payment-event",
            paymentProcessor,
            4
        )
    }

    install(BackgroundJob.BackgroundJobFeature()) {
        name = "shipment-event-consumer"
        job = buildConsumer<String, String>(
            environment,
            "shipment-event",
            shipmentProcessor,
            4
        )
    }
}
