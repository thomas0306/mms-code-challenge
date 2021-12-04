package com.mms.oms.support

import com.typesafe.config.ConfigFactory
import io.ktor.config.HoconApplicationConfig
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.createTestEnvironment
import org.junit.AfterClass
import org.junit.BeforeClass
import org.slf4j.LoggerFactory

open class BaseTest {
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

        @BeforeClass
        @JvmStatic
        fun setup() {
            logger.debug("Starting application with config ....")
            inMemoryDatabase.start()
            inMemoryKafka.start()
            engine.start()
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            inMemoryDatabase.stop()
            inMemoryKafka.stop()
            engine.stop(5000, 5000)
        }
    }
}
