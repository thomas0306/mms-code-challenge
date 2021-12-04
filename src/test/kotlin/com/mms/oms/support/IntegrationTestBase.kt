package com.mms.oms.support

import com.typesafe.config.ConfigFactory
import io.ktor.config.HoconApplicationConfig
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.createTestEnvironment
import io.ktor.util.InternalAPI
import org.junit.BeforeClass
import org.slf4j.LoggerFactory

open class IntegrationTestBase {
    companion object {
        private val logger = LoggerFactory.getLogger(javaClass)

        val config = HoconApplicationConfig(
            ConfigFactory
                .load("application-test.conf")
                .withFallback(
                    ConfigFactory.load("application-test-local.conf")
                )
        )
        private val inMemoryDatabase = InMemoryDatabase(config)
        private val inMemoryKafka = InMemoryKafka()

        val engine = TestApplicationEngine(
            createTestEnvironment {
                config = HoconApplicationConfig(ConfigFactory.load("application-test.conf"))
            }
        )

        val database = InMemoryDatabase(config)

        @OptIn(InternalAPI::class)
        @BeforeClass
        @JvmStatic
        fun setup() {
            if (engine.application.isEmpty) {
                logger.debug("Starting application for test")
                inMemoryDatabase.start()
                inMemoryKafka.start()
                engine.start()
            }
        }
    }
}
