package com.mms.oms.config.kafka

import com.mms.oms.adapters.kafka.OrderProcessorImpl
import com.mms.oms.adapters.kafka.PaymentProcessorImpl
import io.ktor.application.Application
import io.ktor.application.install
import org.koin.ktor.ext.inject

fun Application.configureKafka() {
    val orderProcessor: OrderProcessorImpl by inject()
    val paymentProcessor: PaymentProcessorImpl by inject()

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
}
