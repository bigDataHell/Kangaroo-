# Spark SQL

## 1 Spark SQL  概述 

### 1.1 什么是Spark SQL?

Spark SQL是Spark用来处理结构化数据的一个模块，它提供了一个编程抽象叫做DataFrame并且作为分布式SQL查询引擎的作用。

相比于Spark RDD API，Spark SQL包含了对结构化数据和在其上运算的更多信息，Spark SQL使用这些信息进行了额外的优化，使对结构化数据的操作更加高效和方便。

有多种方式去使用Spark SQL，包括SQL、DataFrames API和Datasets API。但无论是哪种API或者是编程语言，它们都是基于同样的执行引擎，因此你可以在不同的API
之间随意切换，它们各有各的特点，看你喜欢那种风格。

### 1.2 Spark SQL 的四大特性

* 1 易整合 : 将sql查询与spark程序无缝混合，可以使用java、scala、python、R等语言的API操作。

* 2 统一的数据访问 : 以相同的方式连接到任何数据源。

* 3 兼容Hive : 支持hiveSQL的语法

* 4 标准的数据连接 : 可以使用行业标准的JDBC或ODBC连接。

## 2 DataFrame

### 2.1 什么是DataFrame

DataFrame的前身是SchemaRDD，从Spark 1.3.0开始SchemaRDD更名为DataFrame。与SchemaRDD的主要区别是：DataFrame不再直接继承自RDD，
而是自己实现了RDD的绝大多数功能。你仍旧可以在DataFrame上调用rdd方法将其转换为一个RDD。

在Spark中，DataFrame是一种以RDD为基础的分布式数据集，类似于传统数据库的二维表格，DataFrame带有Schema元信息，即DataFrame所表示的二维表数据集的每
一列都带有名称和类型，但底层做了更多的优化。DataFrame可以从很多数据源构建，比如：已经存在的RDD、结构化文件、外部数据库、Hive表。

一个 DataFrame 是一个 Dataset 组织成的指定列。它的概念与一个在关系型数据库或者在 R/Python 中的表是相等的，但是有更多的优化。DataFrame 可以从大量的
Source 中构造出来，像 : 结构化的数据文件，Hive 中的表，外部的数据库，或者已存在的 RDD。DataFrame API 在 Scala，Java，Python 和 R 中是可用的。
在 Scala 和 Java 中，一个 DataFrame 所代表的是一个多个 Row（行）的 Dataset。在 Scala API 中，DataFrame 仅仅是一个 Dataset[Row] 类型的别名 。
然而，在 Java API 中，用户需要去使用 Dataset<Row> 来表示 DataFrame。
  
  
### 2.2 DataFrame与RDD的优缺点

RDD的优缺点： <br>
优点: <br>
（1）编译时类型安全  <br>
		编译时就能检查出类型错误 <br>
（2）面向对象的编程风格  <br>
		直接通过对象调用方法的形式来操作数据<br>
缺点:<br>
（1）序列化和反序列化的性能开销 <br>
		无论是集群间的通信, 还是IO操作都需要对对象的结构和数据进行序列化和反序列化。<br>
（2）GC的性能开销 <br>
		频繁的创建和销毁对象, 势必会增加GC<br>
    
DataFrame通过引入schema和off-heap（不在堆里面的内存，指的是除了不在堆的内存，使用操作系统上的内存），解决了RDD的缺点, Spark通过schame就能够读懂数据, 因此在通信和IO时就只需要序列化和反序列化数据, 而结构的部分就可以省略了；通过off-heap引入，可以快速的操作数据，避免大量的GC。但是却丢了RDD的优点，DataFrame不是类型安全的, API也不是面向对象风格的。

### 2.3  读取数据源创建DataFrame

在spark2.0版本之前，Spark SQL中SQLContext是创建DataFrame和执行SQL的入口，可以利用hiveContext通过hive sql语句操作hive表数据，兼容hive操作，并且hiveContext继承自SQLContext。在spark2.0之后，这些都统一于SparkSession，SparkSession 封装了 SparkContext，SqlContext，通过SparkSession可以获取到SparkConetxt,SqlContext对象。

#### 2.3.1 读取text文件
```  scala 
scala> val rdd1 = sc.textFile("/user/spark/people.txt").map(_.split(" "))
rdd1: org.apache.spark.rdd.RDD[Array[String]] = MapPartitionsRDD[2] at map at <console>:24

scala> case class people(id:Int,name:String,age:Int)
defined class people

scala> val peopleRDD = rdd1.map(x => people(x(0).toInt,x(1),x(2).toInt))
peopleRDD: org.apache.spark.rdd.RDD[people] = MapPartitionsRDD[3] at map at <console>:28

scala> val peopleDF = peopleRDD.toDF
peopleDF: org.apache.spark.sql.DataFrame = [id: int, name: string ... 1 more field]

scala> peopleDF.collect
res1: Array[org.apache.spark.sql.Row] = Array([1,zhangsan,20], [2,lisi,56], [3,dabai,34], [4,xiaobai,45], [5,wangwu,21], [6,top,1])

scala> peopleDF.show
+---+--------+---+
| id|    name|age|
+---+--------+---+
|  1|zhangsan| 20|
|  2|    lisi| 56|
|  3|   dabai| 34|
|  4| xiaobai| 45|
|  5|  wangwu| 21|
|  6|     top|  1|
+---+--------+---+

scala> peopleDF.printSchema
root
 |-- id: integer (nullable = true)
 |-- name: string (nullable = true)
 |-- age: integer (nullable = true)
 
 ``` 
 
 
* 通过SparkSession构建DataFrame,使用spark-shell中已经初始化好的SparkSession对象spark生成DataFrame

```  scala 
scala> spark.read.text("/user/spark/people.txt")
res5: org.apache.spark.sql.DataFrame = [value: string]

scala> res5.show
+-------------+
|        value|
+-------------+
|1 zhangsan 20|
|    2 lisi 56|
|   3 dabai 34|
| 4 xiaobai 45|
|  5 wangwu 21|
|      6 top 1|
+-------------+                 

scala> res5.printSchema
root
 |-- value: string (nullable = true)

``` 
#### 2.3.2  读取json文件创建DataFrame

``` scala 
scala> val df1 = spark.read.json("file:///export/server/spark/examples/src/main/resources/people.json")
df1: org.apache.spark.sql.DataFrame = [age: bigint, name: string]

scala> df1.show
+----+-------+
| age|   name|
+----+-------+
|null|Michael|
|  30|   Andy|
|  19| Justin|
+----+-------+


scala> df1.printSchema
root
 |-- age: long (nullable = true)
 |-- name: string (nullable = true)

``` 

#### 2.3.3 读取parquet列式存储格式文件创建DataFrame

``` scala

scala> val df2 = spark.read.parquet("file:///export/server/spark/examples/src/main/resources/users.parquet")
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
df2: org.apache.spark.sql.DataFrame = [name: string, favorite_color: string ... 1 more field]


scala> df2.show
18/09/18 14:35:46 WARN hadoop.ParquetRecordReader: Can not initialize counter due to context is not a instance of TaskInputOutputContext, but is org.apache.hadoop.mapreduce.task.TaskAttemptContextImpl
+------+--------------+----------------+
|  name|favorite_color|favorite_numbers|
+------+--------------+----------------+
|Alyssa|          null|  [3, 9, 15, 20]|
|   Ben|           red|              []|
+------+--------------+----------------+


scala> df2.printSchema
root
 |-- name: string (nullable = true)
 |-- favorite_color: string (nullable = true)
 |-- favorite_numbers: array (nullable = true)
 |    |-- element: integer (containsNull = true)

```


## 3 DataFrame常用操作

### 3.1 DSL风格语法

DataFrame提供了一个领域特定语言(DSL)来操作结构化数据。

* 拿到某一列的值

``` scala
scala> peopleDF.select("name").show
+--------+
|    name|
+--------+
|zhangsan|
|    lisi|
|   dabai|
| xiaobai|
|  wangwu|
|     top|
+--------+


scala> peopleDF.select("name","age").show
+--------+---+
|    name|age|
+--------+---+
|zhangsan| 20|
|    lisi| 56|
|   dabai| 34|
| xiaobai| 45|
|  wangwu| 21|
|     top|  1|
+--------+---+

```

* 查询所有的name和age，并将age+1

``` scala
scala> peopleDF.select($"name",$"age"+1).show
+--------+---------+
|    name|(age + 1)|
+--------+---------+
|zhangsan|       21|
|    lisi|       57|
|   dabai|       35|
| xiaobai|       46|
|  wangwu|       22|
|     top|        2|
+--------+---------+
```
* 过滤age大于等于25的，使用filter方法过滤
``` scala 
scala> peopleDF.filter($"age" > 25)
res23: org.apache.spark.sql.Dataset[org.apache.spark.sql.Row] = [id: int, name: string ... 1 more field]

scala> res23.show
+---+-------+---+
| id|   name|age|
+---+-------+---+
|  2|   lisi| 56|
|  3|  dabai| 34|
|  4|xiaobai| 45|
+---+-------+---+

```

* 统计年龄大于25的人数

``` scala
scala> peopleDF.filter($"age" > 25).count
res25: Long = 3
```
* 按年龄进行分组并统计相同年龄的人数

``` scala
scala> peopleDF.sort("age").groupBy("age").count.show
+---+-----+                                                                     
|age|count|
+---+-----+
|  1|    1|
| 20|    1|
| 21|    1|
| 34|    1|
| 45|    1|
| 56|    1|
+---+-----+

```
### 3.2 sql风格的语法

DataFrame的一个强大之处就是我们可以将它看作是一个关系型数据表，然后可以通过在程序中使用spark.sql() 来执行SQL语句查询，结果返回一个DataFrame。
如果想使用SQL风格的语法，需要将DataFrame注册成表,采用如下的方式：

`personDF.registerTempTable("t_person")`


``` scala 

scala> peopleDF.registerTempTable("t_people")
warning: there was one deprecation warning; re-run with -deprecation for details

scala> 
scala> spark.sql("select * from t_people")
res32: org.apache.spark.sql.DataFrame = [id: int, name: string ... 1 more field]

scala> spark.sql("select * from t_people").show
+---+--------+---+
| id|    name|age|
+---+--------+---+
|  1|zhangsan| 20|
|  2|    lisi| 56|
|  3|   dabai| 34|
|  4| xiaobai| 45|
|  5|  wangwu| 21|
|  6|     top|  1|
+---+--------+---+
```


