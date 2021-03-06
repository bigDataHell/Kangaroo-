## 1 目标

* 介绍企业监控系统的一种实现方式
* 集成Flume+Kafka+Storm+Redis
* 配置Flume脚本及自定义Flume的拦截器
* 规划Kafka集群及设计Topic的分片&副本数量
* Storm整合Mysql数据库
* Storm集成发送短信、邮件功能
* 定时将数据从mysql数据中更新到Storm程序
 	
  
## 2 开发步骤

1）创建数据库的表架构、初始化业务系统名称、业务系统需要监控的字段、业务系统的开发人员（手机号、邮箱地址） <br>
2）编写Flume脚本去收集数据 <br>
3）创建Kafka的topic <br>
4）编写Storm程序。 <br>

## 3 数据库表结构

* 1）用户表（开发人员表）
用户编号、用户名称、用户手机号、用户邮箱地址、是否可用

![用户表](https://github.com/bigDataHell/Kangaroo-/blob/master/images/storm_userTable.png)

* 2）应用程序表
用来保存应用的信息，包括应用名称、应用描述、应用是否在线等信息

![应用程序表](https://github.com/bigDataHell/Kangaroo-/blob/master/images/storm_appTable.png)

* 3）应用类型表
用来保存应用类型编号,应用类型名称，如linux，web，java，itcast.bi;应用类型录入时间 ;应用类型修改时间 

![应用类型表](https://github.com/bigDataHell/Kangaroo-/blob/master/images/storm_appTypeTable.png)



* 4）规则表（每个应用系统要监控哪些规则）

![规则表](https://github.com/bigDataHell/Kangaroo-/blob/master/images/%E5%AE%9E%E6%97%B6%E6%97%A5%E5%BF%97%E7%9B%91%E6%8E%A7%E8%AD%A6%E5%91%8A01.png)
 
* 5）结果表
用来保存触发规则后的记录，包括告警编号、是否短信告知、是否邮件告知、告警明细等信息。

![结果表](https://github.com/bigDataHell/Kangaroo-/blob/master/images/%E5%AE%9E%E6%97%B6%E6%97%A5%E5%BF%97%E7%9B%91%E6%8E%A7%E8%AD%A6%E5%91%8A02.png)
 

## 4 Flume+Kafka整合

**storm或可以直接使用log4j2收集日志信息**

![日志采集](https://github.com/bigDataHell/Kangaroo-/blob/master/images/storm_%E6%97%A5%E5%BF%97%E9%87%87%E9%9B%86.png)

* 1 flume配置文件 :/export/server/flume/myconfig/app_interceptor/app_interceptor.conf

``` 
a1.sources = r1
a1.channels = c1
a1.sinks = k1

a1.sources.r1.type = exec
a1.sources.r1.command = tail -F /export/data/flume/click_log/error.log
a1.sources.r1.channels = c1
# l连接器id
a1.sources.r1.interceptors = i1
a1.sources.r1.interceptors.i1.type = cn.itcast.realtime.flume.AppInterceptor$AppInterceptorBuilder
# 加入的数据
a1.sources.r1.interceptors.i1.appId = 1

a1.channels.c1.type=memory
a1.channels.c1.capacity=10000
a1.channels.c1.transactionCapacity=100

a1.sinks.k1.type = org.apache.flume.sink.kafka.KafkaSink
a1.sinks.k1.topic = log_monitor
a1.sinks.k1.brokerList = hadoop-node-1:9092
a1.sinks.k1.requiredAcks = 1
a1.sinks.k1.batchSize = 20
a1.sinks.k1.channel = c1
``` 

小知识点:

>`tail -f`      等同于--follow=descriptor，根据文件描述符进行追踪，当文件改名或被删除，追踪停止 <br>
`tail -F`     等同于--follow=name  --retry，根据文件名进行追踪，并保持重试，即该文件被删除或改名后，如果再次创建相同的文件名，会继续追踪 <br>
`tailf`        等同于tail -f -n 10（貌似tail -f或-F默认也是打印最后10行，然后追踪文件），与tail -f不同的是，如果文件不增长，它不会去访问磁盘文件，所以tailf特别适合那些便携机上跟踪日志文件，因为它减少了磁盘访问，可以省电

* 2 将 itcast-realtime-flume-log.jar 和 loggen.sh 复制到 /export/server/flume/myconfig/app_interceptor 文件夹下 模拟日志生成
* 3 将 itcast-realtime-flume-1.0.jar 自定义拦截器 放入到flume的lib文件夹下
* 3 开始采集数据

  * 运行 loggen.sh 文件,开始产生日志.
  * 启动flume
  
./flume-ng agent -n a1 -c /export/server/flume/conf conf -f /export/server/flume/myconfig/app_interceptor/app_interceptor.conf -Dflume.root.logger=DEBUG, console
   
* 4 创建主题

 kafka-topics.sh --create --zookeeper hadoop-node-1:2181 --topic log_monitor --partitions 6 --replication-factor 2 

* 5 消费主题

kafka-console-consumer.sh --zookeeper hadoop-node-1:2181  --topic log_monitor

## 5 编写storm程序

 * 1 异常信息的格式
 
aid:1||msg:error exception in thread main org.hibernate.MappingException: Unknown entity:. <br>
aid:1||msg:error java.lang.ArithmeticException:  by brzero  <br>
aid:1||msg:error symbol  : method createTempFile(java.lang.String,java.lang.String,java.lang.String)   <br>
aid:1||msg:error:Servlet.service() for servlet action threw exception java.lang.NullPointerException  <br>
aid:1||msg:error java.lang.IllegalArgumentException: Path index.jsp does not start with  <br>
aid:1||msg:error invalid method declaration; return type required  <br>
aid:1||msg:error C2143: syntax error: missing : before    <br>
aid:1||msg:error javax.servlet.jsp.JspException: Cannot find message resources under key org.apache.struts.action.MESSAGE  <br>
aid:1||msg:error fatal error C1083: Cannot open include file: R…….h: No such file or directory  <br>
aid:1||msg:error exception in thread main org.hibernate.MappingException: Unknown entity:.  <br>
aid:1||msg:java.sql.SQLException: You have an error in your SQL syntax;  <br>
aid:1||msg:error Exception in thread main java.lang.NumberFormatException: null 20. .  <br>
aid:1||msg:error The server encountered an internal error () that prevented it from fulfilling this request  <br>

* 2 分区,副本,并行度,worker,ack设置

  topic 有6个分区,2个副本,.  <br>
  worker : 2 个  <br>
  ProcessBolt : 6个,对应topic的6个分区, 接受kafka发送的数据.  <br>
  NotifyBolt  : 2个 发送短信和邮件  <br>
  Save2DBBolt  : 2个  将触发的规则消息保存到数据库
  


 
 
 
  
   
