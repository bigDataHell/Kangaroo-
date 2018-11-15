

# Sqoop的使用

[sqoop增量导入实践](https://www.jianshu.com/p/f202ee34d1e3)

* 重要Tip：

__生产环境中，为了防止主库被Sqoop抽崩，我们一般从备库中抽取数据。
一般RDBMS的导出速度控制在60-80MB/s，每个 map 任务的处理速度5-10MB/s 估算，即 -m 参数一般设置4-8，表示启动 4-8 个map 任务并发抽取。__

## 1 sqoop Job的使用

### 1.1 创建Job

``` shell
sqoop job  --create myjob  \
-- import \
--connect jdbc:mysql://192.168.1.7:3306/itheima_ssm \
--username root \
--password 13573765639 \
--target-dir /Mysql/SSM \
--table syslog   \
--m 1  

```
### 1.2 删除job

```
sqoop job --delete myjob
```

### 1.3 执行job

``` 
sqoop job --exec myjob
```

### 1.4 显示job的参数
```
sqoop job --show myjob
```

###  1.5 列出所有的job
```
sqoop job --list
```

## 2 将Mysql中的数据导入到HDFS中

###  2.1 全量导入



``` 
sqoop job  --create myjobss  \
-- import \
--connect jdbc:mysql://192.168.1.7:3306/itheima_ssm \
--username root \
--password 13573765639 \
--target-dir /Mysql/SSM \
--delete-target-dir \
--table syslog   \
--m 4               
``` 
__注意： m 4 如果大于1的话，你设置的数就是分区数，就会产生几个文件，避免产生数据倾斜__

### 2.2 Append模式实战增量导入

* Append模式处理不了更新数据

```
sqoop job  --create append_5 \
-- import \
--connect jdbc:mysql://192.168.1.7:3306/itheima_ssm \
--username root \
--password 13573765639 \
--target-dir /Mysql/SSM \
--table syslog \
--check-column id \
--incremental append \
--last-value 55 \
--m 1
``` 

__注意：id为55的这一条数据并不会被追加到HDFS中__

##  2.3 使用Lastmodified（时间戳）模式

* Lastmodified模式可以更新数据

``` 
sqoop import \
--connect jdbc:mysql://192.168.1.7:3306/itheima_ssm \
   --username root \
   --password 13573765639 \
   --target-dir /Mysql/SSM \
   --split-by id \
   -m 4  \
   --incremental lastmodified \
   --merge-key id \
   --check-column time \
   --last-value “2018-11-15 22:36:12”  
``` 
__注意 ：- -split-by 参数指定根据哪一列来实现哈希分片，从而将不同分片的数据分发到不同 map 任务上去跑，避免数据倾斜。__


sqoop import: SQOOP 命令，从关系型数据库导数到Hadoop 
–check-column: 必须是timestamp列 
–incremental lastmodified: 设置为最后改动模式 
–merge-key: 必须是唯一主键 
–last-value: 所有大于最后一个时间的数据都会被更新



# 问题 
## 1 解决sqoop需要输入密码的问题
    修改配置文件：vi /etc/sqoop/conf/sqoop-site.xml
    
```
<property>
    <name>sqoop.metastore.client.record.password</name>
    <value>true</value>
    <description>If true, allow saved passwords in the metastore.
    </description>
</property>
```
    
