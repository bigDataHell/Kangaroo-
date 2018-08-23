# Kangaroo-

# 日常工作总结
 * 换行 \<br\>
 * 单行文本：前面使用两个Tab
 * 多行文本: 每行行首加两个Tab

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
|#|[Kafka](#kafka)|[mapreduce](#数据计算)|#|#|SQL|[algorithms](#algorithms)|[Exception](#exception)|[一键启动脚本](#一键启动脚本)|
|Hbase|#|hadoop|zookeeper|Mesos|#|#||
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
   


-------------------------


## 数据转换

 ### Kafka
 
   * [Kafka面试题](https://github.com/bigDataHell/Kangaroo-/blob/master/kafka/kafka%E9%9D%A2%E8%AF%95%E9%A2%98.md)
   * [Kafka原理架构](https://github.com/bigDataHell/Kangaroo-/blob/master/kafka/kafka%E5%8E%9F%E7%90%86.md)
   
--------------------------------
 ## 数据结构与算法
   
  ### Algorithms
  
  * [快排](https://github.com/bigDataHell/Kangaroo-/blob/master/dataStructures_algorithms/Quicksort.md)
  
  
---------------------------------------------------------
## Exception

[java.lang.NoClassDefFoundError --Maven-scope](https://github.com/bigDataHell/Kangaroo-/blob/master/Exception/%E5%B8%B8%E8%A7%81%E5%BC%82%E5%B8%B8.md#javalangnoclassdeffounderror----maven)
   
------------------------------------------------------

## 一键启动脚本

 [kafka启动脚本](https://github.com/bigDataHell/Kangaroo-/blob/master/Liunx/kafka%E5%90%AF%E5%8A%A8%E8%84%9A%E6%9C%AC.md)

