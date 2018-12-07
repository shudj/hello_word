package com.jl.test.mode.kafka.scala.spark

import java.util
import java.util.Properties
import java.util.concurrent.Future

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}

/**
  * 封装KafkaProducer实现懒加载，并且将其序列化
  * @param createProducer
  * @tparam K
  * @tparam V
  */
class KafkaSink[K, V](createProducer:() => KafkaProducer[K, V]) extends Serializable {

    lazy val producer = createProducer()
    def send(topic : String, key : K, value : V):util.concurrent.Future[RecordMetadata] =
        producer.send(new ProducerRecord[K, V](topic, key, value))

    def send(topic : String, value : V):Future[RecordMetadata] =
        producer.send(new ProducerRecord[K, V](topic, value))
}

object KafkaSink {
    import scala.collection.JavaConversions._
    def apply[K, V](config: util.Map[String, Object]): KafkaSink[K, V] = {
        val createProducerFunc = () => {
            val producer = new KafkaProducer[K, V](config)
            // 确保JVM关闭的时候，
            sys.addShutdownHook(producer.close())
            producer
        }
        new KafkaSink[K, V](createProducerFunc)
    }

    def apply[K, V](config: Properties): KafkaSink[K, V] = apply(config.toMap)
}
