## 1 上传文件

* 1.1 把hbase-1.3.1-bin.tar.g安装包上传到/export/server目录下

* 1.2 解压压缩包

`tar -zxvf hbase-1.3.1-bin.tar.gz`

* 1.3 修改文件名

`mv hbase-1.3.1 hbase`

* 1.4 配置环境变量

vi /etc/profile
\-
export HBASE_HOME=/export/server/hbase
export PATH=${HBASE_HOME}/bin:$PATH
\-
source /etc/profile

## 2 修改配置文件

* 2.1 进入配置文件所在的目录
cd /export/servers/hbase/conf/

* 2.2  修改第一个配置文件  regionservers 

      hadoop-node-1
      hadoop-node-2
      hadoop-node-3
* 2.3  修改第二个配置文件 hbase-site.xml 

> 注意：以下配置集成的是hadoop ha集群。
如果您的集群没有配置ha，hbase.rootdir 配置项目需要修改：hdfs://master:9000/hbase

``` xml
<configuration>
  <property>
    <name>hbase.rootdir</name>
    <value>hdfs://ns1/hbase</value>
  </property>
  <property>
     <name>hbase.cluster.distributed</name>
     <value>true</value>
  </property>
  <property>
     <name>hbase.master.port</name>
     <value>16000</value>
  </property>
   <property>
    <name>hbase.zookeeper.property.dataDir</name>
    <value>/export/data/zk/</value>
  </property>
  <property>
    <name>hbase.zookeeper.quorum</name>
    <value>hadoop-node-1,hadoop-node-2,hadoop-node-3</value>
  </property>
  <property>
    <name>hbase.zookeeper.property.clientPort</name>
    <value>2181</value>
  </property>
</configuration>
```
* 2.3   修改第三个配置文件 hbase-env.sh

HBASE_MANAGES_ZK=false 表示，hbase和大家伙公用一个zookeeper集群，而不是自己管理集群。

在配置文件中加入:
\-
export JAVA_HOME=/export/server/jdk1.8.0_65
export HBASE_MANAGES_ZK=false
\-

* 2.4 修改第四个配置文件 拷贝hadoop配置文件

拷贝hadoop的配置文件到hbase的配置文件目录

`scp /export/server/hadoop-2.7.4/etc/hadoop/hdfs-site.xml  .`

* 2.5 把Hbane分发到其他节点

 `scp -r  hbase hadoop-node-3:/export/server`
 
 ## 启动集群
 
 启动:
 
 `start-hbase.sh`
 
 停止:
 
  `stop-hbase.sh`
 
 主节点:
 
 ![Hbase01](https://github.com/bigDataHell/Kangaroo-/tree/master/images/Hbase01.png)
 
 从节点 : 
 
  ![Hbase02](https://github.com/bigDataHell/Kangaroo-/tree/master/images/Hbase02.png)
  
  搭建成功!!!!
 
 
 



