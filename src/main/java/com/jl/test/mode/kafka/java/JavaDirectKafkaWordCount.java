package com.jl.test.mode.kafka.java;

import kafka.serializer.StringDecoder;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import scala.Tuple2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * Author: shudj
 * Time: 2018/12/5 11:27
 * Description:
 */
public class JavaDirectKafkaWordCount {
    private static final Pattern SPACE = Pattern.compile(" ");

    public static void main(String[] args) throws InterruptedException {
        String brokers = "localhost:9092";
        String topics = "test1";
        SparkConf sparkConf = new SparkConf().setAppName("DirectKafkaWordCount").setMaster("local[2]");
        JavaStreamingContext javaStreamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(2));
        HashSet<String> topicSet = new HashSet<>(Arrays.asList(topics.split(",")));
        HashMap<String, String> kafkaConfiguration = new HashMap<>();
        kafkaConfiguration.put("metadata.broker.list", brokers);
        JavaPairInputDStream<String, String> messages =
                KafkaUtils.createDirectStream(javaStreamingContext,
                                String.class,
                                String.class,
                                StringDecoder.class,
                                StringDecoder.class,
                                kafkaConfiguration,
                                topicSet);
        JavaDStream<String> lines = messages.map(Tuple2::_2);
        JavaDStream<String> words = lines.flatMap(x -> Arrays.asList(SPACE.split(x)).iterator());
        JavaPairDStream<String, Integer> wordCounts = words.mapToPair(s -> new Tuple2<String, Integer>(s, 1)).reduceByKey((i, j) -> i + j);
        wordCounts.print();
        javaStreamingContext.start();
        javaStreamingContext.awaitTermination();
    }
}
