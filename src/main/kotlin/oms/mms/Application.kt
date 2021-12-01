package oms.mms

import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import oms.mms.plugins.configureMonitoring
import oms.mms.plugins.configureRouting
import oms.mms.plugins.configureSerialization

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureMonitoring()
        configureSerialization()
    }.start(wait = true)
}
