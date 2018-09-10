

## SPARK command

* spark集群启动命令 

`start-all.sh`

* spark集群停止命令

`stop-all.sh`

* spark集群UI界面 :

`http://IP:8080`

*  本地模式启动
单机模式：通过本地 N 个线程跑任务，只运行一个 SparkSubmit 进程。

`spark-shell local[2]`

* spark-shlle 连接集群启动

`spart-shlle --master spark://hadoop-node-1:7077 --executor-memory 1g --total-executor-cores 2`


