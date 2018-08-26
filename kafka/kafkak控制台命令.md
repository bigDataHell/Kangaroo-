

## 创建主题

kafka-topics.sh --create --zookeeper hadoop-node-1:2181 --topic system_log --partitions 6 --replication-factor 2 

`system_log` : 主题名 <br>
`partitions` : 分区数量 <br>
`replication-factor` :  副本数量 <br>

## 查看所有主题

kafka-topics.sh --zookeeper hadoop-node-1:2181 --list

任意节点即可


## 查看主题的详细信息

kafka-topics.sh --zookeeper hadoop-node-1:2181 --describe --topic system_log

## 控制台消费topic的数据

kafka-console-consumer.sh --zookeeper hadoop-node-1:2181 --from-beginning --topic system_log

`system_log` : 主题name

## 查看某个分区的最大或最小偏移量

* 最小 <br>
kafka-run-class.sh kafka.tools.GetOffsetShell --topic `system_log`  --time `-2` --broker-list hadoop-node-1:9092 --partitions `0`
* 最大 <br>
kafka-run-class.sh kafka.tools.GetOffsetShell --topic `system_log`  --time `-1` --broker-list hadoop-node-1:9092 --partitions `0`

## 增加topic分区数

kafka-topics.sh --zookeeper hadoop-node-1:2181  --alter --topic `system_log` --partitions `10`

## 删除topic，慎用，只会删除zookeeper中的元数据，消息文件须手动删除

kafka-run-class.sh kafka.admin.DeleteTopicCommand --zookeeper hadoop-node-1:2181 --topic `system_log`


