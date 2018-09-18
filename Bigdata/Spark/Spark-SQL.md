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




