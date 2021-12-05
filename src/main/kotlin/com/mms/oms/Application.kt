package com.mms.oms

import com.mms.oms.adapters.rest.configureOrderRouting
import com.mms.oms.adapters.rest.configurePaymentRouting
import com.mms.oms.config.configureDatabase
import com.mms.oms.config.configureDependencyInjection
import com.mms.oms.config.configureStatusPage
import com.mms.oms.config.cron.configureScheduler
import com.mms.oms.config.kafka.configureKafka
import com.mms.oms.config.serialization.configureSerialization
import com.mms.oms.plugins.configureMonitoring
import io.ktor.application.Application
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() = runBlocking {
    // Technical
    configureDependencyInjection()
    configureSerialization()
    configureMonitoring()
    configureDatabase()
    configureKafka()
    configureStatusPage()
    configureScheduler()

    // Functional
    configureOrderRouting()
    configurePaymentRouting()
}
