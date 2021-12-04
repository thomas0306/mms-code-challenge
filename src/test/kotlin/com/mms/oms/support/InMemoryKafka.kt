package com.mms.oms.support

import com.consol.citrus.kafka.embedded.EmbeddedKafkaServer
import com.consol.citrus.kafka.embedded.EmbeddedKafkaServerBuilder

class InMemoryKafka {
    private val embeddedKafka: EmbeddedKafkaServer = EmbeddedKafkaServerBuilder().kafkaServerPort(59092).build()

    fun start() {
        embeddedKafka.start()
    }

    fun stop() {
        embeddedKafka.stop()
    }
}
