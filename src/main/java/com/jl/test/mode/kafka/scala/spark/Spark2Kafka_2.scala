package com.jl.test.mode.kafka.scala.spark

import java.util.Properties

import org.apache.kafka.clients.producer.{ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.{SparkConf, SparkContext}

import scala.io.StdIn

/**
  * 升级第一版，将KafkaProducer序列化，并懒加载后，广播到每个分区中，用的时候即取
  */
object Spark2Kafka_2 {

    def main(args: Array[String]): Unit = {
        val topic: String = "test12"
        try {
            val input = StdIn.readLine()
            if (input.length > 0 && input.indexOf(",") != -1) {
                val conf = new SparkConf().setAppName("SPARK_KAFKA_2").setMaster("local[2]")
                val sc = new SparkContext(conf)
                val inputRDD = sc.parallelize(input.split(","))
                // 广播kafkaProducer
                val kafkaProducer: Broadcast[KafkaSink[String, String]] = {
                    val kafkaProducerConfig = {
                        val p = new Properties()
                        p.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
                        p.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
                        p.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)

                        p
                    }

                    sc.broadcast(KafkaSink[String, String](kafkaProducerConfig))
                }

                inputRDD.foreachPartition(rdd =>
                    rdd.foreach{
                        case x: String => kafkaProducer.value.send(topic, x)
                    }
                )
            }
        } catch {
            case e: Exception => e.printStackTrace()
        }
    }
}
