package com.mms.oms

import com.mms.oms.adapters.rest.configureOrderRouting
import com.mms.oms.adapters.rest.configurePaymentRouting
import com.mms.oms.config.configureDatabase
import com.mms.oms.config.configureDependencyInjection
import com.mms.oms.config.kafka.configureKafka
import com.mms.oms.config.serialization.configureSerialization
import com.mms.oms.plugins.configureMonitoring
import io.ktor.application.Application

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    // Technical
    configureDependencyInjection()
    configureSerialization()
    configureMonitoring()
    configureDatabase()

    // Functional
    configureKafka()
    configureOrderRouting()
    configurePaymentRouting()
}
