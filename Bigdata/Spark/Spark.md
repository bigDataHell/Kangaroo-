## Spark

	通用性。
-------------------
## Spark模块
	Spark Core			//核心库
	Spark SQL			//SQL
	Spark Streaming			//准实时计算。
	Spark MLlib			//机器学习库
	Spark graph			//图计算

--------------------
## Spark集群运行

	1.local			//本地模式
	2.standalone		//独立模式
	3.yarn			//yarn模式
	4.mesos			//mesos
--------------------
## start-all.sh

	start-master.sh	//RPC端口 7077
	start-slave.sh	spark://s201:7077
	
--------------------
## webui

	http://hadoop-node-1:8080

--------------------
## 添加针对scala文件的编译插件

* 要关闭maven的自动编译功能  settings --> Compiler --> Build Project automatically

``` xml


		<build>
			<sourceDirectory>src/main/java</sourceDirectory>
			<plugins>
				<plugin>
					<groupId>org.apache.maven.plugins</groupId>
					<artifactId>maven-compiler-plugin</artifactId>
					<configuration>
						<source>1.8</source>
						<target>1.8</target>
					</configuration>
				</plugin>
				<plugin>
					<groupId>net.alchim31.maven</groupId>
					<artifactId>scala-maven-plugin</artifactId>
					<version>3.2.2</version>
					<configuration>
						<recompileMode>incremental</recompileMode>
					</configuration>
					<executions>
						<execution>
							<goals>
								<goal>compile</goal>
								<goal>testCompile</goal>
							</goals>
						</execution>
					</executions>
				</plugin>
			</plugins>
		</build>
``` 
