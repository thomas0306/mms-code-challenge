package com.mms.oms.config.kafka

import org.apache.kafka.clients.consumer.ConsumerRecord

interface KafkaProcessor<K, V> {
    suspend fun process(record: ConsumerRecord<K, V>)
}
