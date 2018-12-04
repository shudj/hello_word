package com.jl.test.mode.kafka.java;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

/**
 * Author: shudj
 * Time: 2018/12/4 17:13
 * Description:
 */
public class ConsumerDemo1 {
    public static void main(String[] args) {
        Logger log = Logger.getLogger(ConsumerDemo1.class);
        String topic = "topicForTest";
        ArrayList<String> topicList = new ArrayList<>();
        topicList.add(topic);

        Properties consumerPros = new Properties();
        consumerPros.put("bootstrap.servers", "localhost:9092");
        consumerPros.put("group.id", "Demo");
        consumerPros.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerPros.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerPros.put("enable.auto.commit", "true");
        consumerPros.put("auto.commit.interval.ms", "1000");
        consumerPros.put("session.timeout.ms", "30000");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(consumerPros);
        consumer.subscribe(topicList);
        log.info("Subscribed to topic" + topic);

        int i = 0;
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(500);
                for (ConsumerRecord<String, String> record: records) {
                    log.error("offset = " + record.offset() + "key = " + record.key() + "value = " + record.value());
                }
                consumer.commitAsync(new OffsetCommitCallback() {
                    @Override
                    public void onComplete(Map<TopicPartition, OffsetAndMetadata> map, Exception e) {

                    }
                });
            }
        } finally {
            try {
                consumer.commitAsync();
            } finally {
                consumer.close();
            }
        }
    }
}
