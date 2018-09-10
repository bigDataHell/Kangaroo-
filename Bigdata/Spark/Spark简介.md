# Spark

##  1 运行模式

#### 并行

* 集群计算。
* 并行计算。

#### 并发

* 并发执行。

## 2 Spark特点

	Lightning-fast cluster computing。
	快如闪电的集群计算。
	大规模快速通用的计算引擎。
	速度:	比hadoop 100x,磁盘计算快10x
	使用:	java / Scala /R /python
			提供80+算子(操作符)，容易构建并行应用。
	通用:	组合SQL ，流计算 + 复杂分析。

	运行：	Hadoop, Mesos, standalone, or in the cloud,local.

### Spark模块

	Spark core		//核心模块
	Spark SQL		//SQL
	Spark Streaming	/	/流计算
	Spark MLlib		//机器学习
	Spark graph		//图计算

	DAG		//direct acycle graph,有向无环图。
	

### API

	[SparkContext]
		Spark程序的入口点，封装了整个spark运行环境的信息。
	

	[RDD]
		resilient distributed dataset,弹性分布式数据集。等价于集合。
		
## 3 集群启动, UI

 * 启动 
` start-all.sh`
 * 停止
 `stop-all.sh`
 * 集群UI
 `http://hadoop-node-1:8080`
 * 单节点UI
 `http://hadoop-node-1:4040`
 
#### 运行 spark-shell  指定具体的 master  地址

（1）需求:
spark-shell 运行时指定具体的 master 地址，读取 HDFS 上的数据，做单词计数，然后将结果保存在 HDFS 上。

（2）执行启动命令：
`spark-shell  
--master spark://hdp-node-01:7077  
--executor-memory 1g  
--total-executor-cores 2`

* 参数说明：

--master spark://hdp-node-01:7077 指定 Master 的地址 <br>
--executor-memory 1g 指定每个 worker 可用内存为 1g <br>
--total-executor-cores 2 指定整个集群使用的 cup 核数为 2 个 <br>

注意：
如果启动 spark shell 时没有指定 master 地址，但是也可以正常启动 spark shell 
和执行 spark shell 中的程序，其实是启动了 spark 的 local 模式，
 
## 4 word count : scala-shell

	//加载文本文件,以换行符方式切割文本. : Array(hello  world2,hello world2 ,...)
	val rdd1 = sc.textFile("/home/centos/test.txt");

	//单词统计1
	$scala>val rdd1 = sc.textFile("/home/centos/test.txt")
	$scala>val rdd2 = rdd1.flatMap(line=>line.split(" "))
	$scala>val rdd3 = rdd2.map(word = > (word,1))
	$scala>val rdd4 = rdd3.reduceByKey(_ + _)
	$scala>rdd4.collect

	//单词统计2
	sc.textFile("/home/centos/test.txt").flatMap(_.split(" ")).map((_,1)).reduceByKey(_ + _).collect

	//统计所有含有wor字样到单词个数。filter

	//过滤单词
	sc.textFile("/root/test.txt").flatMap(_.split(" ")).filter(_.contains("wor")).map((_,1)).reduceByKey(_ + _).collect



[API]

	SparkContext:
		Spark功能的主要入口点。代表到Spark集群的连接，可以创建RDD、累加器和广播变量.
		每个JVM只能激活一个SparkContext对象，在创建sc之前需要stop掉active的sc。
	
	SparkConf:
		spark配置对象，设置Spark应用各种参数，kv形式。
## 5 wordCount-java

* local模式 

``` java
public class WordCount_Java {

  public static void main(String[] args) {

    // 1 创建sparkConf对象
    SparkConf sparkConf = new SparkConf();
    //sparkConf.setAppName("wordCountJava");
    sparkConf.setMaster("local");

    // 2 创建 java sc
    JavaSparkContext sc = new JavaSparkContext(sparkConf);

    // 3 加载文本文件
    // JavaRDD<String> rdd1 = sc.textFile("D:\\wordcount\\input\\1.txt");
    JavaRDD<String> rdd1 = sc.textFile(args[0]);

    // 4 压扁
    JavaRDD<String> rdd2 = rdd1.flatMap(new FlatMapFunction<String, String>() {
      public Iterator<String> call(String s) throws Exception {
        List list = new ArrayList<String>();
        String[] split = s.split(" ");
        for (String word : split) {
          list.add(word);
        }
        return list.iterator();
      }
    });

    // 5 映射 把单词转化为键值对
    JavaPairRDD<String, Integer> rdd3 = rdd2
        .mapToPair(new PairFunction<String, String, Integer>() {

          public Tuple2<String, Integer> call(String s) throws Exception {
            return new Tuple2<String, Integer>(s, 1);
          }
        });

    // 6 reduce化简 统计
    JavaPairRDD<String, Integer> rdd4 = rdd3
        .reduceByKey(new Function2<Integer, Integer, Integer>() {
          public Integer call(Integer v1, Integer v2) throws Exception {
            return v1 + v2;
          }
        });

    // 二元元组 ???????????????????
    List<Tuple2<String, Integer>> list = rdd4.collect();

    for (Tuple2<String, Integer> t : list) {
      System.out.println(t._1() + " : " + t._2());
    }
  }
}
``` 
* 打包,上传jar包
* local模式下

`spark-submit --master local --name wordCountJava --class cn.hzh.wordCount.Java.WordCount_Java spark-wordCount-1.0-SNAPSHOT.jar /root/test.txt`

* wordCount-集群模式下

`spark-submit  --master spark://hadoop-node-1:7077 --name MyWordCount --class cn.hzh.wordCount.Java.WordCount_Java spark-wordCount-1.0-SNAPSHOT.jar hdfs://hadoop-node-1:9000/user/spark/test.txt`
	

