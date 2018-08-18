
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
    
