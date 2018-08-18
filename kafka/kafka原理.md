
## Kafka 名词解释

 * Producer ：消息生产者，就是向 kafka broker 发消息的客户端。
 
 * Consumer ：消息消费者，向 kafka broker 取消息的客户端
 
 * Topic ：主题。
 
 * Consumer Group （CG）：这是 kafka 用来实现一个 topic 消息的广播（发给所有的 consumer）
    和单播（发给任意一个 consumer）的手段。一个 topic 可以有多个 CG。topic 的消息会复制（不
    是真的复制，是概念上的）到所有的 CG，但每个 partion 只会把消息发给该 CG 中的一个
    consumer。如果需要实现广播，只要每个 consumer 有一个独立的 CG 就可以了。要实现单播只
    要所有的 consumer 在同一个 CG。用 CG 还可以将 consumer 进行自由的分组而不需要多次发送
    消息到不同的 topic。
    
 * Broker ：一台 kafka 服务器就是一个 broker。一个集群由多个 broker 组成。一个 broker 可以容
    纳多个 topic。
    
 * Partition：为了实现扩展性，一个非常大的 topic 可以分布到多个 broker（即服务器）上，一个
    topic 可以分为多个 partition，每个 partition 是一个有序的队列。partition 中的每条消息都会被分
    配一个有序的 id（offset）。kafka 只保证按一个 partition 中的顺序将消息发给 consumer，不保
    证一个 topic 的整体（多个 partition 间）的顺序。
    
 * Offset：kafka 的存储文件都是按照 offset.kafka 来命名，用 offset 做名字的好处是方便查找。例
    如你想找位于 2049 的位置，只要找到 2048.kafka 的文件即可。当然 the first offset 就是
    00000000000.kafka
    
 * Replication：Kafka 支持以 Partition 为单位对 Message 进行冗余备份，每个 Partition 都可以配
    置至少 1 个 Replication(当仅 1 个 Replication 时即仅该 Partition 本身)。
    
 * Leader：每个 Replication 集合中的 Partition 都会选出一个唯一的 Leader，所有的读写请求都由
    Leader 处理。其他 Replicas 从 Leader 处把数据更新同步到本地，过程类似大家熟悉的 MySQL
    中的 Binlog 同步。每个 Cluster 当中会选举出一个 Broker 来担任 Controller，负责处理 Partition
    的 Leader 选举，协调 Partition 迁移等工作。
    
  * ISR(In-Sync Replica)：是 Replicas 的一个子集，表示目前 Alive 且与 Leader 能够“Catch-up”的
      Replicas 集合。 由于读写都是首先落到 Leader  上，所以一般来说通过同步机制从 Leader 上拉取
      数据的 Replica 都会和 Leader 有一些延迟(包括了延迟时间和延迟条数两个维度)，任意一个超过
      阈值都会把该 Replica 踢出 ISR。每个 Partition 都有它自己独立的 ISR。
### 生产者数据分发策略

1 每个topic数据的分发策略在生产者端.

2 Kafka在数据生产的时候，有一个数据分发策略。默认的情况使用DefaultPartitioner.class类。
  这个类中就定义数据分发的策略。
 
    1) 如果用户指定了partition,生产端就不会调用DefaultPartitioner.partition()方法,partition返回分区编号.
        ? 怎么指定分区数量?
        
    2) 当用户指定key，使用hash算法。如果key一直不变，同一个key算出来的hash值是个固定值。如果是固定值，这种hash取模就没有意义。
        我们要保证key的值是一直变动的,比如可以是username,则相同的用户会被分到同一个分区中.
        
    ``` java
      /**
     * Create a record to be sent to Kafka
     * 
     * @param topic The topic the record will be appended to
     * @param key The key that will be included in the record
     * @param value The record contents
     */
    public ProducerRecord(String topic, K key, V value) {
        this(topic, null, null, key, value, null);
    }
  ```
      DefaultPartitioner.partition()中的哈希算法:
```  java
  return Utils.toPositive(Utils.murmur2(keyBytes)) % numPartitions; 
```
    
    3）当用既没有指定partition也没有key,会使用默认的分发策略,即轮询分发,每个partiton依次分发.
``` java
      /**
     * Create a record with no key
     * 
     * @param topic The topic this record should be sent to
     * @param value The record contents
     */
    public ProducerRecord(String topic, V value) {
        this(topic, null, null, null, value, null);
    }
```
    4) partition和key同时存在,按照partition分发数据.
    
 ### 消费者的负载均衡机制
 
  * 问题 生产者的速度很快,但是消费者的消费速度跟不上,造成数据大量滞后和延时.
  * 解决 多几个消费者共同消费.
  
    多个消费者会组成一个**消费组**,消费组中的成员 共同消费同一份数据.<br>
    当消费者数量和分区数量相等时,如果消费速度还是跟不上生产速度,则此时在增加消费时是没有作用的.<br>
    因为Kafka的负载均衡策略规定,比分区数量多出来的消费者处于空闲状态,一个消费者只能消费一个partition中的数据.
    
    实际解决方案:<br>
      1 修改topic的partition数量<br>
      2 减少消费者的处理时间,提高处理速度.
                 
 
 ### 消息不丢失机制
    
