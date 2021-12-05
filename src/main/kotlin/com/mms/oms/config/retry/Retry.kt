package com.mms.oms.config.retry

import kotlinx.coroutines.delay
import kotlinx.coroutines.supervisorScope
import org.apache.kafka.common.utils.ExponentialBackoff
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

private val DEFAULT_BACKOFF_POLICY = ExponentialBackoff(500, 2, 10000, 0.1)

object Retry {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Throws(RetryExhaustedException::class, RetryExemptedException::class)
    suspend fun withRetry(
        maxAttempt: Int = 5,
        backoffPolicy: ExponentialBackoff = DEFAULT_BACKOFF_POLICY,
        onExceptions: Set<KClass<out Exception>> = setOf(),
        recover: (suspend () -> Unit)? = null,
        block: suspend () -> Unit,
    ) = supervisorScope {
        val isAlwaysRetry = onExceptions.isEmpty()
        var exceptionCaught: Exception? = null
        repeat(maxAttempt) {
            try {
                block()
                return@supervisorScope
            } catch (e: Exception) {
                exceptionCaught = e
                if (isAlwaysRetry || e::class in onExceptions) {
                    if ((it + 1) < maxAttempt) {
                        logger.debug("Attempt [${it + 1}/$maxAttempt] failed, proceed to retry")
                        delay(backoffPolicy.backoff(it.toLong()))
                    } else {
                        logger.debug("Attempt [${it + 1}/$maxAttempt] failed, stop retrying")
                    }
                } else {
                    if (recover != null) {
                        logger.error("${e::class.simpleName} exempted from retry, attempt recovery", e)
                        recover()
                        return@supervisorScope
                    }

                    throw RetryExemptedException("${e::class.simpleName} exempted from retry", e)
                }
            }
        }

        if (recover == null) {
            throw RetryExhaustedException("Maximum retry reached", exceptionCaught)
        }

        logger.error("Retry exhausted, attempt recovery", exceptionCaught)
        recover()
        return@supervisorScope
    }
}
