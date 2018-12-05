package com.jl.test.mode.kafka.java.ip;

import kafka.serializer.StringDecoder;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
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
        PropertyReader propertyReader = new PropertyReader();
        CacheIpLookup cacheIpLookup = new CacheIpLookup();
        SparkConf conf = new SparkConf().setAppName("IP_FFAUD");
        JavaStreamingContext javaStreamingContext = new JavaStreamingContext(conf, Durations.seconds(3));
        Set<String> topicSet = new HashSet<>(Arrays.asList(propertyReader.getPropertyValue(PropertyReader.TOPIC).split(",")));
        Map<String, String> kafkaConfiguration = new HashMap<>();
        kafkaConfiguration.put("metadata.broker.list", propertyReader.getPropertyValue(PropertyReader.BROKER_LIST));
        kafkaConfiguration.put("group.id", propertyReader.getPropertyValue(PropertyReader.GROUP_ID));

        JavaPairInputDStream<String, String> messages =
                KafkaUtils.createDirectStream(
                        javaStreamingContext,
                        String.class,
                        String.class,
                        StringDecoder.class,
                        StringDecoder.class,
                        kafkaConfiguration,
                        topicSet);
        JavaDStream<String> ipRecords = messages.map(Tuple2::_2);
        JavaDStream<String> fraudIPs = ipRecords.filter(s -> {
            String IP = s.split(",")[0];
            String[] ranges = IP.split("\\.");

            return cacheIpLookup.isFraudIP(ranges[0]);
        });
        fraudIPs.dstream().saveAsTextFiles("","");
        javaStreamingContext.start();
        javaStreamingContext.awaitTermination();
    }
}
