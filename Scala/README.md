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
 
 ``` scala
 val function0 = (x:Int,y:Int)=>{
      println("这是一个函数")
      x+y*x
    }
    
 ```
 #### 2.9 方法和函数的区别
 
在函数式编程语言中，函数是“头等公民”，它可以像任何其他数据类型一样被传递和操作，函数是一个对象，继承自FuctionN。 <br>
函数对象有apply、curried、toString、tupled这些方法。而方法不具有这些特性。 <br>
如果想把方法转换成一个函数，可以用方法名跟上下划线的方式。 <br>

案例：首先定义一个方法，再定义一个函数，然后将函数传递到方法里面

``` scala
    //定义一个函数
    val function0 = (x:Int,y:Int)=>{
      println("这是一个函数")
      x+y*x
    }
    // 函数的参数类型,函数的返回值类型,函数的参数
    def method (f:(Int,Int)=>Int) =f(3,5)
    val result = method(function0)
    println(result)
```

 * 将一个方法转换为函数
 
 ``` scala
 
     // 方法
    def method2 (x:Int,y:Int) = x*y
    // 转换为函数
    val fun = method2 _
    println(fun(3,6))
    
  ```
  
  
## 3 数组

#### 3.1 定长数组和变长数组

* 定长数组

__定常数组如果new出来,则每个元素的初始化值为0,长度为设置值__
__如不使用new,则为一个长度为1的数组,必须按照角标依次赋值.__

``` scala
    //定常数组,长度不可变,内容可变.
    //定义一个长度为5的数据,默认为0
    //注意：如果new，相当于调用了数组的apply方法，直接为数组赋值
    val arr = new Array[Int](5);
    // 赋值
    arr(0) = 1;
    //查看所有元素值
    println(arr.toBuffer)
    for (i <- arr) println(i)
 ```

* 变长数组

``` scala
     //变长数组（数组缓冲）
    //如果想使用数组缓冲，需要导入import scala.collection.mutable.ArrayBuffer包
    val ab = ArrayBuffer[Int]()
    //向数组缓冲的尾部追加一个元素
    //+=尾部追加元素
    ab += 1
    //追加多个元素
    ab += (2, 3, 4, 5)
    //追加一个数组++=
    ab ++= Array(6, 7)
    //追加一个数组缓冲
    ab ++= ArrayBuffer(8,9)
    //打印数组缓冲ab

    //在数组某个位置插入元素用insert，从某下标插入
    ab.insert(0, -1, 0)
    //删除数组某个位置的元素用remove  按照下标删除
    ab.remove(0)
    println(ab)


``` 

#### 3.2 遍历数组

``` scala
    val  arr = Array(3,4,5,8,12,45,67)

    //增强for循环
    for( i <- arr) print(i+" ")

    //好用的until会生成一个Range
    //reverse是将前面生成的Range反转
    for(i <- (0 until arr.length).reverse) print(arr(i)+" ")
```

#### 3.3 数组转换

__yield__ 关键字将原始的数组进行转换会产生一个新的数组，原始的数组不变

``` scala

    val arr = Array(3, 4, 5, 8, 12, 45, 67)

    val arr2 = for (i <- arr) yield i * 2
    val arr3 = for (i <- arr if (i % 2 == 0)) yield  i * 2
    //map更好用
    val arr4 = arr.map(_ * 2)
    //filter是过滤，接收一个返回值为boolean的函数
    //map相当于将数组中的每一个元素取出来，应用传进去的函数
    val arr5 = arr.filter(_ % 2 == 0).map(_ * 2)
```
 
#### 3.4 数组常用算法

``` scala

    println(arr.sum)
    println(arr.max)
    println(arr.min)
    //排序
    arr.sorted
```
## 4 映射

* 在Scala中，把哈希表这种数据结构叫做映射。

__在Scala中，有两种Map，一个是immutable包下的Map，该Map中的内容不可变；另一个是mutable包下的Map，该Map中的内容可变__

默认不可以更改值,如果要更改,则手动导入 `import scala.collection.mutable.Map`


* 构建映射
（1）构建映射格式

1、val map=Map(键 -> 值，键 -> 值....) <br>
2、利用元组构建  val map=Map((键，值),(键，值),(键，值)....)

* 获取和修改映射中的值

``` scala

    //创建Map方式一
    val map = Map(1 -> 2, "大白" -> "小白", "344" -> 456)
    // 值=map(键)
    println(map(1))
    println(map("344"))
    //如果映射中有值,就返回映射的值,没有就返回默认值.
    println(map.getOrElse(1, 2222))
    println(map.getOrElse(2, 2222))

    // 在mutable下 更改值
    map(1) = 3

    //创建Map方式二
    val map2 = Map((1, "one"), (2, "two"), (3, "there"))

    //获取所有的key
    val nameList = map2.map(_._1)
    //或取所有的value
    val resultList = map2.map(_._2)

```



__注意 :通常我们在创建一个集合是会用val这个关键字修饰一个变量（相当于java中的final），那么就意味着该变量的引用不可变，该引用中的内容是不是可变，取决于这个引用指向的集合的类型__

## 5 元组

映射是K/V对偶的集合，对偶是元组的最简单形式，元组可以装着多个不同类型的值。

创建元组

* （1）元组是不同类型的值的聚集；对偶是最简单的元组。
* （2）元组表示通过将不同的值用小括号括起来，即表示元组。









