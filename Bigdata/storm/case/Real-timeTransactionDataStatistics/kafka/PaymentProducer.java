package cn.itcast.realtime.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * 负责生产支付信息
 */
public class PaymentProducer {
    public static void main(String[] args) throws InterruptedException {
        /* 1、连接集群，通过配置文件的方式
         * 2、发送数据-topic:order，value
         */
        Properties props = new Properties();
        props.put("bootstrap.servers", "hadoop-node-1:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(props);

        for (int i = 0; i < 100000; i++) {
            // 发送数据
            ProducerRecord<String, String> value = new ProducerRecord<String, String>("payment", new Payment().random());
            kafkaProducer.send(value);
            Thread.sleep(1000);
            /**
             * 如果ProducerRecord中制定了数据发送那个partition，就用这个编号
             * 平常一般不指定partition
             */
        }
    }
}
