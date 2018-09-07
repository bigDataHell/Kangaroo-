
在Spark中存在两种对RDD进行排序的函数，分别是 `sortBy`和`sortByKey`函数: 

* sortBy是对标准的RDD进行排序，它是从Spark 0.9.0之后才引入的（可以参见SPARK-1063）。
* sortByKey函数是对PairRDD进行排序，也就是有Key和Value的RDD,即tuple2类型的RDD

__排序的前提是排序不受分区的影响__ :

  * 1 把所有的数据汇总到一个分区中进行排序.
  * 2 RDD多个分区的情况下,使用RDD.collect()消除分区的影响.
  
  
### sortBy

函数是在org.apache.spark.rdd.RDD类中实现的，它的实现如下：

``` scala
  /**
   * Return this RDD sorted by the given key function.
   * 返回按给定key函数排序的RDD。
   */
  def sortBy[K](
      f: (T) => K,
      ascending: Boolean = true,
      numPartitions: Int = this.partitions.length)
      (implicit ord: Ordering[K], ctag: ClassTag[K]): RDD[T] = withScope {
    this.keyBy[K](f)
        .sortByKey(ascending, numPartitions)
        .values
  }
  
```

该函数最多可以传三个参数：

* 第一个参数是一个函数，该函数的也有一个带T泛型的参数，返回类型和RDD中元素的类型是一致的；
  
* 第二个参数是ascending，从字面的意思大家应该可以猜到，是的，这参数决定排序后RDD中的元素是升序还是降序，默认是true，也就是升序；
  
* 第三个参数是numPartitions，该参数决定排序后的RDD的分区个数，默认排序后的分区个数和排序之前的个数相等，即为this.partitions.size。
  
  
从sortBy函数的实现可以看出，第一个参数是必须传入的，而后面的两个参数可以不传入。而且sortBy函数函数的实现依赖于sortByKey函数.

## sortBy 使用

sortByKey函数作用于Key-Value形式的RDD，并对Key进行排序。它是在`org.apache.spark.rdd.OrderedRDDFunctions`中实现的，实现如下

``` scala
 /**
   * Sort the RDD by key, so that each partition contains a sorted range of the elements. Calling
   * `collect` or `save` on the resulting RDD will return or output an ordered list of records
   * (in the `save` case, they will be written to multiple `part-X` files in the filesystem, in
   * order of the keys).
   */
  // TODO: this currently doesn't work on P other than Tuple2!
  
  按key对RDD进行排序，这样每个分区都包含一个排序范围的元素。调用
  生成的RDD上的`collect`或`save`将返回或输出有序的记录列表
  (在`save`的情况下，它们将按照键的顺序写入文件系统中的多个`part-X`文件）。
  // TODO：除了Tuple2之外，这个目前不适用于P！
  
  def sortByKey(ascending: Boolean = true, numPartitions: Int = self.partitions.length)
      : RDD[(K, V)] = self.withScope
  {
    val part = new RangePartitioner(numPartitions, self, ascending)
    new ShuffledRDD[K, V, V](self, part)
      .setKeyOrdering(if (ascending) ordering else ordering.reverse)
  }
``` 

参数 : 

 * 1 ascending 排序方式,默认true,升序.
 * 2 排序后的分区数,默认父RDD的分区数
 
 ## 案例 
 
 ``` scala
 /**
  * sortBy 和 sortByKey测试
  */
object SortDemo {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("sortTest")
    conf.setMaster("local[2]")

    val sc = new SparkContext(conf)

    // 注意,rdd1有两个分区 = 线程数量
    val rdd1 = sc.parallelize(Array(1,5,3,7,89,32,2,4))

    rdd1.sortBy(x =>x).collect().foreach(e => println(e))

    val rdd2 =sc.textFile("D:\\wordcount\\input\\2.txt")

    val rdd3 = rdd2.map(line =>{
      var arr = line.split(" ")
      (arr(0).toInt,line)
    })

    rdd3.sortByKey(false).collect().foreach(e => println(e))
  }
}
```
