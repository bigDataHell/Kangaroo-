
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
      
      
      ? kafka的客户端和服务端分别是什么?
      

## 消费模型

消息由生产者发送到 Kafka 集群后，会被消费者消费。一般来说我们的消费模型有两种：
 * 推送模型(Push)
 * 拉取模型(Pull)

基于推送模型的消息系统，由消息代理记录消费状态。消息代理将消息推送到消费者后，标记这条消息为已经被消费，但是这种方式无法很好地保证消费的处理语义。

比如当我们已经把消息发送给消费者之后，由于消费进程挂掉或者由于网络原因没有收到这条消息，如果我们在消费代理将其标记为已消费，这个消息就永久丢失了。

如果我们利用生产者收到消息后回复这种方法，消息代理需要记录消费状态，这种不可取。

如果采用 Push，消息消费的速率就完全由消费代理控制，一旦消费者发生阻塞，就会出现问题。

*Kafka 采取拉取模型(Poll)，由自己控制消费速度，以及消费的进度，消费者可以按照任意的偏移量进行消费。*

比如消费者可以消费已经消费过的消息进行重新处理，或者消费最近的消息等等。

## Kafka和其他消息队列的对比

Kafka 和传统的消息系统不同在于：
* Kafka是一个分布式系统，易于向外扩展。
* 它同时为发布和订阅提供高吞吐量。
* 它支持多订阅者，当失败时能自动平衡消费者。
* 消息的持久化。


 * Kafka 和其他消息队列的对比

![kafka01](https://github.com/bigDataHell/Kangaroo-/blob/master/images/kafka01.jpg)


## Topic 和 Partition

在 Kafka 中的每一条消息都有一个 Topic。一般来说在我们应用中产生不同类型的数据，都可以设置不同的主题。

一个主题一般会有多个消息的订阅者，当生产者发布消息到某个主题时，订阅了这个主题的消费者都可以接收到生产者写入的新消息。

Kafka 为每个主题维护了分布式的分区(Partition)日志文件，每个 Partition 在 Kafka 存储层面是 Append Log。

任何发布到此 Partition 的消息都会被追加到 Log 文件的尾部，在分区中的每条消息都会按照时间顺序分配到一个单调递增的顺序编号，也就是我们的 Offset。Offset 是一个 Long 型的数字。

我们通过这个 Offset 可以确定一条在该 Partition 下的唯一消息。在 Partition 下面是保证了有序性，但是在 Topic 下面没有保证有序性。

![kafka01](https://github.com/bigDataHell/Kangaroo-/blob/master/images/kafka04.jpg)

在上图中我们的生产者会决定发送到哪个 Partition：
 * 如果没有 Key 值则进行轮询发送。
* 如果有 Key 值，对 Key 值进行 Hash，然后对分区数量取余，保证了同一个 Key 值的会被路由到同一个分区；如果想队列的强顺序一致性，可以让所有的消息都设置为同一个 Key。


## 高可用模型及幂等

在分布式系统中一般有三种处理语义：

### at-least-once

至少一次，有可能会有多次。如果 Producer 收到来自 Ack 的确认，则表示该消息已经写入到 Kafka 了，此时刚好是一次，也就是我们后面的 Exactly-once。

但是如果 Producer 超时或收到错误，并且 request.required.acks 配置的不是 -1，则会重试发送消息，客户端会认为该消息未写入 Kafka。

如果 Broker 在发送 Ack 之前失败，但在消息成功写入 Kafka 之后，这一次重试将会导致我们的消息会被写入两次。

所以消息就不止一次地传递给最终 Consumer，如果 Consumer 处理逻辑没有保证幂等的话就会得到不正确的结果。

在这种语义中会出现乱序，也就是当第一次 Ack 失败准备重试的时候，但是第二消息已经发送过去了，这个时候会出现单分区中乱序的现象。

我们需要设置 Prouducer 的参数 max.in.flight.requests.per.connection，flight.requests 是 Producer 端用来保存发送请求且没有响应的队列，保证 Produce r端未响应的请求个数为 1。

### at-most-once

如果在 Ack 超时或返回错误时 Producer 不重试，也就是我们讲 request.required.acks = -1，则该消息可能最终没有写入 Kafka，所以 Consumer 不会接收消息。

### exactly-once

刚好一次，即使 Producer 重试发送消息，消息也会保证最多一次地传递给 Consumer。该语义是最理想的，也是最难实现的。

在 0.10 之前并不能保证 exactly-once，需要使用 Consumer 自带的幂等性保证。0.11.0 使用事务保证了。

### 如何实现 exactly-once

要实现 exactly-once 在 Kafka 0.11.0 中有两个官方策略：

**单 Producer 单 Topic**

每个 Producer 在初始化的时候都会被分配一个唯一的 PID，对于每个唯一的 PID，Producer 向指定的 Topic 中某个特定的 Partition 发送的消息都会携带一个从 0 单调递增的 Sequence Number。

在我们的 Broker 端也会维护一个维度为，每次提交一次消息的时候都会对齐进行校验：
* 如果消息序号比 Broker 维护的序号大一以上，说明中间有数据尚未写入，也即乱序，此时 Broker 拒绝该消息，Producer 抛出 InvalidSequenceNumber。
* 如果消息序号小于等于 Broker 维护的序号，说明该消息已被保存，即为重复消息，Broker 直接丢弃该消息，Producer 抛出 DuplicateSequenceNumber。
* 如果消息序号刚好大一，就证明是合法的。

上面所说的解决了两个问题：
* 当 Prouducer 发送了一条消息之后失败，Broker 并没有保存，但是第二条消息却发送成功，造成了数据的乱序。
* 当 Producer 发送了一条消息之后，Broker 保存成功，Ack 回传失败，Producer 再次投递重复的消息。

上面所说的都是在同一个 PID 下面，意味着必须保证在单个 Producer 中的同一个 Seesion 内，如果 Producer 挂了，被分配了新的 PID，这样就无法保证了，所以 Kafka 中又有事务机制去保证。

## 事务

在 Kafka 中事务的作用是：
* 实现 exactly-once 语义。
* 保证操作的原子性，要么全部成功，要么全部失败。
* 有状态的操作的恢复。

事务可以保证就算跨多个，在本次事务中的对消费队列的操作都当成原子性，要么全部成功，要么全部失败。

并且，有状态的应用也可以保证重启后从断点处继续处理，也即事务恢复。

在 Kafka 的事务中，应用程序必须提供一个唯一的事务 ID，即 Transaction ID，并且宕机重启之后，也不会发生改变。

Transactin ID 与 PID 可能一一对应，区别在于 Transaction ID 由用户提供，而 PID 是内部的实现对用户透明。

为了 Producer 重启之后，旧的 Producer 具有相同的 Transaction ID 失效，每次 Producer 通过 Transaction ID 拿到 PID 的同时，还会获取一个单调递增的 Epoch。

由于旧的 Producer 的 Epoch 比新 Producer 的 Epoch 小，Kafka 可以很容易识别出该 Producer 是老的，Producer 并拒绝其请求。

为了实现这一点，Kafka 0.11.0.0 引入了一个服务器端的模块，名为 Transaction Coordinator，用于管理 Producer 发送的消息的事务性。

该 Transaction Coordinator 维护 Transaction Log，该 Log 存于一个内部的 Topic 内。

由于 Topic 数据具有持久性，因此事务的状态也具有持久性。Producer 并不直接读写 Transaction Log，它与 Transaction Coordinator 通信，然后由 Transaction Coordinator 将该事务的状态插入相应的 Transaction Log。

Transaction Log 的设计与 Offset Log 用于保存 Consumer 的 Offset 类似。

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
 如果采用 Push，消息消费的速率就完全由消费代理控制，一旦消费者发生阻塞，就会出现问题。



本文章部分信息来源于 咖啡拿铁 大家可以去微信搜索,非常不错的一个关于后端,java,架构的微信号
    
