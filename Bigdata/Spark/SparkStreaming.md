# ![]()Spark Streaming流式处理

## 1 概述

Spark Streaming 是 Spark Core API 的扩展，它支持弹性的，高吞吐的，容错的实时数据流的处理。数据可以通过多种数据源获取，例如 Kafka，Flume，Kinesis 
以及 TCP sockets，也可以通过例如 map，reduce，join，window 等的高阶函数组成的复杂算法处理。最终，处理后的数据可以输出到文件系统，数据库以及实时仪表
盘中。事实上，你还可以在数据流上使用 Spark机器学习 以及 图形处理算法 。

 * 为什么要学习Spark Streaming

 1 易用  : 可以像编写离线批处理一样去编写流式程序，支持java/scala/python语言。

 2 容错 : SparkStreaming在没有额外代码和配置的情况下可以恢复丢失的工作。

 3 易整合到Spark体系 : 流式处理与批处理和交互式查询相结合。

 

## 2 Spark Streaming原理

#### 2.1 Spark Streaming原理

Spark Streaming 是基于spark的流式批处理引擎，其基本原理是把输入数据以某一时间间隔批量的处理，当批处理间隔缩短到秒级时，便可以用于处理实时数据流。

#### 2.2 Spark Streaming计算流程

Spark Streaming是将流式计算分解成一系列短小的批处理作业。这里的批处理引擎是Spark Core，也就是把Spark Streaming的输入数据按照batch size（如1秒）
分成一段一段的数据（Discretized Stream），每一段数据都转换成Spark中的RDD（Resilient Distributed Dataset），然后将Spark Streaming中对DStream的
Transformation操作变为针对Spark中对RDD的Transformation操作，将RDD经过操作变成中间结果保存在内存中。整个流式计算根据业务的需求可以对中间的结果进行
缓存或者存储到外部设备。下图显示了Spark Streaming的整个流程。

#### 2.3 Spark Streaming容错性

对于流式计算来说，容错性至关重要。首先我们要明确一下Spark中RDD的容错机制。每一个RDD都是一个不可变的分布式可重算的数据集，其记录着确定性的操作继承关
系（lineage），所以只要输入数据是可容错的，那么任意一个RDD的分区（Partition）出错或不可用，都是可以利用原始输入数据通过转换操作而重新算出的。  

#### 2.4 Spark Streaming实时性

对于实时性的讨论，会牵涉到流式处理框架的应用场景。Spark Streaming将流式计算分解成多个Spark Job，对于每一段数据的处理都会经过Spark DAG图分解以及
Spark的任务集的调度过程。对于目前版本的Spark Streaming而言，其最小的Batch Size的选取在0.5~2秒钟之间（Storm目前最小的延迟是100ms左右），所以
Spark Streaming能够满足除对实时性要求非常高（如高频实时交易）之外的所有流式准实时计算场景。


## 3 DStream

#### 3.1 什么是DStream
Discretized Stream是Spark Streaming的基础抽象，代表持续性的数据流和经过各种Spark算子操作后的结果数据流。在内部实现上，DStream是一系列连续的RDD
来表示。每个RDD含有一段时间间隔内的数据，

Spark Streaming 提供了一个高层次的抽象叫做离散流（discretized stream）或者 DStream，它代表一个连续的数据流。DStream 可以通过来自数据源的输入
数据流创建，例如 Kafka，Flume 以及 Kinesis，或者在其他 DStream 上进行高层次的操作创建。在内部，一个 DStream 是通过一系列的 RDD 来表示。

它的工作流程像下面的图所示一样，接受到实时数据后，给数据分批次，然后传给Spark Engine处理最后生成该批次的结果。

Discretized Stream（离散化流）或者 DStream（离散流）是 Spark Streaming 提供的基本抽象。它代表了一个连续的数据流，无论是从源接收到的输入数据流，
还是通过变换输入流所产生的处理过的数据流。在内部，一个离散流（DStream）被表示为一系列连续的 RDDs，RDD 是 Spark 的一个不可改变的，分布式的数据集的
抽象

## 4 DStream相关操作

DStream上的操作与RDD的类似，分为Transformations（转换）和Output Operations（输出）两种，此外转换操作中还有一些比较特殊的操作，如：
updateStateByKey()、transform()以及各种Window相关的操作。

#### 4.1 Transformations on DStreams

|Transformation|Meaning|
|:-----------| :------:|
|map(func)	|对DStream中的各个元素进行func函数操作，然后返回一个新的DStream|
|flatMap(func)	|与map方法类似，只不过各个输入项可以被输出为零个或多个输出项|
|filter(func)	|过滤出所有函数func返回值为true的DStream元素并返回一个新的DStream|
|repartition(numPartitions)	|增加或减少DStream中的分区数，从而改变DStream的并行度|
|union(otherStream)	|将源DStream和输入参数为otherDStream的元素合并，并返回一个新的DStream.|
|count()	|通过对DStream中的各个RDD中的元素进行计数，然后返回只有一个元素的RDD构成的DStream|
|reduce(func)	|对源DStream中的各个RDD中的元素利用func进行聚合操作，然后返回只有一个元素的RDD构成的新的DStream.|
|countByValue()	|对于元素类型为K的DStream，返回一个元素为（K,Long）键值对形式的新的DStream，Long对应的值为源DStream中各个RDD的key出现的次数|
|reduceByKey(func, [numTasks])	|	利用func函数对源DStream中的key进行聚合操作，然后返回新的（K，V）对构成的DStream|
|join(otherStream, [numTasks])	|输入为（K,V)、（K,W）类型的DStream，返回一个新的（K，（V，W））类型的DStream|
|cogroup(otherStream, [numTasks])	|输入为（K,V)、（K,W）类型的DStream，返回一个新的 (K, Seq[V], Seq[W]) 元组类型的DStream|
|transform(func)		|通过RDD-to-RDD函数作用于DStream中的各个RDD，可以是任意的RDD操作，从而返回一个新的RDD|
|updateStateByKey(func)	|根据key的之前状态值和key的新值，对key进行更新，返回一个新状态的DStream|

__特殊的Transformations__

* （1）UpdateStateByKey Operation

UpdateStateByKey用于记录历史记录，保存上次的状态

* （2）Window Operations(开窗函数)

滑动窗口转换操作: <br>
滑动窗口转换操作的计算过程如下图所示，我们可以事先设定一个滑动窗口的长度（也就是窗口的持续时间），并且设定滑动窗口的时间间隔（每隔多长时间执行一次计算
），然后，就可以让窗口按照指定时间间隔在源DStream上滑动，每次窗口停放的位置上，都会有一部分DStream被框入窗口内，形成一个小段的DStream，这时，
就可以启动对这个小段DStream的计算。

   (1)  红色的矩形就是一个窗口，窗口框住的是一段时间内的数据流。

（2）这里面每一个time都是时间单元，在官方的例子中，每隔window size是3 time unit, 而且每隔2个单位时       间，窗口会slide一次。

所以基于窗口的操作，需要指定2个参数：

•	window length - The duration of the window (3 in the figure) <br>
•	slide interval - The interval at which the window-based operation is performed (2 in the figure).  

a.  窗口大小，一段时间内数据的容器。

b.  滑动间隔，每隔多久计算一次。



#### 4.2 Output Operations on DStreams

Output Operations可以将DStream的数据输出到外部的数据库或文件系统，当某个Output Operations被调用时（与RDD的Action相同），spark streaming程序
才会开始真正的计算过程。

|Output Operation	| Meaning |
|:----------:  | :------------|
|print()	|打印到控制台|
|saveAsTextFiles(prefix, [suffix])	|保存流的内容为文本文件，文件名为 "prefix-TIME_IN_MS[.suffix]".|
|saveAsObjectFiles(prefix, [suffix])	|保存流的内容为SequenceFile，文件名为  "prefix-TIME_IN_MS[.suffix]". |
|saveAsHadoopFiles(prefix, [suffix])|	保存流的内容为hadoop文件，文件名为  "prefix-TIME_IN_MS[.suffix]". |
|foreachRDD(func) |	对Dstream里面的每个RDD执行func|




## 5 DStream操作实战

* 架构图

![01](https://github.com/bigDataHell/Kangaroo-/blob/master/images/SparkStreaming01.png)

#### 5.1 SparkStreaming接受socket数据，实现单词计数WordCount

#### 5.1.1 实现流程

* （1）安装并启动生产者

首先在linux服务器上用YUM安装nc工具，nc命令是netcat命令的简称,它是用来设置路由器。我们可以利用它向某个端口发送数据。

`yum install -y nc`

* （2）通过netcat工具向指定的端口发送数据

 `nc -lk 9999  `

* （ 3）编写Spark Streaming程序

``` scala
package cn.hzh.streaming

import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * sparkStreming流式处理接受socket数据，实现单词统计
  */
object SparkStreamingTCP {

  def main(args: Array[String]): Unit = {
    //配置sparkConf参数 设置master的lcoal[N] N必须大于 1 一个线程负责接收数据,其他线程处理数据
    val sparkConf: SparkConf = new SparkConf().setAppName("SparkStreamingTCP").setMaster("local[2]")
    //构建sparkContext对象
    val sc: SparkContext = new SparkContext(sparkConf)
    //设置日志输出级别
    sc.setLogLevel("WARN")
    //构建StreamingContext对象，每个批处理的时间间隔
    val scc: StreamingContext = new StreamingContext(sc, Seconds(5))
    //注册一个监听的IP地址和端口  用来收集数据
    val lines: ReceiverInputDStream[String] = scc.socketTextStream("192.168.168.121", 9999)
    //切分每一行记录
    //flatmap()是将函数应用于RDD中的每个元素，将返回的迭代器的所有内容构成新的RDD,
    val words: DStream[String] = lines.flatMap(_.split(" "))
    //每个单词记为1
    val wordAndOne: DStream[(String, Int)] = words.map((_, 1))
    //分组聚合
    val result: DStream[(String, Int)] = wordAndOne.reduceByKey(_ + _)
    //打印数据
    result.print()
    scc.start()
    scc.awaitTermination()
  }
}
```

####   5.2 SparkStreaming接受socket数据，实现所有批次单词计数结果累加

在上面的那个案例中存在这样一个问题：每个批次的单词次数都被正确的统计出来，但是结果不能累加！如果将所有批次的结果数据进行累加使用

`updateStateByKey(func)`

来更新状态.



* 编写Spark Streaming程序

``` scala
object SparkStreamingTCPTotal {

  //newValues 表示当前批次汇总成的(word,1)中相同单词的所有的1
  //runningCount 历史的所有相同key的value总和
  def updateFunction(newValues: Seq[Int], runningCount: Option[Int]): Option[Int] = {
    val newCount =runningCount.getOrElse(0)+newValues.sum
    Some(newCount)
  }

  def main(args: Array[String]): Unit = {

    //配置sparkConf参数
    val sparkConf: SparkConf = new SparkConf().setAppName("SparkStreamingTCPTotal").setMaster("local[2]")
    //构建sparkContext对象
    val sc: SparkContext = new SparkContext(sparkConf)
    //设置日志输出的级别
    sc.setLogLevel("WARN")
    //构建StreamingContext对象，每个批处理的时间间隔
    val scc: StreamingContext = new StreamingContext(sc, Seconds(5))
    //设置checkpoint路径，当前项目下有一个ck目录
    scc.checkpoint("./ck")
    //注册一个监听的IP地址和端口  用来收集数据
    val lines: ReceiverInputDStream[String] = scc.socketTextStream("192.168.168.121", 9999)
    //切分每一行记录
    val words: DStream[String] = lines.flatMap(_.split(" "))
    //每个单词记为1
    val wordAndOne: DStream[(String, Int)] = words.map((_, 1))
    //累计统计单词出现的次数
    val result: DStream[(String, Int)] = wordAndOne.updateStateByKey(updateFunction)
    result.print()
    scc.start()
    scc.awaitTermination()
  }
}


```

#### 5.3 SparkStreaming开窗函数reduceByKeyAndWindow，实现单词计数

  ![02](https://github.com/bigDataHell/Kangaroo-/blob/master/images/sparkStreaming02.png)

* 代码实现

``` scala
object SparkStreamingTCPWindow {

  def main(args: Array[String]): Unit = {
    //配置sparkConf参数
    val sparkConf: SparkConf = new SparkConf().setAppName("SparkStreamingTCPWindow")
        .setMaster("local[2]")
    //构建sparkContext对象
    val sc: SparkContext = new SparkContext(sparkConf)
    //设置日志输出的级别
    sc.setLogLevel("WARN")
    //构建StreamingContext对象，每个批处理的时间间隔
    val scc: StreamingContext = new StreamingContext(sc, Seconds(5))
    //注册一个监听的IP地址和端口  用来收集数据
    val lines: ReceiverInputDStream[String] = scc.socketTextStream("192.168.168.121", 9999)
    //切分每一行记录
    val words: DStream[String] = lines.flatMap(_.split(" "))
    //每个单词记为1
    val wordAndOne: DStream[(String, Int)] = words.map((_, 1))
    // reduceByKeyAndWindow方法需要三个参数 :
    // reduceFunction : 一个函数
    // windowDuration : 表示窗口的长度
    // slied 窗口滑动间隔,即每个多久计算一次
    val result = wordAndOne.reduceByKeyAndWindow((x: Int, y: Int) => x + y, Seconds(10), Seconds(10))

    // 8 打印
    result.print()

    //9 开启流式计算
    scc.start()
    scc.awaitTermination()

  }
}
```

#### 5.4 SparkStreaming开窗函数统计一定时间内的热门词汇

* 代码实现

``` scala
object Test {

  def main(args: Array[String]): Unit = {
    //配置sparkConf参数
    val sparkConf: SparkConf = new SparkConf().setAppName("Test").setMaster("local[2]")
    //构建sparkContext对象
    val sc: SparkContext = new SparkContext(sparkConf)
    sc.setLogLevel("WARN")
    //构建StreamingContext对象，每个批处理的时间间隔
    val scc: StreamingContext = new StreamingContext(sc,Seconds(5))
    //注册一个监听的IP地址和端口  用来收集数据
    val lines: ReceiverInputDStream[String] = scc.socketTextStream("192.168.168.121",9999)
    //切分每一行记录
    val words: DStream[String] = lines.flatMap(_.split(" "))
    //每个单词记为1
    val wordAndOne: DStream[(String, Int)] = words.map((_,1))
    //reduceByKeyAndWindow函数参数意义：
    // windowDuration:表示window框住的时间长度，如本例5秒切分一次RDD，框10秒，就会保留最近2次切分的RDD
    //slideDuration:  表示window滑动的时间长度，即每隔多久执行本计算
    val result: DStream[(String, Int)] = wordAndOne.reduceByKeyAndWindow((a:Int,b:Int)=>a+b,Seconds(10),Seconds(5))
    val data=result.transform(rdd=>{
      //降序处理后，取前3位
      val dataRDD: RDD[(String, Int)] = rdd.sortBy(t=>t._2,false)
      val sortResult: Array[(String, Int)] = dataRDD.take(3)
      println("--------------print top 3 begin--------------")
      sortResult.foreach(println)
      println("--------------print top 3 end--------------")
      dataRDD
    })
    data.print()
    scc.start()
    scc.awaitTermination()
  }
}


```

## 6.	Spark Streaming整合flume实战

flume作为日志实时采集的框架，可以与SparkStreaming实时处理框架进行对接，flume实时产生数据，sparkStreaming做实时处理。
Spark Streaming对接FlumeNG有两种方式，一种是FlumeNG将消息Push推给Spark Streaming，还有一种是Spark Streaming从flume 中Poll拉取数据。

#### 6.1 Poll方式 实际采用

* （1）安装flume1.6以上

* （2）下载依赖包

spark-streaming-flume-sink_2.11-2.0.2.jar放入到flume的lib目录下

* （3）修改flume/lib下的scala依赖包版本

从spark安装目录的jars文件夹下找到scala-library-2.11.8.jar 包，替换掉flume的lib目录下自带的scala-library-2.10.1.jar。

* （4）写flume的agent，注意既然是拉取的方式，那么flume向自己所在的机器上产数据就行

* （5）编写flume-poll.conf配置文件

``` 
a1.sources = r1
a1.sinks = k1
a1.channels = c1

#source
a1.sources.r1.channels = c1
a1.sources.r1.type = spooldir
a1.sources.r1.spoolDir = /root/data
a1.sources.r1.fileHeader = true


#channel
a1.channels.c1.type =memory
a1.channels.c1.capacity = 20000
a1.channels.c1.transactionCapacity=5000


#sinks
a1.sinks.k1.channel = c1
a1.sinks.k1.type = org.apache.spark.streaming.flume.sink.SparkSink
a1.sinks.k1.hostname=hadoop-node-1
a1.sinks.k1.port = 8888
a1.sinks.k1.batchSize= 2000

``` 
* (6) 启动flume

`flume-ng agent -n a1 -c /export/server/flume/conf -f /export/server/flume/conf/flume-poll-spark.conf -Dflume.root.logger=INFO,console`

* (7) 代码实现

```
<dependency>
    <groupId>org.apache.spark</groupId>
    <artifactId>spark-streaming-flume_2.11</artifactId>
    <version>2.0.2</version>
</dependency>

```

``` scala
object SparkStreamingPollFlume {

  def updataFunc(currentValues: Seq[Int], historyValues: Option[Int]): Option[Int] = {
    val newValues = currentValues.sum + historyValues.getOrElse(0)
    Some(newValues)
  }

  def main(args: Array[String]): Unit = {

    // 1 创建sparkConf
    val sparkConf = new SparkConf().setAppName("SparkStreamingPollFlumes").setMaster("local[2]")
    // 2 创建SparkContext
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("WARN")
    // 3 创建StreamingContext
    val streamingContext = new StreamingContext(sc, Seconds(5))
    streamingContext.checkpoint("./flume")
    //4、通过FlumeUtils调用createPollingStream方法获取flume中的数据
    val pollingStream: ReceiverInputDStream[SparkFlumeEvent] = FlumeUtils
        .createPollingStream(streamingContext, "192.168.168.121", 8888)
    // 5 获取flume中even的body {"headers":xxxxxx,"body":xxxxx}
    val data: DStream[String] = pollingStream.map(x => new String(x.event.getBody.array()))
    // 6 切分每一行,每个单词记为1
    val words = data.flatMap(_.split(" ")).map((_, 1))
    // 7 相同单词出现次数累加
    val result: DStream[(String, Int)] = words.updateStateByKey(updataFunc)

    result.print()

    streamingContext.start()
    streamingContext.awaitTermination()
  }
}
```

#### 6.2 Push方式

* （1）编写flume-push.conf配置文件

```
#push mode
a1.sources = r1
a1.sinks = k1
a1.channels = c1
#source
a1.sources.r1.channels = c1
a1.sources.r1.type = spooldir
a1.sources.r1.spoolDir = /root/data
a1.sources.r1.fileHeader = true
#channel
a1.channels.c1.type =memory
a1.channels.c1.capacity = 20000
a1.channels.c1.transactionCapacity=5000
#sinks
a1.sinks.k1.channel = c1
a1.sinks.k1.type = avro
a1.sinks.k1.hostname=192.168.30.35
a1.sinks.k1.port = 8888
a1.sinks.k1.batchSize= 2000                    
```

__注意配置文件中指明的hostname和port是spark应用程序所在服务器的ip地址和端口。__

* （2）代码实现如下：
``` scala
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.flume.{FlumeUtils, SparkFlumeEvent}
import org.apache.spark.storage.StorageLevel

/**
  * Created by Asus on 2018/9/19.
  */
object SparkStreamingPushFlume {

  def updataFunc(currentValues: Seq[Int], historyValues: Option[Int]): Option[Int] = {
    val newValues = currentValues.sum + historyValues.getOrElse(0)
    Some(newValues)
  }

  def main(args: Array[String]): Unit = {
    // 1 创建sparkConf
    val sparkConf = new SparkConf().setAppName("SparkStreamingPushFlume").setMaster("local[2]")
    // 2 创建SparkContext
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("WARN")
    // 3 创建StreamingContext
    val streamingContext = new StreamingContext(sc, Seconds(5))
    streamingContext.checkpoint("./flume")
    //4、通过FlumeUtils调用createPollingStream方法获取flume中的数据
    val pushingStream: ReceiverInputDStream[SparkFlumeEvent] = FlumeUtils
        .createStream(streamingContext, "192.168.30.35", 9999, StorageLevel.MEMORY_AND_DISK_2)
    // 5 获取flume中even的body {"headers":xxxxxx,"body":xxxxx}
    val data: DStream[String] = pushingStream.map(x => new String(x.event.getBody.array()))
    // 6 切分每一行,每个单词记为1
    val words = data.flatMap(_.split(" ")).map((_, 1))
    // 7 相同单词出现次数累加
    val result: DStream[(String, Int)] = words.updateStateByKey(updataFunc)

    result.print()

    streamingContext.start()
    streamingContext.awaitTermination()
  }
}
```

* 3 先执行Spark代码

* 4 启动flume

`lume-ng agent -n a1 -c /export/server/flume/conf -f /export/server/flume/conf/flume-push-spark.conf -Dflume.root.logger=INFO,console `

## 7.	Spark Streaming整合kafka实战

kafka作为一个实时的分布式消息队列，实时的生产和消费消息，这里我们可以利用SparkStreaming实时地读取kafka中的数据，然后进行相关计算。
在Spark1.3版本后，KafkaUtils里面提供了两个创建dstream的方法，一种为KafkaUtils.createDstream，另一种为KafkaUtils.createDirectStream。


### 7.1 KafkaUtils.createDstream方式

KafkaUtils.createDstream(ssc, [zk], [group id], [per-topic,partitions] ) 使用了receivers接收器来接收数据，利用的是Kafka高层次的消费者api，对于所有的receivers接收到的数据将会保存在Spark executors中，然后通过Spark Streaming启动job来处理这些数据，默认会丢失，可启用WAL日志，它同步将接受到数据保存到分布式文件系统上比如HDFS。 所以数据在出错的情况下可以恢复出来 。

A、创建一个receiver接收器来对kafka进行定时拉取数据，这里产生的dstream中rdd分区和kafka的topic分区不是一个概念，故如果增加特定主体分区数仅仅是增加一个receiver中消费topic的线程数，并没有增加spark的并行处理的数据量。

B、对于不同的group和topic可以使用多个receivers创建不同的DStream 

C、如果启用了WAL(spark.streaming.receiver.writeAheadLog.enable=true)
同时需要设置存储级别(默认StorageLevel.MEMORY_AND_DISK_SER_2)，

#### 7.1.1 KafkaUtils.createDstream实战

* 1 添加kafka的pom依赖
``` 
<dependency>
    <groupId>org.apache.spark</groupId>
    <artifactId>spark-streaming-kafka_0-8_2.11</artifactId>
    <version>2.0.2</version>
</dependency>

``` 

* 2 启动zookeeper集群

zkServer.sh start

* 3 启动kafka集群

kafka-server-start.sh  /export/servers/kafka/config/server.properties

* 4 创建topic

`kafka-topics.sh --create --zookeeper hadoop-node-1:2181 --replication-factor 1 --partitions 3 --topic spark_01`

* 5 向topic中生产数据

通过shell命令向topic发送消息

`  kafka-console-producer.sh --broker-list hadoop-node-1:9092 --topic  spark_01`

* 编写Spark Streaming应用程序
``` scala
//todo:利用sparkStreaming对接kafka实现单词计数----采用receiver(高级API)
object SparkStreamingKafka_Receiver {

  def main(args: Array[String]): Unit = {

    // 创建SparkConf
    val conf = new SparkConf().setAppName("SparkStreamingKafka_Receiver").setMaster("local[2]")
    // 创建SparkContext
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    //  创建StreamingContext
    val ssc: StreamingContext = new StreamingContext(sc, Seconds(5))
    // 准备zk的地址
    val zkQuorum = "hadoop-node-1:2181"
    // 准备groupID
    val groupId = "spark_receiver"
    // 定义topic 当前这个value并不是topic对应的分区数，而是针对于每一个分区使用多少个线程去消费(增加了消费速度)
    val topic = Map("spark_01"->1)

    //7、KafkaUtils.createStream 去接受kafka中topic数据
    //(String, String) 前面一个string表示topic名称，后面一个string表示topic中的数据
    //使用多个reveiver接受器去接受kafka中topic数据
    val stream: ReceiverInputDStream[(String, String)] = KafkaUtils.createStream(ssc,zkQuorum,groupId,topic)

    // 8 获取kafka中topic的数据
    val topicData: DStream[String] = stream.map(_._2)
    // 9 切割数据
    val wordAndOne: DStream[(String, Int)] = topicData.flatMap(_.split(" ")).map((_,1))

    val result = wordAndOne.reduceByKey(_+_)

    result.print()
    ssc.start()
    ssc.awaitTermination()
  }
}
``` 
* 使用多个receivers创建不同的DStream 

``` scala
//todo:利用sparkStreaming对接kafka实现单词计数----采用receiver(高级API)
object SparkStreamingKafka_Receiver {

  def main(args: Array[String]): Unit = {

    // 创建SparkConf
    val conf = new SparkConf().setAppName("SparkStreamingKafka_Receiver")
        // master[N] 至少要大于三
        .setMaster("local[4]")
        //表示开启WAL预写日志，保证数据源的可靠性
        .set("spark.streaming.receiver.writeAheadLog.enable=true", "true")

    // 创建SparkContext
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    //  创建StreamingContext
    val ssc: StreamingContext = new StreamingContext(sc, Seconds(5))
    ssc.checkpoint("./spark_receiver")
    // 准备zk的地址
    val zkQuorum = "hadoop-node-1:2181"
    // 准备groupID
    val groupId = "spark_receiver"
    // 定义topic 当前这个value并不是topic对应的分区数，而是针对于每一个分区使用多少个线程去消费(增加了消费速度)
    val topic = Map("spark_01" -> 2)

    //7、KafkaUtils.createStream 去接受kafka中topic数据
    //(String, String) 前面一个string表示topic名称，后面一个string表示topic中的数据
    //使用多个reveiver接受器去接受kafka中topic数据
    //val stream: ReceiverInputDStream[(String, String)] = KafkaUtils.createStream(ssc,zkQuorum,groupId,topic)

    //使用多个reveiver接受器去接受kafka中topic数据
    val dstreamSeq: immutable.IndexedSeq[ReceiverInputDStream[(String, String)]] = (1 to 3)
        .map(x => {
          val stream: ReceiverInputDStream[(String, String)] = KafkaUtils
              .createStream(ssc, zkQuorum, groupId, topic)
          stream
        }
        )

    //利用streamingcontext调用union,获取得到所有receiver接受器的数据
    val totalDstream: DStream[(String, String)] = ssc.union(dstreamSeq)


    // 8 获取kafka中topic的数据
    val topicData: DStream[String] = totalDstream.map(_._2)
    // 9 切割数据
    val wordAndOne: DStream[(String, Int)] = topicData.flatMap(_.split(" ")).map((_, 1))

    val result = wordAndOne.reduceByKey(_ + _)

    result.print()
    ssc.start()
    ssc.awaitTermination()
  }
}

``` 

总结 : 

通过这种方式实现，刚开始的时候系统正常运行，没有发现问题，但是如果系统异常重新启动sparkstreaming程序后，发现程序会重复处理已经处理过的数据，这种基于receiver的方式，是使用Kafka的高级API，topic的offset偏移量在ZooKeeper中。这是消费Kafka数据的传统方式。这种方式配合着WAL机制可以保证数据零丢失的高可靠性，但是却无法保证数据只被处理一次，可能会处理两次。因为Spark和ZooKeeper之间可能是不同步的。官方现在也已经不推荐这种整合方式，我们使用官网推荐的第二种方式kafkaUtils的createDirectStream()方式。

### 7.2 KafkaUtils.createDirectStream方式


这种方式不同于Receiver接收数据，它定期地从kafka的topic下对应的partition中查询最新的偏移量，再根据偏移量范围在每个batch里面处理数据，Spark通过调用kafka简单的消费者Api（低级api）读取一定范围的数据。 

相比基于Receiver方式有几个优点： 

A、简化并行

不需要创建多个kafka输入流，然后union它们，sparkStreaming将会创建和kafka分区数相同的rdd的分区数，而且会从kafka中并行读取数据，spark中RDD的分区数和kafka中的topic分区数是一一对应的关系。

B、高效，	

第一种实现数据的零丢失是将数据预先保存在WAL中，会复制一遍数据，会导致数据被拷贝两次，第一次是接受kafka中topic的数据，另一次是写到WAL中。而没有receiver的这种方式消除了这个问题。 

C、恰好一次语义(Exactly-once-semantics)

Receiver读取kafka数据是通过kafka高层次api把偏移量写入zookeeper中，虽然这种方法可以通过数据保存在WAL中保证数据不丢失，但是可能会因为sparkStreaming和ZK中保存的偏移量不一致而导致数据被消费了多次。EOS通过实现kafka低层次api，偏移量仅仅被ssc保存在checkpoint中，消除了zk和ssc偏移量不一致的问题。缺点是无法使用基于zookeeper的kafka监控工具。


#### 7.2.1 KafkaUtils.createDirectStream实战

* 代码

``` scala
import kafka.serializer.StringDecoder
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka.KafkaUtils

// todo :利用sparkStreaming对接kafka实现单词计数----采用Direct(低级API)
object SparkStreamingKafka_Direct {

  def main(args: Array[String]): Unit = {

    //1、创建sparkConf
    val sparkConf: SparkConf = new SparkConf()
        .setAppName("SparkStreamingKafka_Direct")
        .setMaster("local[2]")
    //2、创建sparkContext
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("WARN")
    //3、创建StreamingContext
    val ssc = new StreamingContext(sc,Seconds(5))
    ssc.checkpoint("./Kafka_Direct")
    //4、配置kafka相关参数
    val kafkaParams=Map("metadata.broker.list"->"hadoop-node-1:9092,hadoop-node-2:9092,hadoop-node-3:9092","group.id"->"Kafka_Direct")
    //5、定义topic
    val topics=Set("spark_01")
    //6、通过 KafkaUtils.createDirectStream接受kafka数据，这里采用是kafka低级api偏移量不受zk管理
    val dstream: InputDStream[(String, String)] = KafkaUtils.createDirectStream[String,String,StringDecoder,StringDecoder](ssc,kafkaParams,topics)
    //7、获取kafka中topic中的数据
    val topicData: DStream[String] = dstream.map(_._2)
    //8、切分每一行,每个单词计为1
    val wordAndOne: DStream[(String, Int)] = topicData.flatMap(_.split(" ")).map((_,1))
    //9、相同单词出现的次数累加
    val result: DStream[(String, Int)] = wordAndOne.reduceByKey(_+_)
    //10、打印输出
    result.print()
    //开启计算
    ssc.start()
    ssc.awaitTermination()

  }
}
``` 

