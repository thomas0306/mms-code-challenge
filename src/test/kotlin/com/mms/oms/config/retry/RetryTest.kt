package com.mms.oms.config.retry

import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import kotlin.test.Test
import kotlin.test.assertFailsWith

internal class RetryTest {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Test
    fun `should not retry on success`(): Unit = runBlocking {
        Retry.withRetry(
            maxAttempt = 2,
            recover = { throw Exception("This exception should not be thrown") },
        ) {
            logger.info("This is a happy case")
        }
    }

    @Test
    fun `should retry and succeed the last time`(): Unit = runBlocking {
        val executions = sequence {
            yield(false)
            yield(true)
        }.iterator()
        Retry.withRetry(
            maxAttempt = 2,
            recover = { throw Exception("This exception should not be thrown") },
        ) {
            if (executions.next()) {
                logger.info("Ohh I succeed")
                return@withRetry
            }
            throw Exception("I failed!")
        }
    }

    @Test
    fun `should not retry after maxAttempt`(): Unit = runBlocking {
        val executions = sequence {
            yield(false)
            yield(false)
            yield(true)
        }.iterator()

        assertFailsWith<RetryExhaustedException> {
            Retry.withRetry(
                maxAttempt = 2,
            ) {
                if (executions.next()) {
                    logger.info("Ohh I succeed")
                    return@withRetry
                }
                throw Exception("I failed!")
            }
        }
    }

    @Test
    fun `should trigger recover`(): Unit = runBlocking {
        val executions = sequence {
            yield(false)
            yield(false)
            yield(true)
        }.iterator()

        Retry.withRetry(
            maxAttempt = 2,
            recover = { logger.info("I am recovering") }
        ) {
            if (executions.next()) {
                logger.info("Ohh I succeed")
                return@withRetry
            }
            throw Exception("I failed!")
        }
    }

    @Test
    fun `should immediately recover on excluded exceptions`(): Unit = runBlocking {
        val executions = sequence {
            yield(false)
            yield(false)
            yield(true)
        }.iterator()

        Retry.withRetry(
            maxAttempt = 2,
            onExceptions = setOf(NullPointerException::class),
            recover = { logger.info("I am recovering") }
        ) {
            if (executions.next()) {
                logger.info("Ohh I succeed")
                return@withRetry
            }
            throw IllegalStateException("I am an exception not in the list!")
        }
    }

    @Test
    fun `should immediately throw exception on excluded exceptions`(): Unit = runBlocking {
        val executions = sequence {
            yield(false)
            yield(false)
            yield(true)
        }.iterator()

        assertFailsWith<RetryExemptedException> {
            Retry.withRetry(
                maxAttempt = 2,
                onExceptions = setOf(NullPointerException::class),
            ) {
                if (executions.next()) {
                    logger.info("Ohh I succeed")
                    return@withRetry
                }
                throw IllegalStateException("I am an exception not in the list!")
            }
        }
    }
}
