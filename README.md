# Kangaroo-

__模块:__

[BigData](#bigdata) &emsp; &emsp;[java](#java) &emsp; &emsp;[liunx](#liunx) &emsp;&emsp;[网络编程](#网络编程) &emsp;&emsp;[常用软件](#常用软件) &emsp;&emsp;[问题总结](#问题总结) &emsp;&emsp;[数据结构与算法](#数据结构与算法) &emsp;&emsp;[Scala](#scala) &emsp;&emsp; [Docker](#docker)

--------------------------------
## BigData

| 数据存储|数据转换|数据计算|分布式协调|分布式调度|分布式搜索引擎|
|:-----: |:------:|:-----:|:-------:|:-------:|:-------:|
|           1          |       2            |        3              |          4           |     5 |6|
|[Hbase](#hbase)       |[Kafka](#kafka)     |[mapreduce](#数据计算)  |zookeeper             |Yarn     |[elasticsearch](#elasticsearch)|
|Cassandra             |flume               |hadoop                 |Consul                |Mesos                |
|MonggoDB              |RabbitMQ            |[Spark](#spark)                  | Etcd                 |                     |          
|                      |                    |Filink                 | Eureka               |                     |
|#                     |#                   |[Storm](#storm)        |                      |                     |
|#                     |#                   |Hive                   |  #                   |                     |
|#                     |#                   |Pig                    |#                     |#                    |



#### Spark

* [简介](https://github.com/bigDataHell/Kangaroo-/tree/master/Bigdata/Spark)
* [sortBy和sortByKey详解](https://github.com/bigDataHell/Kangaroo-/blob/master/Bigdata/Spark/sortBy%E5%92%8CsortByKey%E8%AF%A6%E8%A7%A3.md)
* [RDD : 弹性分布式数据集](https://github.com/bigDataHell/Kangaroo-/blob/master/Bigdata/Spark/Spark-RDD.md)
* [面试题](https://github.com/bigDataHell/Kangaroo-/blob/master/Bigdata/Spark/%E9%9D%A2%E8%AF%95%E9%A2%98.md)

#### hadoop

#### mapreduce

* 案例 : 倒排索引:
    
    [倒排索引原理](https://github.com/bigDataHell/Kangaroo-/blob/master/Bigdata/mapreduce/invertedIndex/remade.md)  
    [Code](https://github.com/bigDataHell/Kangaroo-/blob/master/Bigdata/mapreduce/invertedIndex/InvertedIndex.java)
    
* MapReduce原理

    [MapTask工作机制](https://github.com/bigDataHell/Kangaroo-/blob/master/Bigdata/mapreduce/mapReduce_principle/MapTask%E5%B7%A5%E4%BD%9C%E6%9C%BA%E5%88%B6.md)<br> 
    [ReduceTask工作机制](https://github.com/bigDataHell/Kangaroo-/blob/master/Bigdata/mapreduce/mapReduce_principle/ReduceTask%E5%B7%A5%E4%BD%9C%E6%9C%BA%E5%88%B6.md)
    

#### flume

#### kafka

   * [Kafka面试题](https://github.com/bigDataHell/Kangaroo-/blob/master/Bigdata/kafka/kafka%E9%9D%A2%E8%AF%95%E9%A2%98.md) <br>
   * [Kafka原理架构](https://github.com/bigDataHell/Kangaroo-/blob/master/Bigdata/kafka/kafka%E5%8E%9F%E7%90%86.md) <br>
   * [Kafka控制台命令](https://github.com/bigDataHell/Kangaroo-/blob/master/Bigdata/kafka/kafkak%E6%8E%A7%E5%88%B6%E5%8F%B0%E5%91%BD%E4%BB%A4.md) <br>
   * [KafkaAPI](https://github.com/bigDataHell/Kangaroo-/blob/master/Bigdata/kafka/API.md) 

#### storm

* [storm原理](https://github.com/bigDataHell/Kangaroo-/blob/master/Bigdata/storm/storm.md)

* 案例 : WordCount

    [WordCount](https://github.com/bigDataHell/Kangaroo-/tree/master/Bigdata/storm/case/WordCountDemo)

* 案例 : 实时交易数据统计

    [storm代码](https://github.com/bigDataHell/Kangaroo-/tree/master/Bigdata/storm/case/Real-timeTransactionDataStatistics/Storm) <br>
    [kafka代码](https://github.com/bigDataHell/Kangaroo-/tree/master/Bigdata/storm/case/Real-timeTransactionDataStatistics/kafka) <br>
    [案例分析](https://github.com/bigDataHell/Kangaroo-/blob/master/Bigdata/storm/case/Real-timeTransactionDataStatistics)

* 案例 : 实时日志监控告警系统

    [案例分析](https://github.com/bigDataHell/Kangaroo-/tree/master/Bigdata/storm/case/Real-timeLogMonitoringAlarmSystem)  <br>
  

#### Hbase

* [Hbase安装和控制台命令](https://github.com/bigDataHell/Kangaroo-/blob/master/Bigdata/Hbase/Hbase%E5%AE%89%E8%A3%85.md)  <br>

#### Elasticsearch

* [Elasticsearch概念](https://github.com/bigDataHell/Kangaroo-/tree/master/Bigdata/Elasticsearch)
* [Elasticsearch和Jackson整合,IK分词器](https://github.com/bigDataHell/Kangaroo-/blob/master/Bigdata/Elasticsearch/API-searchs.md)
* [Elasticsearch连接客户端,CRUD](https://github.com/bigDataHell/Kangaroo-/blob/master/Bigdata/Elasticsearch/API-CRUD.md)
* [Elasticsearch所有的搜索方法](https://github.com/bigDataHell/Kangaroo-/blob/master/Bigdata/Elasticsearch/API%E5%90%84%E7%A7%8D%E6%9F%A5%E8%AF%A2.md)

--------------------------------

## java
 
#### 设计模式 
  * [单例模式]()
  * [代理模式](https://github.com/bigDataHell/Kangaroo-/blob/master/java/designMode/%E4%BB%A3%E7%90%86%E6%A8%A1%E5%BC%8F.md)
#### java机制

 * [java回调机制]()
 
#### 数据库

 * [SQL](https://github.com/bigDataHell/Kangaroo-/tree/master/java/database/SQL)
 
#### Maven

* [maven介绍](https://github.com/bigDataHell/Kangaroo-/tree/master/java/Maven)

--------------------------------
## liunx

#### 一键启动脚本
 * [zookeeeper启动脚本](https://github.com/bigDataHell/Kangaroo-/blob/master/Liunx/kafka%E5%90%AF%E5%8A%A8%E8%84%9A%E6%9C%AC.md)
 * [kafka启动脚本](https://github.com/bigDataHell/Kangaroo-/blob/master/Liunx/kafka%E5%90%AF%E5%8A%A8%E8%84%9A%E6%9C%AC.md)
--------------------------------
## 网络编程

#### http

* [HTTP知识点](https://github.com/bigDataHell/Kangaroo-/tree/master/NetworkProgramming/HTTP)

--------------------------------
## 常用软件

[软件介绍](https://github.com/bigDataHell/Kangaroo-/tree/master/tools)

--------------------------------
## 问题总结

#### 异常

* [java.lang.NoClassDefFoundError --Maven-scope](https://github.com/bigDataHell/Kangaroo-/blob/master/Exception/%E5%B8%B8%E8%A7%81%E5%BC%82%E5%B8%B8.md#javalangnoclassdeffounderror----maven)

* [java.lang.StackOverflowError](https://github.com/bigDataHell/Kangaroo-/blob/master/Exception/%E5%B8%B8%E8%A7%81%E5%BC%82%E5%B8%B8.md#2-javalangstackoverflowerror-%E5%A0%86%E6%A0%88%E6%BA%A2%E5%87%BA%E9%94%99%E8%AF%AF)

* [ mysql 8 plugin caching_sha2_password could not be loaded](https://github.com/bigDataHell/Kangaroo-/blob/master/Exception/%E5%B8%B8%E8%A7%81%E5%BC%82%E5%B8%B8.md#mysql--mysql-8-plugin-caching_sha2_password-could-not-be-loaded)
--------------------------------
## 数据结构与算法

#### 快排

  * [快排](https://github.com/bigDataHell/Kangaroo-/blob/master/dataStructures_algorithms/Quicksort.md)


--------------------------------
## Scala

* [Scala 基础,方法,函数,数组,集合,元组,映射](https://github.com/bigDataHell/Kangaroo-/tree/master/Scala) 
* [Scala 类 对象继承 特质](https://github.com/bigDataHell/Kangaroo-/blob/master/Scala/Scala-%E7%B1%BB-%E5%AF%B9%E8%B1%A1.md)
* [Scala 模式匹配和样例类](https://github.com/bigDataHell/Kangaroo-/blob/master/Scala/Scala-%E6%A8%A1%E5%BC%8F%E5%8C%B9%E9%85%8D%E5%92%8C%E6%A0%B7%E4%BE%8B%E7%B1%BB.md)
* [Scala中面向对象编程之trait](https://github.com/bigDataHell/Kangaroo-/blob/master/Scala/Scala-trait.md)
* [Scala 协变,逆变,非变 上下界](https://github.com/bigDataHell/Kangaroo-/blob/master/Scala/Scala-%E5%8D%8F%E5%8F%98-%E4%B8%8A%E4%B8%8B%E7%95%8C.md)

* [Scala Actor并发编程](https://github.com/bigDataHell/Kangaroo-/blob/master/Scala/ScalaActor%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B.md)

--------------------------------

## Docker

* [docker](https://github.com/bigDataHell/Kangaroo-/blob/master/Docker)

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

