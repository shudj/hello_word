package com.jl.test.mode.kafka.java.kafakStream;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;

import java.util.Arrays;
import java.util.Properties;

/**
 * Author: shudj
 * Time: 2018/12/7 10:17
 * Description:
 */
public class KafkaStreamWordCount {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-Stream-WordCount");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        props.put(StreamsConfig.ZOOKEEPER_CONNECT_CONFIG, "localhost:2181");
        props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        Serde<String> stringSerdes = Serdes.String();
        Serde<Long> longSerde = Serdes.Long();

        KStreamBuilder kStreamBuilder = new KStreamBuilder();
        // 从topic为input中读取数据
        KStream<String, String> topicRecords = kStreamBuilder.stream(stringSerdes, stringSerdes, "input");
        KStream<String, Long> wordCounts = topicRecords.flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\W+")))
                .map((key, word) -> new KeyValue<>(word, word))
                .countByKey("Count")
                .toStream();
        // 存储wordcount结果到topic为wordcount里面
        wordCounts.to(stringSerdes, longSerde, "wordCount");
        KafkaStreams kafkaStreams = new KafkaStreams(kStreamBuilder, props);

        kafkaStreams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }
}
