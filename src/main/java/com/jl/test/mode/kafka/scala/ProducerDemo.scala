import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object ProducerDemo extends App {

    override def main(args: Array[String]): Unit = {
        val producerProps = new Properties()
        // kafka brokers地址列表信息，推荐至少写两个，用““”“”","隔开”
        producerProps.put("bootstrap.servers", "localhost:9092")
        // 序列化
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        /*
         * 0:生成者不会等待服务器的任何确认信号
         * 1:一旦leader broker将信息写入本地日志，生成者就会接收到确认信号
         * all:当leader broker已经接收到所有副本成功的确认信号时，生产者才会收到确认信号
         **/
        producerProps.put("acks", "all")
        // 如果消息发送失败，表示生成者在抛出异常之前重试发送消息的次数
        producerProps.put("retries", 1)
        // 允许生成根据配置的大小对消息进行批量处理
        producerProps.put("batch.size", 20000)
        // 在向broker发送当前批处理之前，生成者应该尽可能等待更多的消息，配置的时间长短单位毫秒
        producerProps.put("linger.ms", 1)
        // 生产者可以用来缓冲等待发送到Kafka服务器的消息的内存大小
        producerProps.put("buffer.memory", 24568545)

        val producer = new KafkaProducer[String, String](producerProps)
        for (a <- 1 to 2000) {
            val record = new ProducerRecord[String, String]("test2", "hello thi is record" + a);
            producer.send(record)
        }
        producer.close();
    }
}