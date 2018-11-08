

# apache sqoop 详解

Sqoop 是`Hadoop`和`关系数据库服务器`之间传送数据的一种工具。它是用来从关系数据
库如：MySQL，Oracle 到 Hadoop 的 HDFS，并从 Hadoop 的文件系统导出数据到关系数据
库。由 Apache 软件基金会提供。

![Sqoop](https://github.com/bigDataHell/Kangaroo-/blob/master/images/Sqoop.png)


__Sqoop 工作机制是将导入或导出命令翻译成 mapreduce 程序来实现。
在翻译出的 mapreduce 中主要是对 inputformat 和 outputformat 进行定制__


## sqoop  安装

安装 sqoop 的前提是已经具备 java 和 hadoop 的环境。
最新稳定版： 1.4.6

配置文件修改：
``` shell
cd $SQOOP_HOME/conf
mv sqoop-env-template.sh sqoop-env.sh
vi sqoop-env.sh
export HADOOP_COMMON_HOME=/root/apps/hadoop/
export HADOOP_MAPRED_HOME=/root/apps/hadoop/
export HIVE_HOME=/root/apps/hive
```

加入 mysql 的 jdbc 驱动包

``` shell
cp /hive/lib/mysql-connector-java-5.1.28.jar $SQOOP_HOME/lib/
```

验证启动
``` shell
bin/sqoop list-databases --connect jdbc:mysql://localhost:3306/ --
username root --password hadoop
```
本命令会列出所有 mysql 的数据库。
到这里，整个 Sqoop 安装工作完成。

## Sqoop  导入

注意 : 我们要知道数据是从哪里导入到哪里!

  __导入 : 是把数据从关系型数据库到导入到HDFS系统中或者Hbase中b.<br>
  导出 : 是把数据从HDFS存储系统中导出到关系型数组库中.__

“导入工具”导入单个表从 RDBMS 到 HDFS。表中的每一行被视为 HDFS 的记录。所有记
录都存储为文本文件的文本数据（或者 `Avro`、`sequence` 文件等二进制数据）。

下面的语法用于将数据导入 HDFS。

`$ sqoop import (generic-args) (import-args)`

Sqoop 测试表数据
在 mysql 中创建数据库 userdb，然后执行参考资料中的 sql 脚本：
创建三张表: emp emp_add emp_conn。
2.1． ．  导入 mysql  表数据到 HDFS
下面的命令用于从 MySQL 数据库服务器中的 emp 表导入 HDFS。
bin/sqoop import \
--connect jdbc:mysql://node-21:3306/sqoopdb \
--username root \
--password hadoop \
--target-dir /sqoopresult \
--table emp --m 1
其中--target-dir 可以用来指定导出数据存放至 HDFS 的目录；
mysql jdbc url 请使用 ip 地址。
为了验证在 HDFS 导入的数据，请使用以下命令查看导入的数据：
hdfs dfs -cat /sqoopresult/part-m-00000
可以看出它会用逗号,分隔 emp 表的数据和字段。
北京市昌平区建材城西路金燕龙办公楼一层 电话：400-618-9090
1201,gopal,manager,50000,TP
1202,manisha,Proof reader,50000,TP
1203,khalil,php dev,30000,AC
1204,prasanth,php dev,30000,AC
1205,kranthi,admin,20000,TP
