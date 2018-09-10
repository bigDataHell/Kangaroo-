# Spark HA  高可用部署

## 1 高可用部署说明

Spark Standalone 集群是 Master-Slaves 架构的集群模式，和大部分的
Master-Slaves 结构集群一样，存在着 Master 单点故障的问题。如何解决这个
单点故障的问题，Spark 提供了两种方案：

（1）基于文件系统的单点恢复(Single-Node Recovery with Local File
System)。

主要用于开发或测试环境。当 spark 提供目录保存 spark Application
和 worker 的注册信息，并将他们的恢复状态写入该目录中，这时，一旦 Master
发生故障，就可以通过重新启动 Master 进程（sbin/start-master.sh），恢复
已运行的 spark Application 和 worker 的注册信息。


（2）基于 zookeeper 的 Standby Masters(Standby Masters with ZooKeeper)。

用于生产模式。其基本原理是通过 zookeeper 来选举一个 Master，其他
的 Master 处于 Standby 状态。将 spark 集群连接到同一个 ZooKeeper 实例并启
动多个 Master，利用 zookeeper 提供的选举和状态保存功能，可以使一个 Master
被选举成活着的 master，而其他 Master 处于 Standby 状态。如果现任 Master
死去，另一个 Master 会通过选举产生，并恢复到旧的 Master 状态，然后恢复调
度。整个恢复过程可能要 1-2 分钟。

## 2 基于 zookeeper 的 Spark HA  高可用集群部署

该 HA 方案使用起来很简单，首先需要搭建一个 zookeeper 集群，然后启动
zooKeeper 集群，最后在不同节点上启动 Master。具体配置如下：

(1)vim spark-env.sh

注释掉 export SPARK_MASTER_HOST=hdp-node-01

(2)在 spark-env.sh 添加 SPARK_DAEMON_JAVA_OPTS，内容如下：

export SPARK_DAEMON_JAVA_OPTS="-Dspark.deploy.recoveryMode=ZOOKEEPER
-Dspark.deploy.zookeeper.url=hdp-node-01:2181,hdp-node-02:2181,hdp-node-03:2181
-Dspark.deploy.zookeeper.dir=/spark"

参数说明

spark.deploy.recoveryMode：恢复模式（Master 重新启动的模式）

有三种：(1)ZooKeeper (2) FileSystem (3)NONE

spark.deploy.zookeeper.url：ZooKeeper 的 Server 地址

spark.deploy.zookeeper.dir：保存集群元数据信息的文件、目录。

包括 Worker，Driver 和 Application。

注意：

__在普通模式下启动 spark 集群，只需要在主机上面执行 start-all.sh 就可以了。 <br>
在高可用模式下启动 spark 集群，先需要在任意一台节点上启动 start-all.sh 命令。 <br>
然后在另外一台节点上单独启动 master。命令 start-master.sh__。
