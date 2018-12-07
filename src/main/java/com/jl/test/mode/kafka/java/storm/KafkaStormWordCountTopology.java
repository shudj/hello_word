package com.jl.test.mode.kafka.java.storm;

import org.apache.spark.storage.TopologyMapper;
import org.apache.storm.kafka.BrokerHosts;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.ZkHosts;

/**
 * Author: shudj
 * Time: 2018/12/6 14:56
 * Description:
 */
public class KafkaStormWordCountTopology {
    public static void main(String[] args) {
        String zkConnString = "localhost:9092";
        String topic = "words";
        BrokerHosts hosts = new ZkHosts(zkConnString);
        SpoutConfig kafkaSpountConfig = new SpoutConfig(hosts, topic, "/" + topic, "wordcountID");
        kafkaSpountConfig.startOffsetTime = kafka.api.OffsetRequest.EarliestTime();

    }
}
