## 1 RDD:

	是spark的基本数据结构，是不可变数据集。RDD中的数据集进行逻辑分区，每个分区可以单独在集群节点
	进行计算。可以包含任何java,scala，python和自定义类型。

	RDD是只读的记录分区集合。RDD具有容错机制。

	创建RDD方式，一、并行化一个现有集合。

	hadoop 花费90%时间用户rw。、(读写)
	
	内存处理计算。在job间进行数据共享。内存的IO速率高于网络和disk的10 ~ 100之间。

#### 内部包含5个主要属性

	* 1.分区列表
	* 2.针对每个split的计算函数。
	* 3.对其他rdd的依赖列表
	* 4.可选，如果是KeyValueRDD的话，可以带分区类。
	* 5.可选，首选块位置列表(hdfs block location);




#### 默认并发度 :

local.backend.defaultParallelism() = scheduler.conf.getInt("spark.default.parallelism", totalCores)
taskScheduler.defaultParallelism = backend.defaultParallelism()
sc.defaultParallelism =...; taskScheduler.defaultParallelism
defaultMinPartitions = math.min(defaultParallelism, 2)
sc.textFile(path,defaultMinPartitions)			//1,2

 cpu的内核数,默认cpu有多少个内核,就开几个并发度

---------------------------------
## 2 RDD变换

	返回指向新rdd的指针，在rdd之间创建依赖关系。每个rdd都有计算函数和指向父RDD的指针。
	
	map()						对每个元素进行变换，应用变换函数
							(T)=>V


	filter()					过滤器,(T)=>Boolean
  
	flatMap()					压扁,T => TraversableOnce[U]

	mapPartitions()					对每个分区进行应用变换，输入的Iterator,返回新的迭代器，可以对分区进行函数处理。
							Iterator<T> => Iterator<U>

	mapPartitionsWithIndex(func)			同上，(Int, Iterator<T>) => Iterator<U>

	sample(withReplacement, fraction, seed)	        采样返回采样的RDD子集。
							withReplacement 元素是否可以多次采样.
							fraction : 期望采样数量.[0,1]

	union()						类似于mysql union操作。
							select * from persons where id < 10 
							union select * from id persons where id > 29 ;

	intersection					交集,提取两个rdd中都含有的元素。
	
	distinct([numTasks]))				去重,去除重复的元素。

	groupByKey()					(K,V) => (K,Iterable<V>)

	reduceByKey(*)					按key聚合。 

	aggregateByKey(zeroValue)(seqOp, combOp, [numTasks])
									按照key进行聚合
	key:String U:Int = 0

	sortByKey					排序

	join(otherDataset, [numTasks])			连接,(K,V).join(K,W) =>(K,(V,W)) 

	cogroup						协分组 (K,V).cogroup(K,W) =>(K,(Iterable<V>,Iterable<!-- <W> -->))
							 
							
	cartesian(otherDataset)				笛卡尔积,RR[T] RDD[U] => RDD[(T,U)]

	pipe						将rdd的元素传递给脚本或者命令，执行结果返回形成新的RDD
	
	coalesce(numPartitions)				减少分区
	
	repartition					可增可减
	
	repartitionAndSortWithinPartitions(partitioner) 再分区并在分区内进行排序
							


## 3 RDD Action
------------------

	collect()								//收集rdd元素形成数组.
	count()									//统计rdd元素的个数
	reduce()								//聚合,返回一个值。
	first									//取出第一个元素take(1)
	take									//
	takeSample (withReplacement,num, [seed])
	takeOrdered(n, [ordering])
	
	saveAsTextFile(path)							//保存到文件
	saveAsSequenceFile(path)						//保存成序列文件

	saveAsObjectFile(path) (Java and Scala)

	countByKey()								//按照key,统计每个key下value的个数.
	

## 4 统计单词出现次数

``` scala
object WordCount01 extends  App{

  val conf = new SparkConf()
  conf.setAppName("wordCount")
  conf.setMaster("local[2]")

  val sc = new SparkContext(conf)

  // 读取文件,按行读取, 两个分区.
  val rdd1 = sc.textFile("D:\\wordcount\\input\\1.txt", 2)

  println("rdd1 : " + rdd1.getClass.getSimpleName)

  //统计单词出现次数方式一
  val num = rdd1.map(_.split(" ").length).reduce(_ + _)
  println("单词出现次数为 : " + num)

  // 切割
  val rdd2 = rdd1.flatMap(_.split(" "))
  // 统计所有单词数量方式二
  println("单词出现次数: "+rdd2.count())

  // 映射
  val rdd3 = rdd2.map((_,1))

  //统计不同的单词出现次数
  val rdd4 =rdd3.reduceByKey(_+_)

  // 统计所有单词数量方式三
  // 获取tuple的value
  val count = rdd4.map(_._2).reduce(_+_)
  println("单词出现次数: "+count)

  val arr = rdd4.collect()

  println("不重复的单词数量 : "+rdd4.count())

  arr.foreach(e => println(e))
}
``` 
## 5 按照key进行分区 : groupByKey

* 数据

		1 tom1 23 shandong
		2 tom2 43 shandong
		3 tom3 2 shanghai
		4 tom4 45 beijing
		5 tom5 67 henan
		6 tom6 34 henan
		7 tom8 12 shandong
		8 tom9 56 hebei

* 按照地区对数据进行分类
``` scala
object GroupByKeyDemo1 {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()

    conf.setAppName("GroupByKeyDemo1")
    conf.setMaster("local[2]")

    val sc = new SparkContext(conf)

    val rdd1 = sc.textFile("D:\\wordcount\\input\\2.txt",2)

    val rdd2 = rdd1.map(line => {
      //取出省份
      val key = line.split(" ")(3)
      // 返回键值对 (胜,line)
      (key,line)
    })

    var rdd3 = rdd2.groupByKey()

    println("地区数量 : "+rdd3.count())

    rdd3.collect().foreach(t =>{
      val key = t._1

      println("地区 : "+key +" 人数 : "+t._2.size)
      for ( e <- t._2){
        println(e)
      }
    })
  }
}
``` 

## 6 join  关联查询

* students.txt 

		1 zhangsan 
		2 lisi
		3 wangwu
		4 dabai
		5 xiaobai
		6 feixue

* scores.txt
	
		1 300
		2 888
		3 88
		4 99
		5 500
		6 256
``` scala
object JoinDemo extends App {

  val conf = new SparkConf()
  conf.setAppName("JOIN")
  conf.setMaster("local")

  val sc = new SparkContext(conf)

  // 名单
  val nameRdd1 = sc.textFile("D:\\wordcount\\input\\students.txt")
  val nameRdd2 = nameRdd1.map(line => {
    val arr = line.split(" ")
    //返回tuple
    (arr(0).toInt, arr(1))
  })

  // 分数
  val scoreRdd1 = sc.textFile("D:\\wordcount\\input\\scores.txt")
  val scoreRdd2 = scoreRdd1.map(line => {
    val arr = line.split(" ")
    //返回tuple
    (arr(0).toInt, arr(1).toInt)
  })

  val rdd = nameRdd2.join(scoreRdd2).sortByKey()
  rdd.collect().foreach(t => {
    println(t._1 + " : " + t._2._1 + " : "+t._2._2)
  })
}
```  

## 7 cogroup  协分组

* cogroup01.txt

		heinan 44
		heinan 22
		heinan 33 
		heinan 66
		heinan 45
		heibei 44
		heibei 45
		heibei 88
		heibei 43
* cogroup02.txt

	heinan tom1
	heinan tom2
	heinan tom3
	heibei jree2
	heibei jree4
	shandong luss
	

``` scala
object cogroupDemo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("cogroup")
    conf.setMaster("local[2]")

    val sc = new SparkContext(conf)

    val rdd1 = sc.textFile("D:\\wordcount\\input\\cogroup01.txt")
    // k v
    val rdd2 = rdd1.map(line => {
      val arr = line.split(" ")
      //返回tuple
      (arr(0),arr(1))
    })
    
    val rdd3 = sc.textFile("D:\\wordcount\\input\\cogroup02.txt")
    // k w
    val rdd4 = rdd3.map(line => {
      val arr = line.split(" ")
      //返回tuple
      (arr(0),arr(1))
    })
    val rdd = rdd2.cogroup(rdd4)
    rdd.collect().foreach(t => {
      println(t._1 +" :============ " )
      // t._2 还是一个元组
      for( e <- t._2._1) println(e)
      for( e <- t._2._2) println(e)
    })
  }
}
``` 

## 8 cartesian 笛卡儿积

``` scala
object CartesianDemo extends App{

  val conf = new SparkConf()
  conf.setAppName("cogroup")
  conf.setMaster("local[2]")

  val sc = new SparkContext(conf)
  // parallelize : 并行
  val rdd1 = sc.parallelize(Array("dabai","xiaobai","dahei","xiaohei"))
  val rdd2 = sc.parallelize(Array("傻","白","甜","晕"))

  val rdd =rdd1.cartesian(rdd2)
  println("数量 : "+rdd.count())
  rdd.collect().foreach( e =>{
    println(e._1 +" : "+e._2)
  })
}
```

## 9 pipe

不会用............


spark集成hadoop ha
-------------------------
	1.复制core-site.xml + hdfs-site.xml到spark/conf目录下
	2.分发文件到spark所有work节点
	3.启动spark集群
	4.启动spark-shell,连接spark集群上
		$>spark-shell --master spark://s201:7077
		$scala>sc.textFile("hdfs://mycluster/user/centos/test.txt").collect();	
