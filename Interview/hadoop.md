

## 1 client上传文件的循序

 *  1 client向NameNode发起文件写入的请求.
 *  2 NameNode根据上传的文件大小和文件块的配置,返回给Client他所管理的部分DataNode的信息.
 *  3 Client将文件划分为多个块,根据DataNode的地址信息,按循序写入到没一个DataNode中.
 
## 2 hadoop有几种运行模式?

* 未分布式   在 1 个机器上运行 HDFS 的 NameNode 和 DataNode、YARN 的ResourceManger 和 NodeManager，
* 单机模式   仅 1 个机器运行 1 个 java 进程，主要用于调试。
* 分布式 

## 3 SecondaryNameNode  的作用

辅助后台程序，与NameNode进行通信，以便定期保存HDFS元数据的快照。

## 4 请列出正常工作的hadoop集群中hadoop都需要启动哪些进程，他们的作用分别是什么？

1) NameNode: HDFS的守护进程，负责记录文件是如何分割成数据块，以及这些数据块分别被存储到那些数据节点上，它的主要功能是对内存及IO进行集中管理

2) Secondary NameNode：辅助后台程序，与NameNode进行通信，以便定期保存HDFS元数据的快照。
3) DataNode：负责把HDFS数据块读写到本地的文件系统。
4) JobTracker：负责分配task，并监控所有运行的task。
5) TaskTracker：负责执行具体的task，并与JobTracker进行交互。

## 5 hive有哪些方式保存元数据，各有哪些特点？

1) 内存数据库derby，较小，不常用
2) 本地mysql，较常用
3) 远程mysql，不常用

## 6 map-reduce程序运行的时候会有什么比较常见的问题
比如说作业中大部分都完成了，但是总有几个reduce一直在运行
这是因为这几个reduce中的处理的数据要远远大于其他的reduce，可能是因为对键值对任务划分的不均匀造成的数据倾斜
解决的方法可以在分区的时候重新定义分区规则对于value数据很多的key可以进行拆分、均匀打散等处理，或者是在map端的combiner中进行数据预处理的操作

##  7 请简述hadoop怎么样实现二级排序？


在Hadoop中，默认情况下是按照key进行排序，如果要按照value进行排序怎么办？
有两种方法进行二次排序，分别为：buffer and in memory sort和 value-to-key conversion。
buffer and in memory sort
主要思想是：在reduce()函数中，将某个key对应的所有value保存下来，然后进行排序。 这种方法最大的缺点是：可能会造成out of memory。
 
value-to-key conversion
主要思想是：将key和部分value拼接成一个组合key（实现WritableComparable接口或者调setSortComparatorClass函数），
这样reduce获取的结果便是先按key排序，后按value排序的结果，需要注意的是，用户需要自己实现Paritioner，以便只按照key进行数据划分。
Hadoop显式的支持二次排序，在Configuration类中有个setGroupingComparatorClass()方法，可用于设置排序group的key值

## 8 请简述mapreduce中，combiner，partition作用？

## HDFS的架构

master/slave  架构
HDFS 采用 master/slave 架构。一般一个 HDFS 集群是有一个 Namenode 和一
定数目的 Datanode 组成。Namenode 是 HDFS 集群主节点，Datanode 是 HD








