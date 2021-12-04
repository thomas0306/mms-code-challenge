package com.mms.oms.support

import com.consol.citrus.kafka.embedded.EmbeddedKafkaServer
import com.consol.citrus.kafka.embedded.EmbeddedKafkaServerBuilder
import kotlin.concurrent.thread

class InMemoryKafka {
    private val embeddedKafka: EmbeddedKafkaServer = EmbeddedKafkaServerBuilder().kafkaServerPort(59092).build()

    fun start() {
        thread(isDaemon = true, name = "in-memory-kafka") {
            embeddedKafka.start()
        }
        Thread.sleep(5000)
    }

    fun stop() {
        embeddedKafka.stop()
    }
}
