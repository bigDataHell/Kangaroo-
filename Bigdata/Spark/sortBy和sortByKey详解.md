
在Spark中存在两种对RDD进行排序的函数，分别是 sortBy和sortByKey函数: 

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
   * 返回按给定键函数排序的RDD。
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
  
  
