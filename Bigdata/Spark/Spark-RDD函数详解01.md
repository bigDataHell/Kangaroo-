## 1 创建并行集合

可以在您的 driver program（驱动程序）中已存在的集合上通过调用  SparkContext 的` parallelize `方法来创建并行集合。该集合的元素从一个可以并行操作的 distributed dataset（分布式数据集）中复制到另一个 dataset（数据集）中去。例如，这里是一个如何去创建一个保存数字 1 ~ 5 的并行集合。

``` scala
val data = Array(1, 2, 3, 4, 5)
val rdd = sc.parallelize(data)
```

并行集合中一个很重要参数是 partitions（分区）的数量，它可用来切割 dataset（数据集）。__Spark 将在集群中的每一个分区上运行一个任务__。通常您希望群集中的每一个 CPU 计算 2-4 个分区。一般情况下，Spark 会尝试根据您的群集情况来自动的设置的分区的数量。
当然，您也可以将分区数作为第二个参数传递到 parallelize (e.g. sc.parallelize(data, 10)) 方法中来手动的设置它。

-----------------------------
## 2  外部数据集

Spark 可以从 Hadoop 所支持的任何存储源中创建 distributed dataset（分布式数据集），包括本地文件系统，HDFS，Cassandra，HBase，Amazon S3 等等
。 Spark 支持文本文件，SequenceFiles，以及任何其它的 Hadoop InputFormat。

可以使用`SparkContext`的 `textFile` 方法来创建文本文件的 RDD。此方法需要一个文件的 URI（计算机上的本地路径 ，hdfs://，s3n:// 等等的 URI），
并且读取它们作为一个 lines（行）的集合。下面是一个调用示例 : 

``` scala
scala> val distFile = sc.textFile("data.txt")
distFile: org.apache.spark.rdd.RDD[String] = data.txt MapPartitionsRDD[10] at textFile at <console>:26
```
__使用 Spark 来读取文件的一些注意事项 :__

* 如果使用本地文件系统的路径，所工作节点的相同访问路径下该文件必须可以访问。复制文件到所有工作节点上，或着使用共享的网络挂载文件系统。

* 所有 Spark 中基于文件的输入方法，包括 textFile（文本文件），支持目录，压缩文件，或者通配符来操作。
    例如，您可以用 textFile("/my/directory")，textFile("/my/directory/*.txt") 和 textFile("/my/directory/*.gz")。

* textFile 方法也可以通过第二个可选的参数来控制该文件的分区数量。默认情况下，Spark 为文件的每一个 block（块）创建的一个分区（HDFS 中块大小默认是 128M）
    ，当然你也可以通过传递一个较大的值来要求一个较高的分区数量。请注意，分区的数量不能够小于块的数量。
    
    
 __除了文本文件之外，Spark 的 Scala API 也支持一些其它的数据格式 :__

* `SparkContext.wholeTextFiles` 可以读取包含多个小文本文件的目录，并返回它们的每一个 (filename, content) 对。这与 textFile 形成对比，它的每一个文件中的每一行将返回一个记录。

* 针对 `SequenceFiles`，使用 SparkContext 的 sequenceFile[K, V] 方法，其中 K 和 V 指的是它们在文件中的类型。这些应该是 Hadoop 中 Writable 接口的子类，例如 IntWritable 和 Text。
  此外，Spark 可以让您为一些常见的 Writables 指定原生类型。例如，sequenceFile[Int, String] 会自动读取 IntWritables 和 Texts 。

* 针对其它的 `Hadoop InputFormats`，您可以使用 SparkContext.hadoopRDD 方法，它接受一个任意 JobConf 和 input format（输入格式）类，key 类和          value 类。
     通过相同的方法你可以设置你 Hadoop Job 的输入源。你还可以使用基于 “new” 的 MapReduce API（org.apache.hadoop.mapreduce）来使用                  SparkContext.newAPIHadoopRDD 以设置 InputFormats。
  
* RDD.saveAsObjectFile 和 SparkContext.objectFile 支持使用简单的序列化的 Java Object 来保存 RDD。虽然这不像 Avro 这种专用的格式一样高效，但其提供了一种更简单的方式来保存任何的 RDD。

## 3 RDD操作

### 3.1 基础

``` scala
val lines = sc.textFile("data.txt")
val lineLengths = lines.map(s => s.length)
val totalLength = lineLengths.reduce((a, b) => a + b)
``` 

第一行从外部文件中定义了一个基本的 RDD，但这个数据集并未加载到内存中或即将被行动 : line 仅仅是一个类似指针的东西，指向该文件。

第二行定义了 lineLengths 作为 map transformation 的结果。请注意，由于 laziness（延迟加载）lineLengths 不会被立即计算，。

__最后，我们运行 reduce，这是一个 action。在这此时，Spark 分发计算任务到不同的机器上运行，每台机器都运行在 map 的一部分并本地运行 reduce，仅仅返回它聚合后的结果给驱动程序。__

如果我们也希望以后再次使用lineLengths，我们还可以添加：

`lineLengths.persist()`

在 reduce 之前，这将导致 lineLengths 在第一次计算之后就被保存在 memory 中。

__查看源码可知，sc.textFile(param) 中的 param 可以是以逗号分隔的字符串路径。如 "/user/data/123.txt,/user/data/1234.txt"！实际的生产开发中常用！__

## 3.2 传递函数给 Spark

当驱动程序在集群上运行时，Spark 的 API 在很大程度上依赖于传递函数。有2种推荐的方式来做到这一点 : 

匿名函数的语法 Anonymous function syntax，它可以用于短的代码片断。 <br>
在全局单例对象中的静态方法。例如，你可以定义对象 MyFunctions 然后传递 MyFunctions.func1，具体如下 : 
``` scala
object MyFunctions {
  def func1(s: String): String = { ... }
}

myRdd.map(MyFunctions.func1)
```

请注意，虽然也有可能传递一个类的实例（与单例对象相反）的方法的引用，这需要发送整个对象，包括类中其它方法。例如，考虑 : 
``` scala
class MyClass {
  def func1(s: String): String = { ... }
  def doStuff(rdd: RDD[String]): RDD[String] = { rdd.map(func1) }
}
```
这里，如果我们创建一个MyClass的实例，并调用doStuff，在map内有MyClass实例的func1方法的引用，所以整个对象需要被发送到集群的。

它类似于 `rdd.map(x => this.func1(x))。`

类似的方式，访问外部对象的字段将引用整个对象 : 
``` scala
class MyClass {
  val field = "Hello"
  def doStuff(rdd: RDD[String]): RDD[String] = { rdd.map(x => field + x) }
}
``` 
相当于写 `rdd.map(x => this.field + x)`，它引用这个对象所有的东西。为了避免这个问题，最简单的方法是 field（域）到本地变量，而不是从外部访问它的 : 
```  scala
def doStuff(rdd: RDD[String]): RDD[String] = {
  val field_ = this.field
  rdd.map(x => field_ + x)
}
```

### 3.2 理解闭包(重点)

闭包是一个函数，返回值依赖于声明在函数外部的一个或多个变量。
闭包通常来讲可以简单的认为是可以访问不在当前作用域范围内的一个函数。

在集群中执行代码时，一个关于 Spark 更难的事情是理解的变量和方法的范围和生命周期。

修改其范围之外的变量 RDD 操作可以混淆的常见原因。在下面的例子中，我们将看一下使用的 foreach() 代码递增累加计数器，但类似的问题，也可能会出现其他操作上。

示例 <br>
考虑一个简单的 RDD 元素求和，以下行为可能不同，具体取决于是否在同一个 JVM 中执行。

一个常见的例子是当 Spark 运行在本地模式（--master = local[n]）时，与部署 Spark 应用到群集（例如，通过 spark-submit 到 YARN）: 

``` scala
var counter = 0
var rdd = sc.parallelize(data)
 
// Wrong: Don't do this!!
rdd.foreach(x => counter += x)
 
println("Counter value: " + counter)

``` 

本地  VS 集群模式

上面的代码行为是不确定的，并且可能无法按预期正常工作。Spark 执行作业时，会分解 RDD 操作到每个执行者里。在执行之前，Spark 计算任务的 closure（闭包）。而闭包是在 RDD 上的执行者必须能够访问的变量和方法（在此情况下的 foreach() ）。闭包被序列化并被发送到每个执行器。

闭包的变量副本发给每个 executor ，当 counter 被 foreach 函数引用的时候，它已经不再是 driver node 的 counter 了。虽然在 driver node 仍然有一个 counter 在内存中，但是对 executors 已经不可见。executor 看到的只是序列化的闭包一个副本。所以 counter 最终的值还是 0，因为对 counter 所有的操作所有操作均引用序列化的 closure 内的值。

在本地模式，在某些情况下的 foreach 功能实际上是同一 JVM 上的驱动程序中执行，并会引用同一个原始的计数器，实际上可能更新。

为了确保这些类型的场景明确的行为应该使用的 Accumulator（累加器）。当一个执行的任务分配到集群中的各个 worker 结点时，Spark 的累加器是专门提供安全更新变量的机制。本指南的累加器的部分会更详细地讨论这些。

在一般情况下，closures - constructs 像循环或本地定义的方法，不应该被用于改动一些全局状态。Spark 没有规定或保证突变的行为，以从封闭件的外侧引用的对象。一些代码，这可能以本地模式运行，但是这只是偶然和这样的代码如预期在分布式模式下不会表现。改用如果需要一些全局聚集累加器。

__打印 RDD 的元素__

另一种常见的语法用于打印 RDD 的所有元素使用 rdd.foreach(println) 或 rdd.map(println)。在一台机器上，这将产生预期的输出和打印 RDD 的所有元素。然而，在集群 cluster 模式下，stdout 输出正在被执行写操作 executors 的 stdout 代替，而不是在一个驱动程序上，因此stdout 的 driver 程序不会显示这些！要打印 driver 程序的所有元素，可以使用的 collect() 方法首先把 RDD 放到 driver 程序节点上 : rdd.collect().foreach(println)。这可能会导致 driver 程序耗尽内存，虽说，因为 collect() 获取整个 RDD 到一台机器; 如果你只需要打印 RDD 的几个元素，一个更安全的方法是使用 take() : `rdd.take(100).foreach(println)。`



### 3.4 使用 Key-Value 对工作

虽然大多数 Spark 操作工作在包含任何类型对象的 RDDs 上，只有少数特殊的操作可用于 Key-Value 对的 RDDs。最常见的是分布式 “shuffle” 操作，如通过元素的 key 来进行 grouping 或 aggregating 操作。

在 Scala 中，这些操作时自动可用于包含 Tuple2 对象的 RDDs（在语言中内置的元组，通过简单的写 (a, b) ）。在 PairRDDFunctions 类中该 Key-Value 对的操作有效的，其中围绕元组的 RDD 自动包装。

例如，下面的代码使用的 Key-Value 对的 reduceByKey 操作统计文本文件中每一行出现了多少次 : 
``` scala
val lines = sc.textFile("data.txt")
val pairs = lines.map(s => (s, 1))
val counts = pairs.reduceByKey((a, b) => a + b)
```
我们也可以使用 counts.sortByKey() ，例如，在对按字母顺序排序，最后 counts.collect() 把他们作为一个数据对象返回给的驱动程序。

注意 : 使用自定义对象作为 Key-Value 对操作的 key 时，您必须确保自定义 equals() 方法有一个 hashCode() 方法相匹配。有关详情，请参见这是 Object.hashCode() documentation 中列出的约定
















