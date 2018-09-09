
## 1 需求



目前大多数的分布式架构底层通信都是通过RPC实现的，RPC框架非常多，比如前我们学过的Hadoop项目的RPC通信框架，但是Hadoop在设计之初就是为了运行长达数小时的批量而设计的，在某些极端的情况下，任务提交的延迟很高，所以Hadoop的RPC显得有些笨重。

 
## 2 项目概述

#### 2.1 需求


目前大多数的分布式架构底层通信都是通过RPC实现的，RPC框架非常多，比如前我们学过的Hadoop项目的RPC通信框架，但是Hadoop在设计之初就是为了运行长达数小时的批量而设计的，在某些极端的情况下，任务提交的延迟很高，所以Hadoop的RPC显得有些笨重。
Spark 的RPC是通过Akka类库实现的，Akka用Scala语言开发，基于Actor并发模型实现，Akka具有高可靠、高性能、可扩展等特点，使用Akka可以轻松实现分布式RPC功能。

#### 2.2Akka简介

![01](https://github.com/bigDataHell/Kangaroo-/blob/master/images/actor01.png)

Akka基于Actor模型，提供了一个用于构建可扩展的（Scalable）、弹性的（Resilient）、快速响应的（Responsive）应用程序的平台。
Actor模型：在计算机科学领域，Actor模型是一个并行计算（Concurrent Computation）模型，它把actor作为并行计算的基本元素来对待：为响应一个接收到的消息，一个actor能够自己做出一些决策，如创建更多的actor，或发送更多的消息，或者确定如何去响应接收到的下一个消息。
 
Actor是Akka中最核心的概念，它是一个封装了状态和行为的对象，Actor之间可以通过交换消息的方式进行通信，每个Actor都有自己的收件箱（Mailbox）。通过Actor能够简化锁及线程管理，可以非常容易地开发出正确地并发程序和并行系统，Actor具有如下特性：

*  (1)、提供了一种高级抽象，能够简化在并发（Concurrency）/并行（Parallelism）应用场景下的编程开发
* （2）、提供了异步非阻塞的、高性能的事件驱动编程模型
* （3）、超级轻量级事件处理（每GB堆内存几百万Actor）



## 3  实战一：

利用Akka的actor编程模型，实现2个进程间的通信。

![01](https://github.com/bigDataHell/Kangaroo-/blob/master/images/actor05.png)

#### 3.1.	 架构图

![01](https://github.com/bigDataHell/Kangaroo-/blob/master/images/actor02.png)
 
#### 3.2.	 重要类介绍
ActorSystem：在Akka中，ActorSystem是一个重量级的结构，他需要分配多个线程，所以在实际应用中，ActorSystem通常是一个单例对象，我们可以使用这个ActorSystem创建很多Actor。

注意：

* （1）、ActorSystem是一个进程中的老大，它负责创建和监督actor
* （2）、ActorSystem是一个单例对象
* （3）、actor负责通信

#### 3.3.	 Actor

在Akka中，Actor负责通信，在Actor中有一些重要的生命周期方法。

* （1）preStart()方法：该方法在Actor对象构造方法执行后执行，整个Actor生命周期中仅执行一次。
* （2）receive()方法：该方法在Actor的preStart方法执行完成后执行，用于接收消息，会被反复执行。

__注意 : 在运行后会报错,要给main方法传参,具体设置看图:__ 

![03](https://github.com/bigDataHell/Kangaroo-/blob/master/images/actor03.png)

#### POM
```xml

  <properties>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
    <encoding>UTF-8</encoding>
    <scala.version>2.11.8</scala.version>
    <scala.compat.version>2.11</scala.compat.version>
  </properties>

  <dependencies>
    <dependency>
      <groupId>org.scala-lang</groupId>
      <artifactId>scala-library</artifactId>
      <version>${scala.version}</version>
    </dependency>

    <dependency>
      <groupId>com.typesafe.akka</groupId>
      <artifactId>akka-actor_2.11</artifactId>
      <version>2.3.14</version>
    </dependency>

    <dependency>
      <groupId>com.typesafe.akka</groupId>
      <artifactId>akka-remote_2.11</artifactId>
      <version>2.3.14</version>
    </dependency>

  </dependencies>

  <build>
    <sourceDirectory>src/main/scala</sourceDirectory>
    <testSourceDirectory>src/test/scala</testSourceDirectory>
    <plugins>
      <plugin>
        <groupId>net.alchim31.maven</groupId>
        <artifactId>scala-maven-plugin</artifactId>
        <version>3.2.2</version>
        <executions>
          <execution>
            <goals>
              <goal>compile</goal>
              <goal>testCompile</goal>
            </goals>
            <configuration>
              <args>
                <arg>-dependencyfile</arg>
                <arg>${project.build.directory}/.scala_dependencies</arg>
              </args>
            </configuration>
          </execution>
        </executions>
      </plugin>

      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <version>2.4.3</version>
        <executions>
          <execution>
            <phase>package</phase>
            <goals>
              <goal>shade</goal>
            </goals>
            <configuration>
              <filters>
                <filter>
                  <artifact>*:*</artifact>
                  <excludes>
                    <exclude>META-INF/*.SF</exclude>
                    <exclude>META-INF/*.DSA</exclude>
                    <exclude>META-INF/*.RSA</exclude>
                  </excludes>
                </filter>
              </filters>
              <transformers>
                <transformer
                  implementation="org.apache.maven.plugins.shade.resource.AppendingTransformer">
                  <resource>reference.conf</resource>
                </transformer>
                <transformer
                  implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                  <mainClass></mainClass>
                </transformer>
              </transformers>
            </configuration>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
```

#### Master

``` scala
package cn.hzh.rpc

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

// todo  利用akka的actor模型实现两个进程之间的通信 ---- master端
class Master extends Actor {

  // 构造代码块先被执行
  println("Master constructor invoked")

  // 会在构造代码块执行之后被调用,且只会调用一次
  override def preStart(): Unit = {
    println("preStart method invoked")

  }

  // receive方会在preStart方法调用之后执行,表示不断的接收消息
  override def receive: Receive = {
    case "connect" => {
      println("我是客户端")
      // master发送注册成功的信息给worker
      sender ! "success"
    }
  }
}

object Master {

  def main(args: Array[String]): Unit = {
    //master的ip地址
    val host = args(0)
    //master的port端口
    val port = args(1)

    //准备配置文件信息
    val configStr =
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
      """.stripMargin

    //配置config对象 利用ConfigFactory解析配置文件，获取配置信息
    val config = ConfigFactory.parseString(configStr)

    // 1、创建ActorSystem,它是整个进程中老大，它负责创建和监督actor，它是单例对象
    val masterActorSystem = ActorSystem("masterActorSystem", config)
    // 2、通过ActorSystem来创建master actor
    val masterActor: ActorRef = masterActorSystem.actorOf(Props(new Master), "masterActor")
    // 3、向master actor发送消息
    //masterActor ! "connect"
  }
}

```

#### worker
``` scalal
package cn.hzh.rpc

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

// todo :利用akka中的actor实现2个进程之间的通信--------worker端
class Worker extends Actor {

  // 构造代码块先被执行
  println("Worker constructor invoked")

  // 会在构造代码块执行之后被调用,且只会调用一次
  override def preStart(): Unit = {
    println("Worker method invoked")
    // 获取master actor的引用
    //ActorContext : 全局变量 可以通过已经存在的actor中,寻找目标actor.
    // 调用对应的actor Selection方法,需要一个path路径: 1 通信协议 2 master的IP地址 3 master的端口
    // 4 创建masterActor老大  5 actor层级
    val master = context
        .actorSelection("akka.tcp://masterActorSystem@192.168.3.18:8888/user/masterActor")

    // 向master actor发送消息
    master ! "connect"


  }

  // receive方会在preStart方法调用之后执行,表示不断的接收消息
  override def receive: Receive = {
    case "connect" => {
      println("我是Worker")
    }
    case "success" => {
      println("注册成功")
    }
  }
}

object Worker {

  def main(args: Array[String]): Unit = {

    //worker的ip地址
    val host = args(0)
    //worker的port端口
    val port = args(1)

    val configStr =
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
      """.stripMargin

    val conf = ConfigFactory.parseString(configStr)
    //1 创建ActorSystem,它是整个进程的老大,负责监督和创建actor
    val workerActorSystem = ActorSystem("workerActorSystem", conf)
    //2 通过ActorSystem来创建 worker actor
    val workerActory: ActorRef = workerActorSystem.actorOf(Props(new Worker), "workerActory")
    //workerActory ! "connect"

  }
}


```
##  4	实战二

使用Akka实现一个简易版的spark通信框架

![01](https://github.com/bigDataHell/Kangaroo-/blob/master/images/actor04.png)

#### 4.1 代码开发

* Master

``` scala
package cn.hzh.spark

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.duration._

// todo  利用akka实现简易版的spark通信框架 ---- master端
class Master extends Actor {

  // 构造代码块先被执行
  println("Master constructor invoked")

  // 定义一个map集合,封装worker信息
  private val workerMap = new mutable.HashMap[String, WorkerInfo]()

  // 定义一个List集合,用来存放WorkInfo信息,方便后期按照worker上的资源进行排序
  private val workerList = new mutable.ListBuffer[WorkerInfo]()

  // master定时检查的时间间隔
  val CHECK_OUT_TIME_INTERVAL = 15000 //15秒

  // 会在构造代码块执行之后被调用,且只会调用一次
  override def preStart(): Unit = {
    println("preStart method invoked")

    //master定时检查超时的worker
    // 需要手动导入隐式转换
    import context.dispatcher
    context.system.scheduler.schedule(0 millis, CHECK_OUT_TIME_INTERVAL millis, self, CheckOutTime)
  }

  // receive方会在preStart方法调用之后执行,表示不断的接收消息
  override def receive: Receive = {
    // master接受worker的注册信息
    case RegisterMessage(workerId, memory, cores) => {
      // 判断当前worker是否已经注册
      if (!workerMap.contains(workerId)) {
        //保存信息到map集合中
        val workerInfo = new WorkerInfo(workerId, memory, cores)
        workerMap.put(workerId, workerInfo)
        // 保存workerInfo信息到list集合中
        workerList += workerInfo
        //master反馈注册成功给worker
        sender ! RegisteredMessage(s"workerId: $workerId 注册成功")
      }
    }
    //master接受worker的心跳信息
    case SendheartBeat(workerId) => {
      // 判断worker是否已经注册,master只接受已经注册过的worker心跳
      if (workerMap.contains(workerId)) {
        //获取workerInfo信息
        val workerInfo: WorkerInfo = workerMap(workerId)
        // 获取当前系统时间
        val lastTime: Long = System.currentTimeMillis()
        workerInfo.lastHeartBeatTime = lastTime
      }
    }
    case CheckOutTime => {
      //过滤出超时的worker, 判断逻辑 :获取对当前的系统时间 - worker上一次的心跳时间 >master定时检查的时间间隔
      val allOutTimeWorker: ListBuffer[WorkerInfo] = workerList
          .filter(x => System.currentTimeMillis() - x.lastHeartBeatTime > CHECK_OUT_TIME_INTERVAL)

      // 遍历超时的worker信息,然后移除掉超时的worker
      for (workerInfo <- allOutTimeWorker) {
        // Map移除
        workerMap.remove(workerInfo.workerId)
        // List移除
        workerList -= workerInfo

        println("超时的workerId : " + workerInfo.workerId)
      }
      println("活着的worker总数 :" + workerList.size)
      // master按照worker内存大小进行降序排序
      println(workerList.sortBy(x => x.memory).reverse.toList)
    }
  }
}

object Master {

  def main(args: Array[String]): Unit = {
    //master的ip地址
    val host = args(0)
    //master的port端口
    val port = args(1)

    //准备配置文件信息
    val configStr =
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
      """.stripMargin

    //配置config对象 利用ConfigFactory解析配置文件，获取配置信息
    val config = ConfigFactory.parseString(configStr)

    // 1、创建ActorSystem,它是整个进程中老大，它负责创建和监督actor，它是单例对象
    val masterActorSystem = ActorSystem("masterActorSystem", config)
    // 2、通过ActorSystem来创建master actor
    val masterActor: ActorRef = masterActorSystem.actorOf(Props(new Master), "masterActor")
    // 3、向master actor发送消息
    //masterActor ! "connect"
  }
}

```
* Worker

``` scala
package cn.hzh.spark

import java.util.UUID
import akka.actor.{Actor, ActorRef, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._

// todo :利用akka利用akka实现简易版的spark通信框架 --------worker端
class Worker(val memory: Int, val cores: Int, val masterHost: String,
    val masterPort: String) extends Actor {

  // 定义workerID
  private val workerId: String = UUID.randomUUID().toString

  // 定义发送心跳的时间间隔
  val SEND_HEART_HEAT_INERVAL = 10000 // 10秒

  // 定义全局变量
  var master: ActorSelection = _

  // 构造代码块先被执行
  println("Worker constructor invoked")

  // 会在构造代码块执行之后被调用,且只会调用一次
  override def preStart(): Unit = {
    println("Worker method invoked")

    // 获取master actor的引用
    //ActorContext : 全局变量 可以通过已经存在的actor中,寻找目标actor.
    // 调用对应的actor Selection方法,需要一个path路径: 1 通信协议 2 master的IP地址 3 master的端口 4 创建masterActor老大  5 actor层级
    master = context
        .actorSelection(s"akka.tcp://masterActorSystem@$masterHost:$masterPort/user/masterActor")

    // 向master发送注册信息,将信息封装到样例类中,主要包含: WorkerId, memory,cores
    master ! RegisterMessage(workerId, memory, cores)

  }

  // receive方会在preStart方法调用之后执行,表示不断的接收消息
  override def receive: Receive = {
    case RegisteredMessage(message) => {
      // worker接受master的反馈信息
      println(message)
      // 向master发送心跳 参数: 1 延迟多久进行发送 2 间隔多久发送一次 3
      // worker先给自己发送心跳 self 可以当做是this的别名
      // 需要手动导入隐式转换
      import context.dispatcher
      context.system.scheduler.schedule(0 millis, SEND_HEART_HEAT_INERVAL millis, self, HeatBeat)
    }
    // worker自己发的心跳
    case HeatBeat => {
      // 这个时候才是正真的向master发送心跳
     master ! SendheartBeat(workerId)
      println("发送心跳++++++++++++++++++")
    }

  }
}

object Worker {

  def main(args: Array[String]): Unit = {

    //worker的ip地址
    val host = args(0)
    //worker的port端口
    val port = args(1)
    // 定义worker的内存
    val memory = args(2).toInt
    // 定义worker的核数
    val cores = args(3).toInt
    // 定义master的IP地址
    val masterHost = args(4)
    // 定义master的IP地址
    val masterPort = args(5)

    val configStr =
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
      """.stripMargin

    val conf = ConfigFactory.parseString(configStr)
    //1 创建ActorSystem,它是整个进程的老大,负责监督和创建actor
    val workerActorSystem = ActorSystem("workerActorSystem", conf)
    //2 通过ActorSystem来创建 worker actor
    val workerActory: ActorRef = workerActorSystem
        .actorOf(Props(new Worker(memory, cores, masterHost, masterPort)), "workerActory")

  }
}
``` 
* 样例类

``` scala
package cn.hzh.spark

// RemoteMessage : 远程消息
trait RemoteMessage extends Serializable {}

// Register Message : 注册消息
//  worker向master发送注册消息,由于不在同一进程中,需要实现序列化 1 workerID 2 内存 3  核数
case class RegisterMessage(workerId: String, memory: Int,
    cores: Int) extends RemoteMessage

// master反馈注册成功信息给worker,,由于不在同一进程中,需要实现序列化
case class RegisteredMessage(message: String) extends RemoteMessage

//worker向worker发送心跳,在同一进程中,不需要序列化
case object HeatBeat

// worker向master发送心跳,由于不在同一进程中,需要实现序列化
case class SendheartBeat(workerId: String) extends RemoteMessage

// 没有参数的样例类,直接 object即可
// master自己向自己发送消息,不需要序列化
case object CheckOutTime
``` 
* WorkerInfo

``` scala
package cn.hzh.spark

//封装worker信息
class WorkerInfo(val workerId: String, val memory: Int, val cores: Int) {

  // _ : 表示默认初始值为0
  // 定义一个变量,又来存放worker上一次心跳时间
  var lastHeartBeatTime: Long = _

  override def toString: String = {
    s"workerId : $workerId, memory : $memory, cores:$cores"
  }
}

``` 

#### 4.2 测试 

配置3个worker的configuration(组态),只要使每个worker的端口号即可

![01](https://github.com/bigDataHell/Kangaroo-/blob/master/images/actor06.png)

-------------------

![01](https://github.com/bigDataHell/Kangaroo-/blob/master/images/actor07.png)





