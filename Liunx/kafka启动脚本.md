

### 1 在 /etc/profile 中配置:

``` 
#set kafka env
export KAFKA_HOME=/export/server/kafka
export PATH=${KAFKA_HOME}/bin:$PATH
```

**source /ect/profile**

### 2 在kafka的bin目录下新建三个文件

* startkafka.sh

```
cat /export/server/kafka/bin/slave | while read line
do
{
 echo $line
 ssh $line "source /etc/profile;nohup kafka-server-start.sh /export/server/kafka/config/server.properties >/dev/null 2>&1 &"
}&
wait
done 


```

* stopkafka.sh

```
cat /export/server/kafka/bin/slave | while read line
do
{
 echo $line
 ssh $line "source /etc/profile;jps |grep Kafka |cut -c 1-4 |xargs kill -s 9 "
}&
wait
done 

```

* save 配置主机名或者IP地址

```
hadoop-node-1
hadoop-node-2
hadoop-node-3

```

