
## 业务背景:

根据订单mq，快速计算双11当天的订单量、销售金额。

## 架构设计及思路

支付系统+kafka+storm/Jstorm集群+redis集群

1、支付系统发送mq到kafka集群中，编写storm程序消费kafka的数据并计算实时的订单数量、订单数量<br>
2、将计算的实时结果保存在redis中<br>
3、外部程序访问redis的数据实时展示结果<br>

## 数据准备

随机生成:

订单编号、订单时间、支付编号、支付时间、商品编号、商家名称、商品价格、优惠价格、支付金额

![实时交易01](https://github.com/bigDataHell/Kangaroo-/blob/master/images/storm_%E5%AE%9E%E6%97%B6%E4%BA%A4%E6%98%9301.png)


## 业务口径

![实时交易02](https://github.com/bigDataHell/Kangaroo-/blob/master/images/storm_%E5%AE%9E%E6%97%B6%E4%BA%A4%E6%98%9302.png)

* 订单总数：一条支付信息当一条订单处理，假设订单信息不会重发（实际情况要考虑订单去重的情况，父子订单等多种情况），计算接收到MQ的总条数，即当做订单数。
*	销售额：累加所有的订单中商品的价格
*	支付金额：累加所有订单中商品的支付价格
*	用户人数：一条支付信息当一个人处理，假设订单一个人只下一单（实际情况要考虑用户去重的情况）。

整体淘宝的业务指标，每个品类，每个产品线，每个淘宝店，每个商品

**Redis Key如何设计？**

Index：{pid}：{date}   value <br>
Index:1290:20160526   value <br>
Index:1291:20160526   value <br>
Index:1292:20160526   value <br>
Index:1293:20160526   value <br>
Index:1294:20160526   value <br>

## 数据展示

读取redis中的数据，每秒进行展示，打印在控制台。

## 工程设计

	数据产生：编写kafka数据生产者，模拟订单系统发送mq <br>
	数据输入：使用PaymentSpout消费kafka中的数据<br>
	数据计算：使用CountBolt对数据进行统计<br>
	数据存储：使用Sava2RedisBolt对数据进行存储，将结果数据存储到redis中<br>


1、获取外部数据源，MQSpout----Open(连接你的RMQ)---nextTuple()-----emit（json）

2、ParserPaymentInfoBolt()----execute(Tuple)------解析Json----JavaBean<br>
   productId,orderId,time,price（原价，订单价，优惠价，支付价），user，收货地址<br>
   total：原价、total：订单价、total：订单人数……
   
3、Save2ReidsBolt，保存相关业务指标

  问题：   在redis中存放整个网站销售的原价，  b:t:p:20160410 ---> value <br>
	         redis:   String----> value1+value2 + value3 + value4  incrBy
     
b:t:p:20160410
b:t:p:20161111
b:t:p:20160412


分析: 


![实时分析03](https://github.com/bigDataHell/Kangaroo-/blob/master/images/storm_%E5%AE%9E%E6%97%B6%E5%88%86%E6%9E%9003.png)





