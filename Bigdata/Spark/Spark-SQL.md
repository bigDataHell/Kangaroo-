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
## 4 DateSet 

### 4.1 什么是DataSet

DataSet是分布式的数据集合，Dataset提供了强类型支持，也是在RDD的每行数据加了类型约束。DataSet是在Spark1.6中添加的新的接口。它集中了RDD的优点（强类型和可以用强大lambda函数）以及使用了Spark SQL优化的执行引擎。DataSet可以通过JVM的对象进行构建，可以用函数式的转换（map/flatmap/filter）进行多种操作。

DataSet包含了DataFrame的功能，Spark2.0中两者统一，DataFrame表示为DataSet[Row]，即DataSet的子集。 <br>
（1）DataSet可以在编译时检查类型 <br>
（2）并且是面向对象的编程接口 <br>
相比DataFrame，Dataset提供了编译时类型检查，对于分布式程序来讲，提交一次作业太费劲了（要编译、打包、上传、运行），到提交到集群运行时才发现错误，这会浪费大量的时间，这也是引入Dataset的一个重要原因。

### 4.2 DataSet 与 DateFrame之间的转换

DataFrame和DataSet可以相互转化。

* （1）DataFrame转为 DataSet

`df.as[ElementType]` 这样可以把DataFrame转化为DataSet。

``` scala
scala> case class People(age:Long,name:String)
defined class People

scala> val df2 = df.as[People]
df2: org.apache.spark.sql.Dataset[People] = [age: bigint, name: string]

scala> df2.show
+----+-------+
| age|   name|
+----+-------+
|null|Michael|
|  30|   Andy|
|  19| Justin|
+----+-------+

```

* （2）DataSet转为DataFrame 

`ds.toDF()` 这样可以把DataSet转化为DataFrame。

### 4.3 创建DateSet

* （1）通过spark.createDataset创建
``` scala
scala> val ds1 = spark.createDataset(sc.textFile("/user/spark//people.txt"))
ds1: org.apache.spark.sql.Dataset[String] = [value: string]

scala> ds1.show
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
```

* （2）通toDS方法生成DataSet
``` scala
scala> case class People(name:String,age:Long)
defined class People

scala> val data = List(People("zhansan",13),People("lisi",34))
data: List[People] = List(People(zhansan,13), People(lisi,34))

scala> val dataset = data.toDS
dataset: org.apache.spark.sql.Dataset[People] = [name: string, age: bigint]

scala> dataset.show
+-------+---+
|   name|age|
+-------+---+
|zhansan| 13|
|   lisi| 34|
+-------+---+
```
* （3）通过DataFrame转化生成

## 5 以编程方式执行Spark SQL查询

### 5.1． 编写Spark SQL程序实现RDD转换成DataFrame

前面我们学习了如何在Spark Shell中使用SQL完成查询，现在我们通过IDEA编写Spark SQL查询程序。

Spark官网提供了两种方法来实现从RDD转换得到DataFrame，第一种方法是利用反射机制，推导包含某种类型的RDD，通过反射将其转换为指定类型的DataFrame，适用于提前知道RDD的schema。第二种方法通过编程接口与RDD进行交互获取schema，并动态创建DataFrame，在运行时决定列及其类型。

首先在maven项目的pom.xml中添加Spark SQL的依赖。

``` xml
    <dependency>
      <groupId>org.apache.spark</groupId>
      <artifactId>spark-core_2.11</artifactId>
      <version>2.0.2</version>
    </dependency>
<dependency>
    <groupId>org.apache.spark</groupId>
    <artifactId>spark-sql_2.11</artifactId>
    <version>2.0.2</version>
</dependency>
```

### 5.2 通过反射推断Schema

Scala支持使用case class类型导入RDD转换为DataFrame，通过case class创建schema，case class的参数名称会被利用反射机制作为列名。这种RDD可以高效的转换为DataFrame并注册为表。

``` scala
package cn.itcast.sql

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Column, DataFrame, SparkSession}

//todo:将rdd转换为DataFrame,利用反射机制---case class样例类
//定义一个样例类
case class Person(id:Int,name:String,age:Int)
object CaseClassSchema {
  def main(args: Array[String]): Unit = {
    //1、创建sparkSession
    val spark: SparkSession = SparkSession.builder().appName("CaseClassSchema").master("local[2]").getOrCreate()
    //2、获取sparkContext对象
    val sc: SparkContext = spark.sparkContext
    sc.setLogLevel("WARN")
    //3、通过sparkContext读取数据文件
    val data: RDD[String] = sc.textFile("D:\\wordcount\\input\\people.txt")
    //4、切分每一行
    val linesArrayRDD: RDD[Array[String]] = data.map(_.split(" "))
    //5、可以将linesArrayRDD跟样例类关联
    val personRDD: RDD[Person] = linesArrayRDD.map(x =>Person(x(0).toInt,x(1),x(2).toInt))
    //6、将personRDD转化为DataFrame
    //手动导入隐式转换
    import spark.implicits._
    val personDF: DataFrame = personRDD.toDF

    //---------------------DSL语法操作---------------start
    //打印dataframe中的schema信息
    personDF.printSchema()
    //打印dataframe中的数据
    personDF.show()
    //获取dataframe中的第一条记录
    println(personDF.head())
    //打印dataframe中的总记录数
    println(personDF.count())
    //打印dataframe中所有的字段名称
    personDF.columns.foreach(x=>println(x))
    //获取对应的name字段结果
    personDF.select("name").show()
    //获取对应的name字段结果
    personDF.select($"name").show()
    //获取对应的age字段结果
    personDF.select(new Column("age")).show()
    //把所有age字段的结果+1
    personDF.select($"id",$"name",$"age",$"age"+1).show()
    //过滤出age大于30的记录
    personDF.filter($"age" >30).show()
    println(personDF.filter($"age" >30).count())
    //按照对应的age进行分组，统计每一个年龄出现的次数
    personDF.sort("age").groupBy("age").count().show()
    //---------------------DSL语法操作---------------end

    //---------------------SQL语法操作---------------start
    //需要将dataFrame注册成一张表
    personDF.createTempView("t_person")

    //通过sparkSession来操作sql语句
    spark.sql("select * from t_person").show()

    spark.sql("select * from t_person where id =1").show()

    spark.sql("select * from t_person order by age desc").show()
    //---------------------SQL语法操作---------------end
    //关闭
    sc.stop()
    spark.stop()


  }
}

``` 

### 5.3   通过StructType直接指定Schema
当case class不能提前定义好时，可以通过以下三步创建DataFrame

（1）将RDD转为包含Row对象的RDD

（2）基于StructType类型创建schema，与第一步创建的RDD相匹配

（3）通过sparkSession的createDataFrame方法对第一步的RDD应用schema创建DataFrame


``` scala
package cn.hzh.sql

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}

/**
  * Created by Asus on 2018/9/18.
  */
object SparkSqlSchema {

  def main(args: Array[String]): Unit = {
    // 1 创建sparkSession
    val spark: SparkSession = SparkSession.builder().appName("SparkSqlSchema").master("local[2]")
        .getOrCreate()
    // 2 获取SparkContext
    val sc: SparkContext = spark.sparkContext
        sc.setLogLevel("WARN")
    // 3 读取目标文件
    val rdd: RDD[String] = sc.textFile("D:\\wordcount\\input\\people.txt")
    // 4 切割每一行
    val lineData = rdd.map(_.split(" "))
    // 5 将lineData与Row进行关联
    val row: RDD[Row] = lineData.map(x => Row(x(0).toInt, x(1), x(2).toInt))
    // 6 通过structType指定Schema
    val structType = StructType(
          StructField("id", IntegerType, true) ::
          StructField("name", StringType, false) ::
          StructField("age", IntegerType, false) :: Nil
    )
    // 7 通过使用sparkSession来调用createDataFrame生成DataFrame
    val dataFrame: DataFrame = spark.createDataFrame(row,structType)

    dataFrame.printSchema()

    dataFrame.show()

    // sql语法操作
    dataFrame.createTempView("t_people")

    spark.sql("select * from t_people").show()
    spark.sql("select * from t_people where age > 25").show
    spark.sql("select * from t_people order by age desc").show
    
    sc.stop()
    spark.stop()
  }
}

``` 

### 5.4 编写Spark SQL程序操作HiveContext

HiveContext是对应spark-hive这个项目,与hive有部分耦合, 支持hql,是SqlContext的子类，在Spark2.0之后，HiveContext和SqlContext在SparkSession进行了统一，可以通过操作SparkSession来操作HiveContext和SqlContext。

``` scala
package cn.hzh.sql

import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkContext

// todo 利用sparksql操作hivesql
object HiveSupport {

  def main(args: Array[String]): Unit = {
    // 1 创建SparkSession
    val spark: SparkSession = SparkSession.builder()
        .appName("HiveSupport")
        .master("local[2]")
        .enableHiveSupport() //开启对hive的支持
        //  spark.sql.warehouse.dir 来指定仓库中数据库的默认存储位置
        .config("spark.sql.warehouse.dir", "d:\\spark-warehouse")
        .getOrCreate()
    // 2 获取SparkContext
    val sc: SparkContext = spark.sparkContext
    sc.setLogLevel("WARN")
    // 3  通过SparkSession操作hivesql
//    spark.sql("create table if not exists student(id int,name string,age int)" +
//        "row format delimited fields terminated by ','"
//    )

    // 4 加载数据到hive表里
    //   spark.sql("load data local inpath 'spark-wordCount/data/student.txt' into table student")

    // 5 查询表的结果spark-wordCount/data/student.txt
    spark.sql("select * from student").show()

    sc.stop()
    spark.stop()
  }
}

```

## 6 四、 数据源

### 6.1 JDBC

Spark SQL可以通过JDBC从关系型数据库中读取数据的方式创建DataFrame，通过对DataFrame一系列的计算后，还可以将数据再写回关系型数据库中。

#### 6.1.1  SparkSql从MySQL中加载数据

``` xml
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>5.1.38</version>
    </dependency>
```

``` scala
package cn.hzh.sql

import java.util.Properties

import org.apache.spark.sql.{DataFrame, SparkSession}


// todo 利用sparksql从mysql中加载数据
object DataFromMysql {

  def main(args: Array[String]): Unit = {
    // 1 创建SparkSession
   val spark =  SparkSession.builder().appName("DataFromMysql")
        .master("local[2]")
        .getOrCreate()

    // 2 通过sparkSession获取mysql表中的数据
    // 准备配置属性
     val properties = new Properties()
    properties.setProperty("user","root")
    properties.setProperty("password","123456")


    val dataFrame: DataFrame = spark.read.jdbc("jdbc:mysql://192.168.168.121:3306/userdb","emp_add",properties)

    dataFrame.show()

    dataFrame.printSchema()

    dataFrame.createTempView("emp_add")

    spark.sql("select * from emp_add where id >= 1203").show

    //spark.sql("select * from emp_add where id >= 1203").show

    spark.stop()

  }
}

```

* 另一种方式
``` scala
package cn.hzh.sql

import java.util.Properties

import org.apache.spark.sql.{DataFrame, SparkSession}


// todo 利用sparksql从mysql中加载数据
object DataFromMysql {

  def main(args: Array[String]): Unit = {
    // 1 创建SparkSession
   val spark =  SparkSession.builder().appName("DataFromMysql")
        .master("local[2]")
        .getOrCreate()

    val jdbcDF = spark.read
        .format("jdbc")
        .option("url", "jdbc:mysql://192.168.168.121:3306/userdb")
        .option("dbtable", "emp_add")
        .option("user", "root")
        .option("password", "123456")
        .load()

    jdbcDF.show()

    jdbcDF.printSchema()

    jdbcDF.createTempView("emp_add")

    spark.sql("select * from emp_add where id >= 1203").show

    //spark.sql("select * from emp_add where id >= 1203").show

    spark.stop()

  }
}

``` 
### 6.1.2 通过spark-shell运行

* （1）、启动spark-shell(必须指定mysql的连接驱动包)

``` 
spark-shell \
--master spark://hadoop-node-1:7077 \
--executor-memory 1g \
--total-executor-cores  2 \
--jars /export/server/hive/lib/mysql-connector-java-5.1.35.jar \
--driver-class-path /export/server/hive/lib/mysql-connector-java-5.1.35.jar 
``` 

* （2）、从mysql中加载数据

`val mysqlDF = spark.read.format("jdbc").options(Map("url" -> "jdbc:mysql://192.168.168.121:3306/userdb", "driver" -> "com.mysql.jdbc.Driver", "dbtable" -> "emp_add", "user" -> "root", "password" -> "123456")).load()`

## 7 SparkSql将数据写入到MySQL中

``` scala
package cn.hzh.sql

import org.apache.spark.sql.SparkSession
import java.util.Properties

case class People(id: Int, name: String, age: Int)

// todo  使用sparkSession将数据写入mysql中
object DataToMysql {

  def main(args: Array[String]): Unit = {

    // 1 创建SparkSession
    val spark = SparkSession.builder().appName("DataToMysql")
        .master("local[2]")
        .getOrCreate()
    // 2
    val sc = spark.sparkContext
    sc.setLogLevel("WARN")
    // 3
    val rdd = sc.textFile("D:\\wordcount\\input\\people.txt")
    // 4
    val lineArrayData = rdd.map(x => x.split(" "))
    // 5
    val peopleRDD = lineArrayData.map(x => People(x(0).toInt, x(1), x(2).toInt))
    // 6
    // 导包
    import spark.implicits._
    val dataFarme = peopleRDD.toDF

    // 7 注册为表
    dataFarme.createTempView("people")

    val resultDF = spark.sql("select * from people order by age desc")

    // 8 将resultDF写入到mysql中

    val properties = new Properties()
    properties.setProperty("user", "root")
    properties.setProperty("password", "123456")

    //resultDF.write.jdbc("jdbc:mysql://192.168.168.121:3306/userdb","people",properties)
    //mode需要对应4个参数
    //overwrite:覆盖（它会帮你创建一个表，然后进行覆盖）
    //append:追加（它会帮你创建一个表，然后把数据追加到表里面）
    //ignore:忽略（它表示只要当前表存在，它就不会进行任何操作）
    //ErrorIfExists:只要表存在就报错（默认选项）
    resultDF.write.mode("append").jdbc("jdbc:mysql://192.168.168.121:3306/userdb", "people", properties)

    sc.stop()
    spark.stop()

  }
}
``` 

## 8 用maven将程序打包

通过IDEA工具打包即可

``` scala
package cn.hzh.sql

import org.apache.spark.sql.SparkSession
import java.util.Properties

case class People(id: Int, name: String, age: Int)

// todo  使用sparkSession将数据写入mysql中
object DataToMysql {

  def main(args: Array[String]): Unit = {

    // 1 创建SparkSession
    val spark = SparkSession.builder().appName("DataToMysql")
        .getOrCreate()
    // 2
    val sc = spark.sparkContext
    sc.setLogLevel("WARN")
    // 3
    val rdd = sc.textFile(args(0))
    // 4
    val lineArrayData = rdd.map(x => x.split(" "))
    // 5
    val peopleRDD = lineArrayData.map(x => People(x(0).toInt, x(1), x(2).toInt))
    // 6
    // 导包
    import spark.implicits._
    val dataFarme = peopleRDD.toDF

    // 7 注册为表
    dataFarme.createTempView("people")

    val resultDF = spark.sql("select * from people order by age desc")

    // 8 将resultDF写入到mysql中

    val properties = new Properties()
    properties.setProperty("user", "root")
    properties.setProperty("password", "123456")

    //resultDF.write.jdbc("jdbc:mysql://192.168.168.121:3306/userdb","people",properties)
    //mode需要对应4个参数
    //overwrite:覆盖（它会帮你创建一个表，然后进行覆盖）
    //append:追加（它会帮你创建一个表，然后把数据追加到表里面）
    //ignore:忽略（它表示只要当前表存在，它就不会进行任何操作）
    //ErrorIfExists:只要表存在就报错（默认选项）
    resultDF.write.mode("overwrite").jdbc("jdbc:mysql://192.168.168.121:3306/userdb", args(1), properties)

    sc.stop()
    spark.stop()

  }
}

``` 

* 1 打包 

* 2 把jar包上传到 `/export/server/spark/myjar`

* 3 
``` sql
spark-submit --master spark://hadoop-node-1:7077 \
--class cn.hzh.sql.DataToMysql \
--executor-memory 1g \
--total-executor-cores 2 \
--jars  /export/server/hive/lib/mysql-connector-java-5.1.38.jar \
--driver-class-path  /export/server/hive/lib/mysql-connector-java-5.1.38.jar  \
/export/server/spark/myjar/spark-wordCount-1.0-SNAPSHOT-ToMysql.jar /user/spark/people.txt  qq
``` 




