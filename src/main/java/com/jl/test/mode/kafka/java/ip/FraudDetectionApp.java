package com.jl.test.mode.kafka.java.ip;

import com.jl.test.mode.producer2consumer.Consumer;
import kafka.serializer.StringDecoder;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import scala.Tuple2;
import scala.math.Ordering;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Author: shudj
 * Time: 2018/12/5 14:05
 * Description:
 */
public class FraudDetectionApp {
    private static final Pattern SAPCE = Pattern.compile(" ");

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("hadoop.home.dir", "D:\\work\\hadoop-2.8.2");
        PropertyReader propertyReader = new PropertyReader();
        CacheIpLookup cacheIpLookup = new CacheIpLookup();
        SparkConf conf = new SparkConf().setAppName("IP_FFAUD").setMaster("local[2]");
        JavaStreamingContext javaStreamingContext = new JavaStreamingContext(conf, Durations.seconds(6));
        Set<String> topicSet = new HashSet<>(Arrays.asList(propertyReader.getPropertyValue(PropertyReader.TOPIC).split(",")));
        Map<String, Object> kafkaConfiguration = new HashMap<>();
        kafkaConfiguration.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, propertyReader.getPropertyValue(PropertyReader.BROKER_LIST));
        kafkaConfiguration.put(ConsumerConfig.GROUP_ID_CONFIG, propertyReader.getPropertyValue(PropertyReader.GROUP_ID));
        kafkaConfiguration.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaConfiguration.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        JavaInputDStream<ConsumerRecord<Object, Object>> messages = KafkaUtils.createDirectStream(
                javaStreamingContext,
                LocationStrategies.PreferBrokers(),
                ConsumerStrategies.Subscribe((Collection<String>) topicSet, kafkaConfiguration));

        JavaDStream<String> ipRecords = messages.map(new Function<ConsumerRecord<Object, Object>, String>() {
            @Override
            public String call(ConsumerRecord<Object, Object> s) throws Exception {
                return String.valueOf(s.value());
            }
        });
        JavaDStream<String> fraudIPs = ipRecords.filter(s -> {
            String IP = s.split(",")[0];
            String[] ranges = IP.split("\\.");
            System.out.println(ranges[0] + "\t" + s.split(",")[1]);
            return cacheIpLookup.isFraudIP(ranges[0]);
        });
        fraudIPs.print();
        javaStreamingContext.start();
        javaStreamingContext.awaitTermination();
    }
}
