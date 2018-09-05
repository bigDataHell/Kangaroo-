

__Scala有一个十分强大的模式匹配机制，可以应用到很多场合：如switch语句、类型检查等。并且Scala还提供了样例类，对模式匹配进行了优化，可以快速进行匹配。__

## 1 匹配字符串

``` scala
object CaseDemo01 extends App{
  val arr = Array("hadoop", "zookeeper", "spark")
  val name = arr(Random.nextInt(arr.length))
  name match {
    case "hadoop"    => println("大数据分布式存储和计算框架...")
    case "zookeeper" => println("大数据分布式协调服务框架...")
    case "spark" => println("大数据分布式内存计算框架...")
    case _ => println("我不认识你")
  }
}
```

## 2 匹配类型

注意：case y: Double if(y >= 0) => ... <br>
模式匹配的时候还可以添加守卫条件。如不符合守卫条件，将掉入case _中。


``` scala
object CaseDemo01 extends App {
  val arr = Array("hello", 1, 2.0,CaseDemo01)
  val v = arr(Random.nextInt(4))
  println(v)
  v match {
    case x: Int => println("Int " + x)
    case y: Double if (y >= 0) => println("Double " + y)
    case z: String => println("String " + z)
    case _ => throw new Exception("not match exception")
  }
}
```

## 3  匹配数组、元组、集合

注意：在Scala中列表要么为空（Nil表示空列表）要么是一个head元素加上一个tail列表。 <br>
9 :: List(5, 2)  :: 操作符是将给定的头和尾创建一个新的列表 <br>
注意：:: 操作符是右结合的，如9 :: 5 :: 2 :: Nil相当于 9 :: (5 :: (2 :: Nil)) <br>

``` scala
object CaseDemo03 extends App {

  val arr = Array(0, 1, 3, 5)
  arr match {
    // 数组length = 3
    case Array(1, x, y) => println(x + " " + y)
    case Array(0) => println("only 0")
    // 数组 下标0 = 0
    case Array(0, _*) => println("0 ...")
    case _ => println("something else")
  }

  val lst = List(0)
  lst match {
    case 0 :: Nil => println("only 0")
    //有2个任意元素
    case x :: y :: Nil => println(s"x: $x y: $y")
    // 第一个为0,后边任意
    case 0 :: tail => println("0 ...")
    case _ => println("something else")
  }

  val tup = (1, 3, 7)
  tup match {
    case (1, x, y) => println(s"1, $x , $y")
    case (_, z, 5) => println(z)
    case _ => println("else")
  }
}
```

## 4 样例类

在Scala中样例类是一种特殊的类，可用于模式匹配。 <br>
定义形式： 

* case class 类型，是多例的，后面要跟构造参数。
* case object 类型，是单例的。

``` scala

case class SubmitTask(id: String, name: String)
case class HeartBeat(time: Long)
case object CheckTimeOutTask

object CaseDemo04 extends App {
  val arr = Array(CheckTimeOutTask, HeartBeat(12333), SubmitTask("0001", "task-0001"))

  arr(Random.nextInt(arr.length)) match {
    case SubmitTask(id, name) => {
      println(s"$id, $name")
    }
    case HeartBeat(time) => {
      println(s"$time")
      println(time)
    }
    case CheckTimeOutTask => {
      println("check")
    }
  }
}

```
## 5  Option类型

在Scala中Option类型用样例类来表示可能存在或者可能不存在的值(Option的子类有Some和None)。Some包装了某个值，None表示没有值

``` scala
object OptionDemo {
  def main(args: Array[String]) {
    val map = Map("a" -> 1, "b" -> 2)
    val v = map.get("b") match {
      //存在值
      case Some(i) => i
      //不存在值
      case None => 0
    }
    println(v)
    
    //更好的方式
    val v1 = map.getOrElse("c", 0)
    println(v1)
  }
}
```
## 6 7.6.	 偏函数

被包在花括号内没有match的一组case语句是一个偏函数，它是PartialFunction[A, B]的一个实例，A代表输入参数类型，B代表返回结果类型，常用作输入模式匹配，偏函数最大的特点就是它只接受和处理其参数定义域的一个子集。

``` scala
object PartialFuncDemo  {

  // 限定输入为String("one"/"two"), 输出为Int (1/2/-1)
  val func1: PartialFunction[String, Int] = {
    case "one" => 1
    case "two" => 2
    case _ => -1
  }

  //定义一个普通的方法,通过match和case实现偏函数的逻辑
  def func2(num: String) : Int = num match {
    case "one" => 1
    case "two" => 2
    case _ => -1
  }

  def main(args: Array[String]) {
    println(func1("one"))
    println(func2("one"))
  }
}

``` 


