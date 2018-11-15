

# Sqoop的使用

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

```
sqoop import \
--connect jdbc:mysql://192.168.1.7:3306/itheima_ssm \
--username root \
--password 13573765639 \
--table syslog \
--check-column id \  # 递增列（int）
--incremental append \ #基于递增列的增量导入（将递增列值大于阈值的所有数据增量导入Hadoop）
--last-value 35 # 阈值（int)
```


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
    
