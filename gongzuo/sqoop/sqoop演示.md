

# 1 sqoop命令

https://blog.csdn.net/qq_15103205/article/details/72903894

* 显示数据库中的所有数据库

`sqoop list-databases --connect jdbc:mysql://localhost:3306/ -username root -password 123456`

# import

## 2 导入mysql表数据到HDFS


#### 2.1 不指定分割的字符 默认 "," 号
``` 
sqoop import \
--connect jdbc:mysql://hadoop-node-1:3306/userdb \
--username root \
--password 123456 \
--target-dir /sqoopMYAPP \
--table emp --m 1
``` 

#### 2.2 指定分割的字符

    * --fields-terminated-by "\t" : 指定分割的字符
    * --delete-target-dir \ : 如果目标文件夹存在则删除
    * --columns name,deg : 指定字段
    * -- m 1 : 设定map个数
``` 
sqoop import \
--connect jdbc:mysql://hadoop-node-1:3306/userdb \
--username root \
--password 123456 \
--target-dir /sqoopMYAPPnew \
--delete-target-dir \
--table emp --m 1 \
--columns name,deg \
--fields-terminated-by "\t"  \
--where "id > 1203"
``` 

#### 2.3 增量导入HDFS(未验证)
 
 * 增量导入数据到HDFS文件中，可以通过下面三个参数进行设置：
    * --check-column
    * --incremental
    * --last-value
    
```
sqoop import \
--connect jdbc:mysql://hostname:3306/userdb \
--username root \
--password 123456 \
--table emp \
--num-mappers 1 \
--target-dir /sqoop1111 \
--fields-terminated-by "\t" \
--check-column id \
--incremental append \
--last-value 1202     //表示从第5位开始导入
```

#### 2.4 把select查询结果导入HDFS

把select查询结果导入HDFS，必须包含'$CONDITIONS'在where子句中；

```
sqoop import \
--connect jdbc:mysql://hadoop-node-1:3306/userdb \
--username root \
--password 123456 \
--target-dir /sqoopMYAPPnew \
--delete-target-dir \
--m 1 \
--fields-terminated-by "\t"  \
--query 'select id,dept from emp where id > 1203 and $CONDITIONS'
```

## 3 数据导入到hive中

``` 
sqoop import \
--connect jdbc:mysql://hostname:3306/mydb \
--username root \
--password root \
--table mytable \
--num-mappers 1 \
--hive-import \
--hive-database mydb \
--hive-table mytable \
--fields-terminated-by "\t" \
--delete-target-dir \
--hive-overwrite 
``` 


# export

```
sqoop export \
--connect jdbc:mysql://hostname:3306/mydb \
--username root \
--password root \
--table mytable \
--num-mappers 1 \
--export-dir /user/hive/warehouse/mydb.db/mytable \
--input-fields-terminated-by "\t"
``` 
