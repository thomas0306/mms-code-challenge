package com.mms.oms.config.kafka

import io.ktor.application.Application
import io.ktor.application.ApplicationFeature
import io.ktor.util.AttributeKey
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.util.UUID
import kotlin.concurrent.thread

class BackgroundJob(configuration: JobConfiguration) : Closeable {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val job = configuration.job
    private val name = configuration.name

    class JobConfiguration {
        var name: String? = null
        var job: ClosableJob? = null
    }

    class BackgroundJobFeature : ApplicationFeature<Application, JobConfiguration, BackgroundJob> {
        override val key: AttributeKey<BackgroundJob> = AttributeKey("BackgroundJob-${UUID.randomUUID()}")

        override fun install(pipeline: Application, configure: JobConfiguration.() -> Unit): BackgroundJob {
            val configuration = JobConfiguration().apply(configure)
            val backgroundJob = BackgroundJob(configuration)
            configuration.job?.let { thread(name = configuration.name) { it.run() } }
            return backgroundJob
        }
    }

    override fun close() {
        logger.info("Closing $name job")
        job?.close()
        logger.info("Job $name closed")
    }
}
