package com.jl.test.mode.kafka.java;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import scala.Tuple2;
import scala.actors.threadpool.Arrays;

import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * Author: shudj
 * Time: 2018/12/5 10:33
 * Description:
 */
public class KafkaWordCount {
    private static final Pattern WORD_DELIMETER = Pattern.compile(" ");

    public static void main(String[] args) throws InterruptedException {
        // Zookeper地址
        String zkQuorum = "localhost:2181";
        String groupName = "stream";
        int numThreads = 3;
        String topicsName = "test1";
        SparkConf sparkConf = new SparkConf().setAppName("WordCountKafkaStream").setMaster("local[2]");
        JavaStreamingContext javaStreamingContext = new JavaStreamingContext(sparkConf, new Duration(20000));
        HashMap<String, Integer> topicTobeUsedBySpark = new HashMap<>();
        String[] topics = topicsName.split(",");
        for (String topic : topics) {
            topicTobeUsedBySpark.put(topic, numThreads);
        }
        JavaPairReceiverInputDStream<String, String> streamMessages =
                KafkaUtils.createStream(javaStreamingContext, zkQuorum, groupName, topicTobeUsedBySpark);

        // JavaDStream<String> lines = streamMessages.map(Tuple2::_2);
        JavaDStream<String> lines = streamMessages.map(new Function<Tuple2<String, String>, String>() {
            @Override
            public String call(Tuple2<String, String> tuple2) throws Exception {
                return tuple2._2();
            }
        });

        // lines.flatMap(x -> Arrays.asList(WORD_DELIMETER.split(x)).iterator());
        JavaDStream<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) throws Exception {
                return Arrays.asList(WORD_DELIMETER.split(s)).iterator();
            }
        });

        // words.mapToPair(x -> new Tuple2<>(x, 1)).reduceByKey((i, j) -> i +j);
        JavaPairDStream<String, Integer> wordCounts = words.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) throws Exception {
                return new Tuple2<>(s, 1);
            }
        }).reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) throws Exception {
                return integer + integer2;
            }
        });

        wordCounts.print();
        javaStreamingContext.start();
        javaStreamingContext.awaitTermination();
    }
}
