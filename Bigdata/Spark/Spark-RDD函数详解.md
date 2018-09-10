## 1 创建并行集合

可以在您的 driver program（驱动程序）中已存在的集合上通过调用  SparkContext 的` parallelize `方法来创建并行集合。该集合的元素从一个可以并行操作的 distributed dataset（分布式数据集）中复制到另一个 dataset（数据集）中去。例如，这里是一个如何去创建一个保存数字 1 ~ 5 的并行集合。

``` scala
val data = Array(1, 2, 3, 4, 5)
val rdd = sc.parallelize(data)
```

并行集合中一个很重要参数是 partitions（分区）的数量，它可用来切割 dataset（数据集）。__Spark 将在集群中的每一个分区上运行一个任务__。通常您希望群集中的每一个 CPU 计算 2-4 个分区。一般情况下，Spark 会尝试根据您的群集情况来自动的设置的分区的数量。
当然，您也可以将分区数作为第二个参数传递到 parallelize (e.g. sc.parallelize(data, 10)) 方法中来手动的设置它。

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

* 针对 SequenceFiles，使用 SparkContext 的 sequenceFile[K, V] 方法，其中 K 和 V 指的是它们在文件中的类型。这些应该是 Hadoop 中 Writable 接口的子类，例如 IntWritable 和 Text。
  此外，Spark 可以让您为一些常见的 Writables 指定原生类型。例如，sequenceFile[Int, String] 会自动读取 IntWritables 和 Texts 。

* 针对其它的 Hadoop InputFormats，您可以使用 SparkContext.hadoopRDD 方法，它接受一个任意 JobConf 和 input format（输入格式）类，key 类和 value 类。
  通过相同的方法你可以设置你 Hadoop Job 的输入源。你还可以使用基于 “new” 的 MapReduce API（org.apache.hadoop.mapreduce）来使用 SparkContext.newAPIHadoopRDD 以设置 InputFormats。
  
* RDD.saveAsObjectFile 和 SparkContext.objectFile 支持使用简单的序列化的 Java Object 来保存 RDD。虽然这不像 Avro 这种专用的格式一样高效，但其提供了一种更简单的方式来保存任何的 RDD。























