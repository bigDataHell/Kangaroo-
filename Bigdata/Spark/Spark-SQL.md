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

### 2.3 读取数据源创建DataFrame

在spark2.0版本之前，Spark SQL中SQLContext是创建DataFrame和执行SQL的入口，可以利用hiveContext通过hive sql语句操作hive表数据，兼容hive操作，并且hiveContext继承自SQLContext。在spark2.0之后，这些都统一于SparkSession，SparkSession 封装了 SparkContext，SqlContext，通过SparkSession可以获取到SparkConetxt,SqlContext对象。

``` 
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

``` 
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








