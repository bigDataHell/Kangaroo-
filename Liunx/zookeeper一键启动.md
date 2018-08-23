
### 1 在 /etc/profile 中配置:

__最好在本用户中的profile中配置__

``` 
#set zookeeper env
export KAFKA_HOME=/export/server/zookeeper
export PATH=${KAFKA_HOME}/bin:$PATH
```

**source /ect/profile**

注意,在所有的liunx上都需要配置profile文件

### 2 在zookeeper的bin目录下新建三个文件

* startkafka.sh

```

cat /export/server/start_script/slave | while read line
do
{
 echo $line
 ssh $line "source /etc/profile;nohup zkServer.sh start >/dev/null 2>&1 &"
}&
wait
done 



```

* stopkafka.sh

```

cat /export/server/start_script/slave | while read line
do
{
 echo $line
 ssh $line "source /etc/profile;jps |grep QuorumPeerMain |cut -c 1-5 |xargs kill -s 9"
}&
wait
done 


```

* save 配置主机名或者IP地址

```
hadoop-node-1
hadoop-node-2
hadoop-node-3
