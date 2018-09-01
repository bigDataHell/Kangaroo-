# Kangaroo-

__模块:__

[BigData](#bigdata) &emsp; &emsp;[java](#java) &emsp; &emsp;[liunx](#liunx) &emsp;&emsp;[网络编程](#网络编程) &emsp;&emsp;[常用软件](#常用软件) &emsp;&emsp;[问题总结](#问题总结) &emsp;&emsp;[数据结构与算法](#数据结构与算法)

--------------------------------
## BigData

| 数据存储|数据转换|数据计算|分布式协调|分布式调度|
|:-----: |:------:|:-----:|:-------:|:-------:|
|           1          |       2            |        3              |          4           |          5          |
|[Hbase](#hbase)       |[Kafka](#kafka)     |[mapreduce](#数据计算)  |zookeeper             |Yarn                 |
|Cassandra             |flume               |hadoop                 |Consul                |Mesos                |
|MonggoDB              |RabbitMQ            |spark                  | Etcd                 |                     |          
|                      |                    |Filink                 | Eureka               |                     |
|#                     |#                   |[Storm](#storm)        |                      |                     |
|#                     |#                   |Hive                   |  #                   |                     |
|#                     |#                   |Pig                    |#                     |#                    |


#### hadoop

#### mapreduce

* 案例
    * 倒排索引:
    
    [倒排索引原理](https://github.com/bigDataHell/Kangaroo-/blob/master/Bigdata/mapreduce/invertedIndex/remade.md)  
    [Code](https://github.com/bigDataHell/Kangaroo-/blob/master/Bigdata/mapreduce/invertedIndex/InvertedIndex.java)
    
* MapReduce原理

    [MapTask工作机制](https://github.com/bigDataHell/Kangaroo-/blob/master/Bigdata/mapreduce/mapReduce_principle/MapTask%E5%B7%A5%E4%BD%9C%E6%9C%BA%E5%88%B6.md)<br> 
    [ReduceTask工作机制](https://github.com/bigDataHell/Kangaroo-/blob/master/Bigdata/mapreduce/mapReduce_principle/ReduceTask%E5%B7%A5%E4%BD%9C%E6%9C%BA%E5%88%B6.md)
    

#### flume

#### kafka

   * [Kafka面试题](https://github.com/bigDataHell/Kangaroo-/blob/master/Bigdata/kafka/kafka%E9%9D%A2%E8%AF%95%E9%A2%98.md)
   * [Kafka原理架构](https://github.com/bigDataHell/Kangaroo-/blob/master/Bigdata/kafka/kafka%E5%8E%9F%E7%90%86.md)
   * [Kafka控制台命令](https://github.com/bigDataHell/Kangaroo-/blob/master/Bigdata/kafka/kafkak%E6%8E%A7%E5%88%B6%E5%8F%B0%E5%91%BD%E4%BB%A4.md)
   * [KafkaAPI](https://github.com/bigDataHell/Kangaroo-/blob/master/Bigdata/kafka/API.md) 

#### storm

#### Hbase


--------------------------------
## java
 
#### 设计模式 
  * [单例模式]()
  * [代理模式]()
#### java机制

 * [java回调机制]()

--------------------------------
## liunx

#### 一键启动脚本
 * [zookeeeper启动脚本]()
 * [kafka启动脚本]()
--------------------------------
## 网络编程
--------------------------------
## 常用软件
--------------------------------
## 问题总结

#### 异常

* [java.lang.NoClassDefFoundError --Maven-scope](https://github.com/bigDataHell/Kangaroo-/blob/master/Exception/%E5%B8%B8%E8%A7%81%E5%BC%82%E5%B8%B8.md#javalangnoclassdeffounderror----maven)

* [java.lang.StackOverflowError](https://github.com/bigDataHell/Kangaroo-/blob/master/Exception/%E5%B8%B8%E8%A7%81%E5%BC%82%E5%B8%B8.md#2-javalangstackoverflowerror-%E5%A0%86%E6%A0%88%E6%BA%A2%E5%87%BA%E9%94%99%E8%AF%AF)
--------------------------------
## 数据结构与算法

#### 快排

  * [快排](https://github.com/bigDataHell/Kangaroo-/blob/master/dataStructures_algorithms/Quicksort.md)


--------------------------------



# 日常工作总结
 * 换行 \<br\>
 * 单行文本：前面使用两个Tab
 * 多行文本: 每行行首加两个Tab
 * <u>下划线<u>
 *  ~~删除线~~   ~删除线~

 * 插入连接
 
    \[百度](http://baidu.com)
  
 * 插入github仓库的图片并加入超链接
 
    \[![baidu]](http://baidu.com)  
    \[baidu]:http://www.baidu.com/img/bdlogo.gif "百度Logo"  
    
 * 代码
 
      插入代码片段：在代码上下行用```标记，注意`符号是tab键上面那个，要实现语法高亮，则在```后面加上编程语言的名称
      
 * 文本超链接
 
    [要显示的文字](链接的地址"鼠标悬停显示")，在URL之后用双引号括起来一个字符串，即鼠标悬停显示的文本，可不写
    
--------------------------------------------------
    
| 数据存储|数据转换|数据计算|分布式协调|分布式调度|SQL|数据结构与算法|异常|Linux|
|:-----: |:-----:|:-----:|:-----:|:-----:|:-----:|:-----:|:-----:|:-----:|
|1|2|3|4|5|6|7|8|9|
|#|[Kafka](#kafka)|[mapreduce](#数据计算)|#|#|[SQL](#sql)|[algorithms](#algorithms)|[Exception](#exception)|[一键启动脚本](#一键启动脚本)|
|[Hbase](#hbase)|#|hadoop|zookeeper|Mesos|[lucene](#lucene)|[设计模式](#设计模式)|[java](#java)|
|Cassandra|flume|spark|Consul|yarn|#|#||
|MonggoDB|RabbitMQ|Filink|Etcd|#|#|#||
|#|#|[Storm](#storm)|Eureka|#|#|#||
|#|#|Hive|#|#|#|#||
|#|#|Pig|#|#|#|#||




-----------------------
## 数据计算
  ### MapReduce
  
* 倒排索引

    [倒排索引概述](https://github.com/bigDataHell/Kangaroo-/blob/master/mapreduce/invertedIndex/remade.md)  
    [倒排索引代码](https://github.com/bigDataHell/Kangaroo-/blob/master/mapreduce/invertedIndex/InvertedIndex.java)
    
* MapReduce原理

    [MapTask工作机制](https://github.com/bigDataHell/Kangaroo-/blob/master/mapreduce/mapReduce_principle/MapTask%E5%B7%A5%E4%BD%9C%E6%9C%BA%E5%88%B6.md)<br> 
    [ReduceTask工作机制](https://github.com/bigDataHell/Kangaroo-/blob/master/mapreduce/mapReduce_principle/ReduceTask%E5%B7%A5%E4%BD%9C%E6%9C%BA%E5%88%B6.md)
    
 ---------------------------------
 ### Storm
    
 #### 原理与架构
 
   * [Storm原理与架构](https://github.com/bigDataHell/Kangaroo-/blob/master/storm/storm.md)
   
 #### 案例
 
  * [WordCount案例](https://github.com/bigDataHell/Kangaroo-/tree/master/storm/case/WordCountDemo)
  * [实时交易数据统计](https://github.com/bigDataHell/Kangaroo-/tree/master/storm/case/Real-timeTransactionDataStatistics)
  * [实时日志监控告警系统](https://github.com/bigDataHell/Kangaroo-/blob/master/storm/case/Real-timeLogMonitoringAlarmSystem/%E5%AE%9E%E6%97%B6%E6%97%A5%E5%BF%97%E7%9B%91%E6%8E%A7%E5%91%8A%E8%AD%A6%E7%B3%BB%E7%BB%9F.md)


-------------------------


## 数据转换

 ### Kafka
 
   * [Kafka面试题](https://github.com/bigDataHell/Kangaroo-/blob/master/kafka/kafka%E9%9D%A2%E8%AF%95%E9%A2%98.md)
   * [Kafka原理架构](https://github.com/bigDataHell/Kangaroo-/blob/master/kafka/kafka%E5%8E%9F%E7%90%86.md)
   * [Kafka控制台命令](https://github.com/bigDataHell/Kangaroo-/blob/master/kafka/kafkak%E6%8E%A7%E5%88%B6%E5%8F%B0%E5%91%BD%E4%BB%A4.md)
   * [KafkaAPI](https://github.com/bigDataHell/Kangaroo-/blob/master/kafka/API.md)
--------------------------------
 ## 数据结构与算法
   
  ### Algorithms
  
  * [快排](https://github.com/bigDataHell/Kangaroo-/blob/master/dataStructures_algorithms/Quicksort.md)
  
  
---------------------------------------------------------
## Exception

* [java.lang.NoClassDefFoundError --Maven-scope](https://github.com/bigDataHell/Kangaroo-/blob/master/Exception/%E5%B8%B8%E8%A7%81%E5%BC%82%E5%B8%B8.md#javalangnoclassdeffounderror----maven)

* [java.lang.StackOverflowError](https://github.com/bigDataHell/Kangaroo-/blob/master/Exception/%E5%B8%B8%E8%A7%81%E5%BC%82%E5%B8%B8.md#2-javalangstackoverflowerror-%E5%A0%86%E6%A0%88%E6%BA%A2%E5%87%BA%E9%94%99%E8%AF%AF)
   
------------------------------------------------------

## 一键启动脚本

 * [kafka启动脚本](https://github.com/bigDataHell/Kangaroo-/blob/master/Liunx/kafka%E5%90%AF%E5%8A%A8%E8%84%9A%E6%9C%AC.md) <br>
 * [zookeeper启动脚本](https://github.com/bigDataHell/Kangaroo-/blob/master/Liunx/zookeeper%E4%B8%80%E9%94%AE%E5%90%AF%E5%8A%A8.md)
 
 --------------------------------------------------------------
 ## lucene
 
 * [lucene的基本使用](https://github.com/bigDataHell/Kangaroo-/blob/master/SE/lucene.md)
 -----------------------
 
 ## SQL
 
 * [SQL详解](https://github.com/bigDataHell/Kangaroo-/blob/master/SQL/SQL%E8%AF%A6%E8%A7%A301.md)
 -------------------
 
 ## 设计模式
 
 * [单例模式](https://github.com/bigDataHell/Kangaroo-/blob/master/java/designMode/%E5%8D%95%E4%BE%8B%E6%A8%A1%E5%BC%8F.md)
 
 ---------------------
 
 ## java
 
 * [java回调机制](http://chuansong.me/n/1453687251714)
 
 --------------------------
 
 ## Hbase
 
 [Hbase搭建及控制台命令](https://github.com/bigDataHell/Kangaroo-/blob/master/Hbase/Hbase%E5%AE%89%E8%A3%85.md)
 

