## 1 Scala

Scala 是一种多范式的编程语言，其设计的初衷是要集成面向对象编程和函
数式编程的各种特性。Scala 运行于 Java 平台（Java 虚拟机），并兼容现有的
Java 程序。

官网 : http://www.scala-lang.org

__环境搭建__

* 下载安装包
`https://www.scala-lang.org/download/2.11.8.html`

* 配置环境变量

* idea安装scala插件
` https://plugins.jetbrains.com/plugin/1347-scala`

## 2 scala基础学习

#### 2.1 

``` scala 声明变量

//使用 val 定义的变量值是不可变的，相当于 java 里用 final 修饰的变量
 val i = 1
//使用 var 定义的变量是可变得，在 Scala 中鼓励使用 val
 var s =  "hello"
//Scala 编译器会自动推断变量的类型，必要的时候可以指定类型
//变量名在前，类型在后
 val str: String =  "dabai"
```
#### 2.2 常用类型

Scala 和 Java 一样，有 7 种数值类型` Byte`、`Char`、`Short`、`Int`、`Long`、`Float`、`Double`
类型和 1 个 Boolean 类型。

*  Any
  在scala中，Any类是所有类的超类
  
* Any有两个子类：AnyVal和AnyRef

 __AnyVal__
* AnyVal 所有值类型的基类， 它描述的是值，而不是代表一个对象。 
* 它包括 9个AnyVal 子类型：
	* scala.Double 
	* scala.Float 
	* scala.Long 
	* scala.Int 
	* scala.Char 
	* scala.Short 
	* scala.Byte 

上面是数字类型。

还包括scala.Unit 和 scala.Boolean 是非数字类型。


 __AnyRef__
*	是所有引用类型的基类。除了值类型，所有类型都继承自AnyRef 。


#### 2.3 条件表达式

Scala 的条件表达式比较简洁，定义变量时加上 if else 判断条件。例如：

``` scala
    val x = 1
    //判断x的值，将结果赋给y
    val y = if (x > 0) 1 else -1
    //打印y的值
    println(y)

    //支持混合类型表达式
    val z = if (x > 1) 1 else "error"
    //打印z的值
    println(z)

    //如果缺失else，相当于if (x > 2) 1 else ()
    val m = if (x > 2) 1
    println(m)

    //在scala中每个表达式都有值，scala中有个Unit类，用作不返回任何结果的方法的结果类型,相当于Java中的void，Unit只有一个实例值，写成()。
    val n = if (x > 2) 1 else ()
    println(n)

    //if和else if
    val k = if (x < 0) 0
    else if (x >= 1) 1 else -1

```

#### 2.4 块表达式

定义变量时用 {} 包含一系列表达式，其中块的最后一个表达式的值就是块的值。

``` scala
val a = 10
val b = 20
    //在scala中{}中包含一系列表达式，块中最后一个表达式的值就是块的值
    //下面就是一个块表达式
    val result = {
val c=b-a
val d=b-c
d   //块中最后一个表达式的值
}
 //result的值就是块表达式的结果
  println(result)
}
```

#### 2.5  循环

在scala中有for循环和while循环，用for循环比较多, 
for循环语法结构：for (i <- 表达式/数组/集合)

``` scla
   //for(i <- 表达式),表达式1 to 10返回一个Range（区间）
    //每次循环将区间中的一个值赋给i
    for (i <- 1 to 10)
      println(i)

    //for(i <- 数组)
    val arr = Array("a", "b", "c")
    for (i <- arr)
      println(i)

    //高级for循环
    //每个生成器都可以带一个条件，注意：if前面没有分号
    for(i <- 1 to 3; j <- 1 to 3 if i != j)
      print((10 * i + j) + " ")
    println()

    //for推导式：如果for循环的循环体以yield开始，则该循环会构建出一个集合
    //每次迭代生成集合中的一个值
    val v = for (i <- 1 to 10) yield i * 10
    println(v)
```

#### 2.6 调用方法和函数
 Scala中的+ - * / %等操作符的作用与Java一样，位操作符 & | ^ >> <<也一样。只是有一点特别的：这些操作符实际上是方法。例如： <br>
  a + b<br>
是如下方法调用的简写：

a.+(b)<br>
a 方法 b可以写成 a.方法(b)

#### 2.7 定义方法

``` scala

    //可以不指定返回值的类型
    def method(x: Int, y: Int) = {
      val c = x * y
      c
    }

    // 递归方法必须指定返回值类型
    def method2(x:Int): Int = {
      if(x == 1) 1
      else method2(x-1) * x
    }
    
 ``` 
 ####  2.8 定义函数




