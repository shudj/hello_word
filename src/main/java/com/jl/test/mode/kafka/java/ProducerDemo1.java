package com.jl.test.mode.kafka.java;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.Future;

/**
 * Author: shudj
 * Time: 2018/12/3 14:35
 * Description:
 *
 */
public class ProducerDemo1 {

    public static void main(String[] args) {
        Properties producerProps = new Properties();
        // kafka brokers地址列表信息，推荐至少写两个，用““”“”","隔开”
        producerProps.put("bootstrap.servers", "localhost:9092");
        // 序列化
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        /*
         * 0:生成者不会等待服务器的任何确认信号
         * 1:一旦leader broker将信息写入本地日志，生成者就会接收到确认信号
         * all:当leader broker已经接收到所有副本成功的确认信号时，生产者才会收到确认信号
         **/
        producerProps.put("acks","all");
        // 如果消息发送失败，表示生成者在抛出异常之前重试发送消息的次数
        producerProps.put("retries", 1);
        // 允许生成根据配置的大小对消息进行批量处理
        producerProps.put("batch.size", 20000);
        // 在向broker发送当前批处理之前，生成者应该尽可能等待更多的消息，配置的时间长短单位毫秒
        producerProps.put("linger.ms", 1);
        // 生产者可以用来缓冲等待发送到Kafka服务器的消息的内存大小
        producerProps.put("buffer.memory", 24568545);

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(producerProps);

        for (int i = 0; i < 200; i++) {
            /*
             * 如果指定了分区数，那么在发送记录时将使用指定的分区
             * 如果指定了一个key，则按照key的hash值选一个分区
             * 如果两个都没有指定，则将以round-robin方式分区
             **/
            ProducerRecord data = new ProducerRecord<String, String>("test", "Hello this is record" + i);
            Future<RecordMetadata> recordMetadata = producer.send(data);
        }

        producer.close();
    }
}
