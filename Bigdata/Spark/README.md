## Spark

### 运行模式

#### 并行

	* 集群计算。
	* 并行计算。

#### 并发

        * 并发执行。

### Spark优点

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
		Spark功能的主要入口点。代表到Spark集群的连接，可以创建RDD、累加器和广播变量.
		每个JVM只能激活一个SparkContext对象，在创建sc之前需要stop掉active的sc。

	[RDD]
		resilient distributed dataset,弹性分布式数据集。等价于集合。
