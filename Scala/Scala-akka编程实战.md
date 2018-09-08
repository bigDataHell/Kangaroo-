标：使用Akka实现一个简易版的spark通信框架


 
2.	 项目概述
2.1.	 需求
目前大多数的分布式架构底层通信都是通过RPC实现的，RPC框架非常多，比如前我们学过的Hadoop项目的RPC通信框架，但是Hadoop在设计之初就是为了运行长达数小时的批量而设计的，在某些极端的情况下，任务提交的延迟很高，所以Hadoop的RPC显得有些笨重。
Spark 的RPC是通过Akka类库实现的，Akka用Scala语言开发，基于Actor并发模型实现，Akka具有高可靠、高性能、可扩展等特点，使用Akka可以轻松实现分布式RPC功能。
2.2.	 Akka简介
Akka基于Actor模型，提供了一个用于构建可扩展的（Scalable）、弹性的（Resilient）、快速响应的（Responsive）应用程序的平台。
Actor模型：在计算机科学领域，Actor模型是一个并行计算（Concurrent Computation）模型，它把actor作为并行计算的基本元素来对待：为响应一个接收到的消息，一个actor能够自己做出一些决策，如创建更多的actor，或发送更多的消息，或者确定如何去响应接收到的下一个消息。
 
Actor是Akka中最核心的概念，它是一个封装了状态和行为的对象，Actor之间可以通过交换消息的方式进行通信，每个Actor都有自己的收件箱（Mailbox）。通过Actor能够简化锁及线程管理，可以非常容易地开发出正确地并发程序和并行系统，Actor具有如下特性：

(1)、提供了一种高级抽象，能够简化在并发（Concurrency）/并行（Parallelism）应用场景下的编程开发
（2）、提供了异步非阻塞的、高性能的事件驱动编程模型
（3）、超级轻量级事件处理（每GB堆内存几百万Actor）

3.	 项目实现
3.1.	 实战一：
利用Akka的actor编程模型，实现2个进程间的通信。
3.1.1.	 架构图
 
3.1.2.	 重要类介绍
ActorSystem：在Akka中，ActorSystem是一个重量级的结构，他需要分配多个线程，所以在实际应用中，ActorSystem通常是一个单例对象，我们可以使用这个ActorSystem创建很多Actor。
注意：
（1）、ActorSystem是一个进程中的老大，它负责创建和监督actor
（2）、ActorSystem是一个单例对象
（3）、actor负责通信

3.1.3.	 Actor
在Akka中，Actor负责通信，在Actor中有一些重要的生命周期方法。
（1）preStart()方法：该方法在Actor对象构造方法执行后执行，整个Actor生命周期中仅执行一次。
（2）receive()方法：该方法在Actor的preStart方法执行完成后执行，用于接收消息，会被反复执行。
