## 1 什么是Scala  Actor

注：Scala Actor是scala 2.10.x版本及以前版本的Actor。 <br>
Scala在2.11.x版本中将Akka加入其中，作为其默认的Actor，老版本的Actor已经废弃


* 概念

Scala中的Actor能够实现并行编程的强大功能，它是基于事件模型的并发机制，Scala是运用消息的发送、接收来实现高并发的。<br>
Actor可以看作是一个个独立的实体，他们之间是毫无关联的。但是，他们可以通过消息来通信。一个Actor收到其他Actor的信息后，它可以根据需要作出各种相应。消息的类型可以是任意的，消息的内容也可以是任意的。

## 2 java并发编程与Scala Actor编程的区别

|java内置线程模型|Scala actor|
|:--:|:----:|
|"共享数据-锁"|share nothing|
|每一个object有一个monitor,监视多线程对共享数据的访问|不共享数据,actor之间通过message通讯|
|加锁的代码段有synchronized| |
|死锁问题| |
|每个线程内部是循序执行的|每个actor内部是循序执行的|


对于Java，我们都知道它的多线程实现需要对共享资源（变量、对象等）使用synchronized 关键字进行代码块同步、对象锁互斥等等。而且，常常一大块的try…catch语句块中加上wait方法、notify方法、notifyAll方法是让人很头疼的。原因就在于Java中多数使用的是可变状态的对象资源，对这些资源进行共享来实现多线程编程的话，控制好资源竞争与防止对象状态被意外修改是非常重要的，而对象状态的不变性也是较难以保证的。 
与Java的基于共享数据和锁的线程模型不同，Scala的actor包则提供了另外一种不共享任何数据、依赖消息传递的模型,从而进行并发编程。

### 3  Actor的执行顺序

* 1、首先调用start()方法启动Actor
* 2、调用start()方法后其act()方法会被执行
* 3、向Actor发送消息
* 4、act方法执行完成之后，程序会调用exit方法

## 4 发送消息的方式

`!`	      发送异步消息，没有返回值。 <br>
`!?`	    发送同步消息，等待返回值。 <br>
`!!`	    发送异步消息，返回值是 Future[Any]。

注意：Future 表示一个异步操作的结果状态，可能还没有实际完成的异步任务的结果 <br>
      Any  是所有类的超类，Future[Any]的泛型是异步操作结果的类型。

## 5 actor并发编程

怎么实现actor并发编程：

* 1、定义一个class或者是object继承Actor特质，注意导包import scala.actors.Actor
* 2、重写对应的act方法
* 3、调用Actor的start方法执行Actor
* 4、当act方法执行完成，整个程序运行结束

``` scala

class Actor01 extends Actor {
  // 重写act方法
  override def act(): Unit = {
    for (i <- 1 to 100) {
      println("actor : " + i)
    }
  }
}

class Actor02 extends  Actor{
  override def act(): Unit = {
    for (i <- 1 to 100){
      println( "actor02 : ---"+i)

    }
  }
}

object MyActor extends  App{
  //创建actor实例对象
  val actor = new Actor01
  val actor2 = new Actor02
  // 启动actor
  actor.start()
  actor2.start()

}

```
说明：上面分别调用了两个单例对象的start()方法，他们的act()方法会被执行，相同与在java中开启了两个线程，线程的run()方法会被执行 <br>
注意：这两个Actor是并行执行的，act()方法中的for循环执行完成后actor程序就退出了

## 6 actor发送、接受消息

怎么实现actor发送、接受消息

* 1、定义一个class或者是object继承Actor特质，注意导包import scala.actors.Actor
* 2、重写对应的act方法
* 3、调用Actor的start方法执行Actor
* 4、通过不同发送消息的方式对actor发送消息
* 5、act方法中通过receive方法接受消息并进行相应的处理
* 6、act方法执行完成之后，程序退出

``` scala
class Actor02 extends Actor{
  override def act(): Unit = {
    receive{
      case "start" => println("start.........")
    }
  }
}

object  MyActor3 extends App{
  val actor02 = new Actor02
  actor02.start()
  // 发送异步消息,无返回
  actor02 ! "start"
}
```
## 7 actor发送、接受消息



