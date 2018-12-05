package com.jl.test.mode.kafka.scala

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Minutes, StreamingContext}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.scalatest.time.Seconds

object KafkaWordCount {

    def main(args: Array[String]): Unit = {
        // Zookeper地址
        val zkQuorum = "localhost:2181"
        val group = "stream"
        val numThreads = "3"
        val topics = "test1"
        val sparkConf = new SparkConf().setAppName("KafkaWordCount").setMaster("local[2]")
        // 创建spark StreamContext
        val ssc = new StreamingContext(sparkConf, Seconds(2))
        // WALCheckpoint HDFS中存储容错点的地址
        ssc.checkpoint("WALCheckpoint")
        val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap
        // 根据kafka创建stream，然后获取传输过来的内容
        val lines = KafkaUtils.createStream(ssc, zkQuorum, group, topicMap).map(_._2)
        val words = lines.flatMap(_.split(" "))
        val wordCounts = words.map(x => (x, 1L)).reduceByKeyAndWindow(_+_, _-_,Minutes(10), Seconds(2), 2)
        wordCounts.print()
        ssc.start()
        ssc.awaitTermination()
    }
}
