
### 生产者数据分发策略

1 每个topic数据的分发策略在生产者端.

2 Kafka在数据生产的时候，有一个数据分发策略。默认的情况使用DefaultPartitioner.class类。
  这个类中就定义数据分发的策略。
 
    1) 如果用户指定了partition,生产端就不会调用DefaultPartitioner.partition()方法,partition返回分区编号.
        ? 怎么指定分区数量?
    2) 当用户指定key，使用hash算法。如果key一直不变，同一个key算出来的hash值是个固定值。如果是固定值，这种hash取模就没有意义。
    
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
  
   哈希算法:  ``` return Utils.toPositive(Utils.murmur2(keyBytes)) % numPartitions; ```
    
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
