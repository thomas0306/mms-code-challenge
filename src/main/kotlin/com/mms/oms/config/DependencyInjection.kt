package com.mms.oms.config

import com.mms.oms.adapters.kafka.OrderProcessorImpl
import com.mms.oms.adapters.kafka.OrderProducer
import com.mms.oms.config.kafka.buildProducer
import com.mms.oms.domain.service.OrderService
import com.mms.oms.domain.service.OrderServiceImpl
import io.ktor.application.Application
import io.ktor.application.install
import org.apache.kafka.clients.producer.KafkaProducer
import org.koin.dsl.module
import org.koin.ktor.ext.Koin

fun Application.configureDependencyInjection() {

    val appModules = module {
        single { buildProducer<String, String>(environment) as KafkaProducer<String, String> }
        single { OrderServiceImpl() as OrderService }
        single { OrderProcessorImpl() }
        single { OrderProducer() }
    }

    install(Koin) {
        modules(appModules)
    }
}
