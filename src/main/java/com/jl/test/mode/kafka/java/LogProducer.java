package com.jl.test.mode.kafka.java;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class LogProducer {
    private KafkaProducer<String, String> inner;

    public LogProducer() throws Exception {
        Properties props = new Properties();
        props.load(ClassLoader.getSystemResourceAsStream("producer.properties"));
        inner = new KafkaProducer<String, String>(props);
    }

    public void send(String topicName, String message) {
        if (null == topicName || null == message) {
            return;
        }

        ProducerRecord<String, String> km = new ProducerRecord<>(topicName, message);
        inner.send(km);
    }

    public void send(String topicName, Collection<String> messages) {
        if (null == topicName || null == messages) {
            return;
        }
        if (messages.isEmpty()) {
            return;
        }

        List<ProducerRecord<String, String>> kms = new ArrayList<ProducerRecord<String, String>>();
        for (String entry : messages) {
            ProducerRecord<String, String> km = new ProducerRecord<>(topicName, entry);
            inner.send(km);
        }

    }

    public void close() {
        inner.close();
    }

    public static void main(String[] args) {
        LogProducer producer = null;
        try {
            producer = new LogProducer();
            int i = 0;
            while (true) {
                producer.send("test-topics", "this is a sample" + i);
                i++;
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != producer) {
                producer.close();
            }
        }
    }
}
