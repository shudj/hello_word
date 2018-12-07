package com.jl.test.mode.kafka.scala.spark

import java.util

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.io.StdIn

/**
  * 需要每个分区创建一个生产者
  * 注意这里我们并不能将KafkaProducer的新建任务放在foreachPartition外边，
  * 因为KafkaProducer是不可序列化的（not serializable）
  */
object Spark2kafka_1 {

    def main(args: Array[String]): Unit = {
        val topic:String = "test"
        try {
            // 从控制台读取信息
            val input: String = StdIn.readLine()
            val conf: SparkConf = new SparkConf().setAppName("SPARK_KAFKA").setMaster("local[2]")
            val sc = new SparkContext(conf)
            if (null != input && input.length > 0 && input.indexOf(",") > 0) {
                val inputRDD: RDD[String] = sc.parallelize(input.split(","))
                inputRDD.foreachPartition(rdd =>
                    rdd.foreach{
                        case x:String => {
                            // 配置produce的配置信息
                            val props = new util.HashMap[String, Object]()
                            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
                            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer")
                            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer")

                            // 创建生产者
                            val producer = new KafkaProducer[String, String](props)

                            val record = new ProducerRecord[String, String](topic, x)
                            producer.send(record)
                        }
                    }
                )
            }
        } catch {
            case e:Exception => e.printStackTrace()
        }
    }
}
