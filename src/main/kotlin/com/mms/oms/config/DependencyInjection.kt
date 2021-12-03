package com.mms.oms.config

import com.mms.oms.adapters.kafka.OrderProcessorImpl
import com.mms.oms.adapters.kafka.OrderProducerImpl
import com.mms.oms.adapters.kafka.PaymentProcessorImpl
import com.mms.oms.adapters.kafka.PaymentProducerImpl
import com.mms.oms.adapters.kafka.ShipmentProcessorImpl
import com.mms.oms.adapters.kafka.ShipmentProducerImpl
import com.mms.oms.config.kafka.KafkaProcessor
import com.mms.oms.config.kafka.buildProducer
import com.mms.oms.domain.service.OrderService
import com.mms.oms.domain.service.OrderServiceImpl
import com.mms.oms.domain.service.PaymentService
import com.mms.oms.domain.service.PaymentServiceImpl
import com.mms.oms.domain.service.ShipmentService
import com.mms.oms.domain.service.ShipmentServiceImpl
import io.ktor.application.Application
import io.ktor.application.install
import org.apache.kafka.clients.producer.KafkaProducer
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.ktor.ext.Koin

fun Application.configureDependencyInjection() {

    val appModules = module {
        // Kafka
        single { buildProducer<String, String>(environment) as KafkaProducer<String, String> }

        // Order
        single<OrderService> { OrderServiceImpl() }
        single<KafkaProcessor<String, String>>(named("order-processor")) { OrderProcessorImpl() }
        single { OrderProducerImpl() }

        // Payment
        single<PaymentService> { PaymentServiceImpl() }
        single<KafkaProcessor<String, String>>(named("payment-processor")) { PaymentProcessorImpl() }
        single { PaymentProducerImpl() }

        // Shipment
        single<ShipmentService> { ShipmentServiceImpl() }
        single<KafkaProcessor<String, String>>(named("shipment-processor")) { ShipmentProcessorImpl() }
        single { ShipmentProducerImpl() }
    }

    install(Koin) {
        modules(appModules)
    }
}
