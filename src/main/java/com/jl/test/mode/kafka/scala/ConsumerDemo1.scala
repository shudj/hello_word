package com.jl.test.mode.kafka.scala

import java.util
import java.util.Properties

import org.apache.kafka.clients.consumer.{ConsumerRecord, KafkaConsumer, OffsetAndMetadata, OffsetCommitCallback}
import org.apache.kafka.common.TopicPartition
import org.apache.log4j.Logger

object ConsumerDemo1 {
    def main(args: Array[String]): Unit = {
        val log = Logger.getLogger(ConsumerDemo1.getClass)
        val topic = "topicForTest"
        val topicList = new util.ArrayList[String]()
        topicList.add(topic)

        val consumerPros = new Properties()
        consumerPros.put("bootstrap.servers", "localhost:9092")
        consumerPros.put("group.id", "Demo")
        consumerPros.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        consumerPros.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        consumerPros.put("enable.auto.commit", "true")
        consumerPros.put("auto.commit.interval.ms", "1000")
        consumerPros.put("session.timeout.ms", "30000")
        val consumer = new KafkaConsumer[String, String](consumerPros)
        consumer.subscribe(topicList)
        log.info("Subscribed to topic" + topic)

        val i: Int = 0
        try {
            while (true) {
                val records = consumer.poll(2)
                for (record : ConsumerRecord[String, String] <- records) {
                    log.info("offset = " + record.offset() + " key = " + record.key() + " value = " + record.value())
                }

                consumer.commitAsync(new OffsetCommitCallback {
                    override def onComplete(map: util.Map[TopicPartition, OffsetAndMetadata], e: Exception): Unit = {}
                })

            }
        } catch {
            case e: Exception => {
                // TODO: Log Exception here
            }
        } finally {
            try {
                consumer.commitAsync()
            } finally {
                consumer.close()
            }
        }
    }
}
