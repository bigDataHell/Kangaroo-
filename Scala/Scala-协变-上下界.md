
## 1 协变、逆变、非变介绍

协变和逆变主要是用来解决参数化类型的泛化问题。Scala的协变与逆变是非常有特色的，完全解决了Java中泛型的一大缺憾；举例来说，Java中，如果有 A是 B的子类，但 Card[A] 却不是 Card[B] 的子类；而 Scala 中，只要灵活使用协变与逆变，就可以解决此类 Java 泛型问题；
由于参数化类型的参数（参数类型）是可变的，当两个参数化类型的参数是继承关系（可泛化），那被参数化的类型是否也可以泛化呢？Java中这种情况下是不可泛化的，然而Scala提供了三个选择，即协变(“+”)、逆变（“-”）和非变。
下面说一下三种情况的含义，首先假设有参数化特征Queue，那它可以有如下三种定义。

* (1)  trait Queue[T] {} 

这是非变情况。这种情况下，当类型B是类型A的子类型，则Queue[B]与Queue[A]没有任何从属关系，这种情况是和Java一样的。

* (2)  trait Queue[+T] {} 

	这是协变情况。这种情况下，当类型B是类型A的子类型，则Queue[B]也可以认为是Queue[A]的子类型，即Queue[B]可以泛化为Queue[A]。也就是被参数化类型的泛化方向与参数类型的方向是一致的，所以称为协变。
  
* (3)   trait Queue[-T] {} 

这是逆变情况。这种情况下，当类型B是类型A的子类型，则Queue[A]反过来可以认为是Queue[B]的子类型。也就是被参数化类型的泛化方向与参数类型的方向是相反的，所以称为逆变。 

## 协变、逆变、非变总结

*  C[+T]：如果A是B的子类，那么C[A]是C[B]的子类。
*  C[-T]：如果A是B的子类，那么C[B]是C[A]的子类。
*  	C[T]： 无论A和B是什么关系，C[A]和C[B]没有从属关系。

## 3 案例

``` scala
class Super

class Sub extends Super

//协变
class Temp1[+A](title: String)

//逆变
class Temp2[-A](title: String)

//非变
class Temp3[A](title: String)

object Covariance_demo {
  def main(args: Array[String]) {

    //支持协变 Temp1[Sub]还是Temp1[Super]的子类
    // 将子类的引用给父类
    val t1: Temp1[Super] = new Temp1[Sub]("hello scala!!!")

    //支持逆变 Temp1[Super]是Temp1[Sub]的子类
    val t2: Temp2[Sub] = new Temp2[Super]("hello scala!!!")

    //支持非变 Temp3[Super]与Temp3[Sub]没有从属关系，如下代码会报错
    //val t3: Temp3[Sub] = new Temp3[Super]("hello scala!!!")
    //val t4: Temp3[Super] = new Temp3[Sub]("hello scala!!!")
    println(t1.toString)
    println(t2.toString)
  }
}
```

## 3 上界、下界介绍

在指定泛型类型时，有时需要界定泛型类型的范围，而不是接收任意类型。比如，要求某个泛型类型，必须是某个类的子类，这样在程序中就可以放心的调用父类的方法，程序才能正常的使用与运行。此时，就可以使用上下边界Bounds的特性； 

__Scala的上下边界特性允许泛型类型是某个类的子类，或者是某个类的父类；__


(1) U >: T

__这是类型下界的定义，也就是U必须是类型T的父类(或本身，自己也可以认为是自己的父类)。__

(2) S <: T

__这是类型上界的定义，也就是S必须是类型T的子类（或本身，自己也可以认为是自己的子类)。__


