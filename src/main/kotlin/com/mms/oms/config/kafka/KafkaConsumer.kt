package com.mms.oms.config.kafka

import com.mms.oms.config.retry.Retry.withRetry
import io.ktor.application.ApplicationEnvironment
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.errors.WakeupException
import org.slf4j.LoggerFactory
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.Properties
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class Consumer<K, V>(
    private val consumer: KafkaConsumer<K, V>,
    topic: String,
    private val processor: KafkaProcessor<K, V>,
    parallelism: Int = 1
) : ClosableJob {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val closed: AtomicBoolean = AtomicBoolean(false)
    private val threadIndex: AtomicInteger = AtomicInteger(1)
    private var finished = CountDownLatch(1)
    private val dispatcher: CoroutineDispatcher

    init {
        consumer.subscribe(listOf(topic))
        dispatcher = Executors
            .newFixedThreadPool(
                parallelism
            ) { r ->
                Thread(r, "$topic-processor-${threadIndex.getAndIncrement()}")
            }
            .asCoroutineDispatcher()
    }

    override fun run() = try {
        while (!closed.get()) {
            val records = consumer.poll(Duration.of(1000, ChronoUnit.MILLIS))

            runBlocking(dispatcher) {
                records.map {
                    launch {
                        logger.debug(
                            "topic [${it.topic()}], " +
                                "partition [${it.partition()}], " +
                                "offset [${it.offset()}], " +
                                "key [${it.key()}], " +
                                "value [${it.value()}]"
                        )
                        processor.run {
                            withRetry(recover = { recover(it) }) { process(it) }
                        }
                    }
                }.joinAll()
            }

            if (!records.isEmpty) {
                consumer.commitAsync { offsets, exception ->
                    if (exception != null) {
                        logger.error("Commit failed for offsets [$offsets]", exception)
                    } else {
                        logger.trace("Offset committed [$offsets]")
                    }
                }
            }
        }
        logger.info("Finish consuming")
    } catch (e: Throwable) {
        when (e) {
            is WakeupException -> logger.trace("Consumer waked up")
            else -> logger.error("Polling failed", e)
        }
    } finally {
        logger.trace("Commit offset synchronously")
        consumer.commitSync()
        consumer.close()
        finished.countDown()
        logger.trace("Consumer successfully closed")
    }

    override fun close() {
        logger.trace("Close job...")
        closed.set(true)
        consumer.wakeup()
        finished.await(3000, TimeUnit.MILLISECONDS)
        logger.trace("Job is successfully closed")
    }
}

fun <K, V> buildConsumer(
    environment: ApplicationEnvironment,
    topic: String,
    processor: KafkaProcessor<K, V>,
    parallelism: Int
): Consumer<K, V> {
    val consumerConfig = environment.config.config("kafka")
    val consumerProps = Properties().apply {
        this[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = consumerConfig.property("bootstrap.servers").getList()
        this[ConsumerConfig.CLIENT_ID_CONFIG] = "${consumerConfig.property("client.id").getString()}-$topic-client"
        this[ConsumerConfig.GROUP_ID_CONFIG] = consumerConfig.property("group.id").getString()
        this[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = consumerConfig.property("key.deserializer").getString()
        this[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = consumerConfig.property("value.deserializer").getString()
    }
    return Consumer(KafkaConsumer(consumerProps), topic, processor, parallelism)
}
