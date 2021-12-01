package com.mms.oms

import com.mms.oms.adapters.rest.configureOrderRouting
import com.mms.oms.config.DatabaseFactory
import com.mms.oms.plugins.configureMonitoring
import com.mms.oms.plugins.configureSerialization
import io.ktor.application.Application

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    configureOrderRouting()
    configureMonitoring()
    configureSerialization()
    DatabaseFactory.init()
}
