## API 字符串测试

#### producer

``` java 
public class ProducerDemo {

    public static void main(String[] args){
        Properties properties = new Properties();

        //设置kafka集群地址,可以写多个集群地址: "hadoop-node-1:9092,hadoop-node-2:9092"
        properties.put("bootstrap.servers", "hadoop-node-1:9092,hadoop-node-2:9092;hadoop-node-3:9092");

        //设置数据可靠性的级别 -1 0 1  all = -1
        properties.put("acks", "all");

        //发送失败尝试重发次数
        properties.put("retries", 0);

        //每个分区未发送消息总字节大小（单位：字节），超过设置的值就会提交数据到服务端
        properties.put("batch.size", 16384);

        //消息在缓冲区保留的时间，超过设置的值就会被提交到服务端
        properties.put("linger.ms", 1);

        //整个Producer用到总内存的大小，如果缓冲区满了会提交数据到服务端
        //buffer.memory要大于batch.size，否则会报申请内存不足的错误
        properties.put("buffer.memory", 33554432);

        //序列化器
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // 创建生产者对象.
        Producer<String, String> producer = null;

        try {
            producer = new KafkaProducer<String, String>(properties);

            for (int i = 0; i < 1000; i++) {
                String msg = "Message " + i;
                // 参数: 主题,发送的消息.
                producer.send(new ProducerRecord<String, String>("string_demo", msg));
                System.out.println("Sent:" + msg);
                Thread.sleep(500);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            producer.close();
        }

    }

}
```

####  Consumer 

``` java
public class ConsumerDemo {
    public static void main(String[] args) {
        // 1\连接集群
        Properties props = new Properties();

        // 设置kafka集群地址
        props.put("bootstrap.servers", "hadoop-node-1:9092,hadoop-node-2:9092;hadoop-node-3:9092");

        ////设置消费者组，组名字自定义，组名字相同的消费者在一个组
        props.put("group.id", "stringTest");

        //开启offset自动提交
        props.put("enable.auto.commit", "true");

        // 自动提交间隔
        props.put("auto.commit.interval.ms", "1000");

        props.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");

        //实例化一个消费者
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(props);

        //消费者订阅主题，可以订阅多个主题
        kafkaConsumer.subscribe(Arrays.asList("string_demo"));
        //死循环不停的从broker中拿数据
        while (true) {
            //Records 记录 poll 轮询
            ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(100);

            for (ConsumerRecord<String, String> record : consumerRecords) {
                System.out.println(" 偏移量: "+record.offset()
                                    +" record.key: "+record.key()
                                    +" 消费的信息:"+record.value()
                                    +" 分区:"+record.topic()
                                    +" record.toString:"+record.toString()
                                    +" 分区:"+record.partition()
                );
            }
        }
    }

}
```

* 输出:

> 偏移量: 1720 record.key: null 消费的信息:主题one557 分区:string_demo record.toString:ConsumerRecord(topic = string_demo, partition = 0, offset = 1720, CreateTime = 1534846818027, serialized key size = -1, serialized value size = 12, headers = RecordHeaders(headers = [], isReadOnly = false), key = null, value = 主题one557) 分区:0
 偏移量: 1721 record.key: null 消费的信息:主题one558 分区:string_demo record.toString:ConsumerRecord(topic = string_demo, partition = 0, offset = 1721, CreateTime = 1534846818530, serialized key size = -1, serialized value size = 12, headers = RecordHeaders(headers = [], isReadOnly = false), key = null, value = 主题one558) 分区:0
 偏移量: 1722 record.key: null 消费的信息:主题one559 分区:string_demo record.toString:ConsumerRecord(topic = string_demo, partition = 0, offset = 1722, CreateTime = 1534846819030, serialized key size = -1, serialized value size = 12, headers = RecordHeaders(headers = [], isReadOnly = false), key = null, value = 主题one559) 分区:0
 偏移量: 1723 record.key: null 消费的信息:主题one560 分区:string_demo record.toString:ConsumerRecord(topic = string_demo, partition = 0, offset = 1723, CreateTime = 1534846819530, serialized key size = -1, serialized value size = 12, headers = RecordHeaders(headers = [], isReadOnly = false), key = null, value = 主题one560) 分区:0
 偏移量: 1724 record.key: null 消费的信息:主题one561 分区:string_demo record.toString:ConsumerRecord(topic = string_demo, partition = 0, offset = 1724, CreateTime = 1534846820031, serialized key size = -1, serialized value size = 12, headers = RecordHeaders(headers = [], isReadOnly = false), key = null, value = 主题one561) 分区:0

