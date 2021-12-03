package com.mms.oms.config.kafka

import io.ktor.application.Application
import io.ktor.application.install
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.NewTopic
import org.koin.core.qualifier.named
import org.koin.ktor.ext.get

fun Application.configureKafka() {

    val desiredTopics = environment.config.configList("kafka.topics")

    if (environment.config.property("ktor.environment").getString() == "LOCAL") {
        val adminClient = AdminClient.create(
            mapOf(
                "bootstrap.servers" to environment.config.property("kafka.bootstrap.servers").getList()
            )
        )

        val existingTopics = adminClient.listTopics().names().get()

        desiredTopics.forEach {
            val topicName = it.property("name").getString()
            val replica = it.property("replica").getString().toInt()
            val partition = it.property("partition").getString().toInt()
            listOf(topicName, "$topicName-dlt").forEach {
                if (topicName !in existingTopics) {
                    environment.log.info("Attempting to create missing topic [$topicName] in LOCAL environment")
                    val topicConfig = NewTopic(
                        it,
                        partition,
                        replica.toShort(),
                    )
                    adminClient.createTopics(listOf(topicConfig)).all().get()
                }
            }
        }
    }

    desiredTopics.forEach {
        val topicName = it.property("name").getString()
        val parallelism = it.property("parallelism").getString().toInt()
        val processorName = it.property("processor").getString()

        install(BackgroundJob.BackgroundJobFeature()) {
            val processor: KafkaProcessor<String, String> = get(named(processorName))
            name = "$topicName-consumer"
            job = buildConsumer<String, String>(
                environment,
                topicName,
                processor,
                parallelism,
            )
        }
    }
}
