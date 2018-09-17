

## 1 client上传文件的流程

* 1 client发起上传文件的请求,通过RPC与NameNode建立连接,NameNode检查目标文件和父目录是否存在,返回client是否可以上传文件.
* 2 client请求NameNode把文件的第一个块上传的那些DataNode服务器上.
* 3 NameNode根据配置文件的副本机制和机架感知原理来分配文件.返回可用的DataNode服务器的地址: 比如a b c 
* 4 client选择3台DataNode服务器中的A请求连接(实质就是通过RPC调用,建立pipeline管道),A节点就受到请求,会去请求b,b接收到请求在请求c这样整个管道就建立完毕,然后逐级返回给client.
* 5 client开始往A节点上传文件的第一个块(先从磁盘读取数据到本地内存缓存),以包的为单位(64k)上传,A节点每接受一个包,就会存储在发给B节点,B节点在本地存储一份在发个C节点.A节点每发送一个包,就会添加一个应答队列等待应答.
* 6 数据被分割成一个个packet数据包在管道上依次传输,在管道反方向上,逐个发送ack(命令正确应答),最终由管道上的第一个DataNode节点A将 pipeline ACK 发送给 client
* 7 当一个block上传完毕,client会再次请求NameNode上传第二个块到服务器上

##  Shuffle机制

* map阶段处理的数据如何传递给reduce 阶段，是MapReduce 框架中最关键
的一个流程，这个流程就叫shuffle。

* shuffle: 洗牌、发牌——（核心机制：数据分区，排序，合并）。


##  MapReduce的工作机制

* 1 读取数据的组件InputFormat(默认TextInputFormat)会通过getSplits方法对输入目录中的文件进行逻辑切片规划得到splits,有多少个切片就对应启动多少个MapTast,默认按照128M切
* 2 将输入文件切片之后,由 RecordReader对象(默认LineRecordReader）进行读取，以\n 作为分隔符，读取一行数据，返回<key，
   value>。Key 表示每行首字符偏移值，value 表示这一行文本内容
   
* 3 读取 split 返回<key,value>，进入用户自己继承的 Mapper 类中，执行用户重写的 map 函数。RecordReader 读取一行这里调用一次。
* 4 map处理结束之后,将map处理后的每条结果交给了OutputConllect收集器,在收集器中,会对其中的数据进行分区处理,默认分区为key hash以后在按照educe Task的数量取模.
* 5 将数据写入内存环形缓冲区中,缓冲区的作用为批量收集Map的结果,减少磁盘IO的影响,缓冲区默认为100M
* 6 当缓冲区中的数据达到80M时,会启动溢出线程,先对这80M的数据按照key进行排序,如果Job设置了combiner,则这个时候combiner会执行.
* 7 每次溢写都会在磁盘中生成一个临时文件,如果又多次溢出,就会有多个临时文件,当整个数据处理结束之后开始对磁盘中的临时文件进行merge合并,最终只有一个文件写入磁盘,平且为这个文件提供类索引文件,以记录每个 reduce 对应数据的偏移量


### ReduceTask工作机制

* 1 copy阶段  简单地拉取数据。Reduce 进程启动一些数据 copy 线程(Fetcher)，通过 HTTP 方式请求 maptask 获取属于自己的文件。

 
## 2 hadoop有几种运行模式?

* 伪分布式   在 1 个机器上运行 HDFS 的 NameNode 和 DataNode、YARN 的ResourceManger 和 NodeManager，
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

##  9 HDFS的架构

master/slave  架构
HDFS 采用 master/slave 架构。一般一个 HDFS 集群是有一个 Namenode 和一
定数目的 Datanode 组成。Namenode 是 HDFS 集群主节点，Datanode 是 HDFS 集群
从节点，两种角色各司其职，共同协调完成分布式的文件存储服务。

## NameNode的作用

 我们把目录结构及文件分块位置信息叫做元数据。Namenode 负责维护整个
hdfs 文件系统的目录树结构，以及每一个文件所对应的 block 块信息（block 的
id，及所在的 datanode 服务器）。

## Datanode  数据存储

文件的各个 block 的具体存储管理由 datanode 节点承担。每一个 block 都
可以在多个 datanode 上。Datanode 需要定时向 Namenode 汇报自己持有的 block
信息。
存储多个副本（副本数量也可以通过参数设置 dfs.replication，默认是 3）。









