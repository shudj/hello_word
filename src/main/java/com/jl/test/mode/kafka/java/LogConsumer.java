package com.jl.test.mode.kafka.java;

import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogConsumer {

    private ConsumerConfig config;
    private String topic;
    private int partitionNum;
    private MessageExecutor excutor;
    private ConsumerConnector connector;
    private ExceutorService threadPool;

    public LogConsumer(String topic, int partitionsNum, MessageExecutor executor) {
        Properties props = new Properties();
        props.load(ClassLoader.getSystemResourceAsStream("consumer.properties"));
        config = new ConsumerConfig(props);
        this.topic = topic;
        this.partitionNum = partitionsNum;
        this.excutor = executor;
    }

    public void start() throws Exception{
        connector = Consumer.createJavaComsumerConnector(config);
        Map<String, Integer> topics = new HashMap<String, Integer>();
        topics.put(topic, partitionNum);
        Map<String, List<KafkaStream<byte[], byte[]>>> streams = connector.createMessageStreams(topics);
        List<KafkaStream<byte[], byte[]>> partitions = streams.get(topics);
        threadPool = (ExecutorService) Executors.newFixedThreadPool(partitionNum);
        for (KafkaStream<byte[], byte[]> partition : partitions) {
            threadPool).execute(new MessageRunner(partition);
        }
    }

    public void close() {
        try {
            threadPool.shutdownNow();
        } catch (Exception e) {

        } finally {
            connector.shutdown();
        }
    }

    class MessageRunner implements Runnable {
        private  KafkaStream<byte[], byte[]> partition;
        MessageRunner(KafkaStream<byte[], byte[]> partition) {
            this.partition = partition;
        }
        @Override
        public void run() {
            ConsumerIterator<byte[], byte[]> it = partition.iterator();
            while (it.hasNext()) {
                MessageAndMetadata<byte[], byte[]> item = it.next();
                System.out.println("Partition: " + item.partition());
                System.out.println("offset: " + item.offset());
                excutor.execute(new String(item.message()));
            }
        }
    }

    interface MessageExecutor {
        public void execute(String message);
    }

    public static void main(String[] args) {
        LogConsumer consumer = null;
        try {
            MessageExcutor excutor = new MessageExecutor() {
                public void execute(String message) {
                    System.out.println(message);
                }
            };
            consumer = new LogConsumer("test-topic", 2, excutor);
            consumer.start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != consumer) {
                consumer.close();
            }
        }
    }
}
