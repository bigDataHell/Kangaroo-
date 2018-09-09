
## 1 需求



目前大多数的分布式架构底层通信都是通过RPC实现的，RPC框架非常多，比如前我们学过的Hadoop项目的RPC通信框架，但是Hadoop在设计之初就是为了运行长达数小时的批量而设计的，在某些极端的情况下，任务提交的延迟很高，所以Hadoop的RPC显得有些笨重。

 
## 2 项目概述

#### 2.1 需求

![01](https://github.com/bigDataHell/Kangaroo-/blob/master/images/actor01.png)

目前大多数的分布式架构底层通信都是通过RPC实现的，RPC框架非常多，比如前我们学过的Hadoop项目的RPC通信框架，但是Hadoop在设计之初就是为了运行长达数小时的批量而设计的，在某些极端的情况下，任务提交的延迟很高，所以Hadoop的RPC显得有些笨重。
Spark 的RPC是通过Akka类库实现的，Akka用Scala语言开发，基于Actor并发模型实现，Akka具有高可靠、高性能、可扩展等特点，使用Akka可以轻松实现分布式RPC功能。

#### 2.2Akka简介

Akka基于Actor模型，提供了一个用于构建可扩展的（Scalable）、弹性的（Resilient）、快速响应的（Responsive）应用程序的平台。
Actor模型：在计算机科学领域，Actor模型是一个并行计算（Concurrent Computation）模型，它把actor作为并行计算的基本元素来对待：为响应一个接收到的消息，一个actor能够自己做出一些决策，如创建更多的actor，或发送更多的消息，或者确定如何去响应接收到的下一个消息。
 
Actor是Akka中最核心的概念，它是一个封装了状态和行为的对象，Actor之间可以通过交换消息的方式进行通信，每个Actor都有自己的收件箱（Mailbox）。通过Actor能够简化锁及线程管理，可以非常容易地开发出正确地并发程序和并行系统，Actor具有如下特性：

*  (1)、提供了一种高级抽象，能够简化在并发（Concurrency）/并行（Parallelism）应用场景下的编程开发
* （2）、提供了异步非阻塞的、高性能的事件驱动编程模型
* （3）、超级轻量级事件处理（每GB堆内存几百万Actor）

## 3.项目实现

### 3.1.	 实战一：

利用Akka的actor编程模型，实现2个进程间的通信。

![01](https://github.com/bigDataHell/Kangaroo-/blob/master/images/actor02.png)

#### 3.1.1.	 架构图
 
#### 3.1.2.	 重要类介绍
ActorSystem：在Akka中，ActorSystem是一个重量级的结构，他需要分配多个线程，所以在实际应用中，ActorSystem通常是一个单例对象，我们可以使用这个ActorSystem创建很多Actor。

注意：

* （1）、ActorSystem是一个进程中的老大，它负责创建和监督actor
* （2）、ActorSystem是一个单例对象
* （3）、actor负责通信

#### 3.1.3.	 Actor

在Akka中，Actor负责通信，在Actor中有一些重要的生命周期方法。

* （1）preStart()方法：该方法在Actor对象构造方法执行后执行，整个Actor生命周期中仅执行一次。
* （2）receive()方法：该方法在Actor的preStart方法执行完成后执行，用于接收消息，会被反复执行。

__注意 : 在运行后会报错,要给main方法传参,具体设置看图: 

![03](https://github.com/bigDataHell/Kangaroo-/blob/master/images/Actor03.png)

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
