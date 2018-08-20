
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
      
      
## Kafka和其他消息队列的对比

Kafka 和传统的消息系统不同在于：
* Kafka是一个分布式系统，易于向外扩展。
* 它同时为发布和订阅提供高吞吐量。
* 它支持多订阅者，当失败时能自动平衡消费者。
* 消息的持久化。


 * Kafka 和其他消息队列的对比

![kafka01](https://github.com/bigDataHell/Kangaroo-/blob/master/images/kafka01.jpg)
      
## Kafka 架构原理

* kafka架构图:

![kafka01](https://github.com/bigDataHell/Kangaroo-/blob/master/images/kafka02.jpg)

Kafka名词解释

在一套 Kafka 架构中有多个 Producer，多个 Broker，多个 Consumer，每个 Producer 可以对应多个 Topic，每个 Consumer 只能对应一个 Consumer Group。

整个 Kafka 架构对应一个 ZK 集群，通过 ZK 管理集群配置，选举 Leader，以及在 Consumer Group 发生变化时进行 Rebalance。

## Topic 和 Partition

在 Kafka 中的每一条消息都有一个 Topic。一般来说在我们应用中产生不同类型的数据，都可以设置不同的主题。

一个主题一般会有多个消息的订阅者，当生产者发布消息到某个主题时，订阅了这个主题的消费者都可以接收到生产者写入的新消息。

Kafka 为每个主题维护了分布式的分区(Partition)日志文件，每个 Partition 在 Kafka 存储层面是 Append Log。

任何发布到此 Partition 的消息都会被追加到 Log 文件的尾部，在分区中的每条消息都会按照时间顺序分配到一个单调递增的顺序编号，也就是我们的 Offset。Offset 是一个 Long 型的数字。

我们通过这个 Offset 可以确定一条在该 Partition 下的唯一消息。在 Partition 下面是保证了有序性，但是在 Topic 下面没有保证有序性。

![kafka01](https://github.com/bigDataHell/Kangaroo-/blob/master/images/kafka04.jpg)

在上图中我们的生产者会决定发送到哪个 Partition：
* 如果没有 Key 值则进行轮询发送。
* 如果有 Key 值，对 Key 值进行 Hash，然后对分区数量取余，保证了同一个 Key 值的会被路由到同一个分区；如果想队列的强顺序一致性，可以让所有的消息都设置   为同一个 Key。

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

##  网络模型

* Kafka Client：单线程 Selector

![kafka01](https://github.com/bigDataHell/Kangaroo-/blob/master/images/kafka07.jpg)

单线程模式适用于并发链接数小，逻辑简单，数据量小的情况。在 Kafka 中，Consumer 和 Producer 都是使用的上面的单线程模式。

这种模式不适合 Kafka 的服务端，在服务端中请求处理过程比较复杂，会造成线程阻塞，一旦出现后续请求就会无法处理，会造成大量请求超时，引起雪崩。而在服务器中应该充分利用多线程来处理执行逻辑。

* Kafka Server：多线程 Selector

![kafka01](https://github.com/bigDataHell/Kangaroo-/blob/master/images/kafka08.jpg)

在 Kafka 服务端采用的是多线程的 Selector 模型，Acceptor 运行在一个单独的线程中，对于读取操作的线程池中的线程都会在 Selector 注册 Read 事件，负责服务端读取请求的逻辑。

成功读取后，将请求放入 Message Queue共享队列中。然后在写线程池中，取出这个请求，对其进行逻辑处理。

这样，即使某个请求线程阻塞了，还有后续的线程从消息队列中获取请求并进行处理，在写线程中处理完逻辑处理，由于注册了 OP_WIRTE 事件，所以还需要对其发送响应。

## 高可靠分布式存储模型

在 Kafka 中保证高可靠模型依靠的是副本机制，有了副本机制之后，就算机器宕机也不会发生数据丢失。

## 高性能的日志存储

Kafka 一个 Topic 下面的所有消息都是以 Partition 的方式分布式的存储在多个节点上。

同时在 Kafka 的机器上，每个 Partition 其实都会对应一个日志目录，在目录下面会对应多个日志分段(LogSegment)。

LogSegment 文件由两部分组成，分别为“.index”文件和“.log”文件，分别表示为 Segment 索引文件和数据文件。

这两个文件的命令规则为：Partition 全局的第一个 Segment 从 0 开始，后续每个 Segment 文件名为上一个 Segment 文件最后一条消息的 Offset 值，数值大小为 64 位，20 位数字字符长度，没有数字用 0 填充。

如下，假设有 1000 条消息，每个 LogSegment 大小为 100，下面展现了 900-1000 的索引和 Log：

![kafka01](https://github.com/bigDataHell/Kangaroo-/blob/master/images/kafka06.jpg)

由于 Kafka 消息数据太大，如果全部建立索引，既占了空间又增加了耗时，所以 Kafka 选择了稀疏索引的方式，这样索引可以直接进入内存，加快偏查询速度。

简单介绍一下如何读取数据，如果我们要读取第 911 条数据首先第一步，找到它是属于哪一段的。

根据二分法查找到它属于的文件，找到 0000900.index 和 00000900.log 之后，然后去 index 中去查找 (911-900) = 11 这个索引或者小于 11 最近的索引。

在这里通过二分法我们找到了索引是 [10,1367]，然后我们通过这条索引的物理位置 1367，开始往后找，直到找到 911 条数据。

上面讲的是如果要找某个 Offset 的流程，但是我们大多数时候并不需要查找某个 Offset，只需要按照顺序读即可。

而在顺序读中，操作系统会在内存和磁盘之间添加 Page Cache，也就是我们平常见到的预读操作，所以我们的顺序读操作时速度很快。

但是 Kafka 有个问题，如果分区过多，那么日志分段也会很多，写的时候由于是批量写，其实就会变成随机写了，随机 I/O 这个时候对性能影响很大。所以一般来说 Kafka 不能有太多的 Partition。

针对这一点，RocketMQ 把所有的日志都写在一个文件里面，就能变成顺序写，通过一定优化，读也能接近于顺序读。

大家可以思考一下：
* 为什么需要分区，也就是说主题只有一个分区，难道不行吗？
* 日志为什么需要分段？

## 副本机制


Kafka 的副本机制是多个服务端节点对其他节点的主题分区的日志进行复制。



当集群中的某个节点出现故障，访问故障节点的请求会被转移到其他正常节点(这一过程通常叫 Reblance)。



Kafka 每个主题的每个分区都有一个主副本以及 0 个或者多个副本，副本保持和主副本的数据同步，当主副本出故障时就会被替代。

![kafka01](https://github.com/bigDataHell/Kangaroo-/blob/master/images/kafka05.jpg)

在 Kafka 中并不是所有的副本都能被拿来替代主副本，所以在 Kafka 的 Leader 节点中维护着一个 ISR(In Sync Replicas)集合。



翻译过来也叫正在同步中集合，在这个集合中的需要满足两个条件：

* 节点必须和 ZK 保持连接。

* 在同步的过程中这个副本不能落后主副本太多。



另外还有个 AR(Assigned Replicas)用来标识副本的全集，OSR 用来表示由于落后被剔除的副本集合。



所以公式如下：ISR = Leader + 没有落后太多的副本；AR = OSR+ ISR。



这里先要说下两个名词：HW(高水位)是 Consumer 能够看到的此 Partition 的位置，LEO 是每个 Partition 的 Log 最后一条 Message 的位置。



HW 能保证 Leader 所在的 Broker 失效，该消息仍然可以从新选举的 Leader 中获取，不会造成消息丢失。



当 Producer 向 Leader 发送数据时，可以通过 request.required.acks 参数来设置数据可靠性的级别：

* 1（默认）：这意味着 Producer 在 ISR 中的 Leader 已成功收到的数据并得到确认后发送下一条 Message。如果 Leader 宕机了，则会丢失数据。

* 0：这意味着 Producer 无需等待来自 Broker 的确认而继续发送下一批消息。这种情况下数据传输效率最高，但是数据可靠性却是最低的。

* -1：Producer 需要等待 ISR 中的所有 Follower 都确认接收到数据后才算一次发送完成，可靠性最高。

但是这样也不能保证数据不丢失，比如当 ISR 中只有 Leader 时(其他节点都和 ZK 断开连接，或者都没追上)，这样就变成了 acks = 1 的情况。


## 高可用模型及幂等

在分布式系统中一般有三种处理语义：

*  **at-least-once 至少一次**

至少一次，有可能会有多次。如果 Producer 收到来自 Ack 的确认，则表示该消息已经写入到 Kafka 了，此时刚好是一次，也就是我们后面的 Exactly-once。

但是如果 Producer 超时或收到错误，并且 request.required.acks 配置的不是 -1，则会重试发送消息，客户端会认为该消息未写入 Kafka。

如果 Broker 在发送 Ack 之前失败，但在消息成功写入 Kafka 之后，这一次重试将会导致我们的消息会被写入两次。

所以消息就不止一次地传递给最终 Consumer，如果 Consumer 处理逻辑没有保证幂等的话就会得到不正确的结果。

在这种语义中会出现乱序，也就是当第一次 Ack 失败准备重试的时候，但是第二消息已经发送过去了，这个时候会出现单分区中乱序的现象。

我们需要设置 Prouducer 的参数 max.in.flight.requests.per.connection，flight.requests 是 Producer 端用来保存发送请求且没有响应的队列，保证 Produce r端未响应的请求个数为 1。

*  **at-most-once 最多一次**

如果在 Ack 超时或返回错误时 Producer 不重试，也就是我们讲 request.required.acks = -1，则该消息可能最终没有写入 Kafka，所以 Consumer 不会接收消息。

*  **exactly-once 绝对一次**

刚好一次，即使 Producer 重试发送消息，消息也会保证最多一次地传递给 Consumer。该语义是最理想的，也是最难实现的。

在 0.10 之前并不能保证 exactly-once，需要使用 Consumer 自带的幂等性保证。0.11.0 使用事务保证了。

*  如何实现 exactly-once 

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


  return Utils.toPositive(Utils.murmur2(keyBytes)) % numPartitions; 

    
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



**本文章部分信息来源于 `咖啡拿铁` 大家可以去微信搜索,非常不错的一个关于后端,java,架构的微信号**
    
