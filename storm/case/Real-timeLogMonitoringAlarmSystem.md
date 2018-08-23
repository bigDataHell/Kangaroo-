
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



