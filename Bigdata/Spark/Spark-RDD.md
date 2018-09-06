## RDD:

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
## RDD变换

	返回指向新rdd的指针，在rdd之间创建依赖关系。每个rdd都有计算函数和指向父RDD的指针。
	
	map()									 对每个元素进行变换，应用变换函数
											    (T)=>V


	filter()								//过滤器,(T)=>Boolean
  
	flatMap()								//压扁,T => TraversableOnce[U]

	mapPartitions()							//对每个分区进行应用变换，输入的Iterator,返回新的迭代器，可以对分区进行函数处理。
											//Iterator<T> => Iterator<U>

	mapPartitionsWithIndex(func)			//同上，(Int, Iterator<T>) => Iterator<U>

	sample(withReplacement, fraction, seed)	//采样返回采样的RDD子集。
											//withReplacement 元素是否可以多次采样.
											//fraction : 期望采样数量.[0,1]

	union()									//类似于mysql union操作。
											//select * from persons where id < 10 
											//union select * from id persons where id > 29 ;

	intersection							//交集,提取两个rdd中都含有的元素。
	distinct([numTasks]))					//去重,去除重复的元素。

	groupByKey()							//(K,V) => (K,Iterable<V>)

	reduceByKey(*)							//按key聚合。 

	aggregateByKey(zeroValue)(seqOp, combOp, [numTasks])
											//按照key进行聚合
	key:String U:Int = 0

	sortByKey								//排序

	join(otherDataset, [numTasks])			//连接,(K,V).join(K,W) =>(K,(V,W)) 

	cogroup									//协分组
											//(K,V).cogroup(K,W) =>(K,(Iterable<V>,Iterable<!-- <W> -->)) 
	cartesian(otherDataset)					//笛卡尔积,RR[T] RDD[U] => RDD[(T,U)]

	pipe									//将rdd的元素传递给脚本或者命令，执行结果返回形成新的RDD
	coalesce(numPartitions)					//减少分区
	repartition								//可增可减
	repartitionAndSortWithinPartitions(partitioner)
											//再分区并在分区内进行排序


RDD Action
------------------
	collect()								//收集rdd元素形成数组.
	count()									//统计rdd元素的个数
	reduce()								//聚合,返回一个值。
	first									//取出第一个元素take(1)
	take									//
	takeSample (withReplacement,num, [seed])
	takeOrdered(n, [ordering])
	
	saveAsTextFile(path)					//保存到文件
	saveAsSequenceFile(path)				//保存成序列文件

	saveAsObjectFile(path) (Java and Scala)

	countByKey()							//按照key,统计每个key下value的个数.
	

spark集成hadoop ha
-------------------------
	1.复制core-site.xml + hdfs-site.xml到spark/conf目录下
	2.分发文件到spark所有work节点
	3.启动spark集群
	4.启动spark-shell,连接spark集群上
		$>spark-shell --master spark://s201:7077
		$scala>sc.textFile("hdfs://mycluster/user/centos/test.txt").collect();	
