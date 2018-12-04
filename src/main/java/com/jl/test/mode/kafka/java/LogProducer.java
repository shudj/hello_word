package com.jl.test.mode.kafka.java;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class LogProducer {
    private Producer<String, String> inner;
    public LogProducer() throws Exception {
        Properties props = new Properties();
        props.load(ClassLoader.getSystemResourceAsStream("producer.properties"));
        ProducerConfig config = new ProducerConfig(props);
        inner = new Producer<String, String>(config);
    }

    public void send(String topicName, String message) {
        if (null == topicName || null == message) {
            return;
        }

        KeyedMessage<String, String> km = new KeyedMessage<>(topicName, message);
        inner.send(km);
    }

    public void send(String topicName, Collection<String> messages) {
        if (null == topicName || null == messages) {
            return;
        }
        if (messages.isEmpty()) {
            return;
        }

        List<KeyedMessage<String, String>> kms = new ArrayList<KeyedMessage<String, String>>();
        for (String entry : messages) {
            KeyedMessage<String, String> km = new KeyedMessage<>(topicName, message);
            kms.add(km);
        }

        inner.send(kms);
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
