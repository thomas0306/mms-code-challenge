package com.mms.oms.config

import com.mms.oms.adapters.kafka.OrderProcessorImpl
import com.mms.oms.config.kafka.BackgroundJob
import com.mms.oms.config.kafka.buildConsumer
import io.ktor.application.Application
import io.ktor.application.install
import org.koin.ktor.ext.inject

fun Application.configureKafka() {
    val orderProcessor: OrderProcessorImpl by inject()
    install(BackgroundJob.BackgroundJobFeature) {
        name = "kafka-consumer"
        job = buildConsumer<String, String>(
            environment,
            "order-event",
            orderProcessor
        )
    }
}
