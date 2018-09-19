# Spark Streaming流式处理

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

 (1)红色的矩形就是一个窗口，窗口框住的是一段时间内的数据流。

（2）这里面每一个time都是时间单元，在官方的例子中，每隔window size是3 time unit, 而且每隔2个单位时间，窗口会slide一次。

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

![01]()

#### 5.1 SparkStreaming接受socket数据，实现单词计数WordCount

#### 5.1.1 实现流程

* （1）安装并启动生产者

首先在linux服务器上用YUM安装nc工具，nc命令是netcat命令的简称,它是用来设置路由器。我们可以利用它向某个端口发送数据。

`yum install -y nc`

* （2）通过netcat工具向指定的端口发送数据

 `	nc -lk 9999  `
  
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


## 6.	Spark Streaming整合flume实战

## 7.	Spark Streaming整合kafka实战
