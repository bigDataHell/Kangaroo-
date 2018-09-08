
## 1  高阶函数

### 1.1 概念

Scala混合了面向对象和函数式的特性，我们通常将可以作为参数传递到方法中的表达式叫做函数。
在函数式编程语言中，函数是“头等公民”，高阶函数包含：作为值的函数、匿名函数、闭包、柯里化等等。

### 1.2 作为值的函数

可以像任何其他数据类型一样被传递和操作的函数，每当你想要给算法传入具体动作时这个特性就会变得非常有用。

``` scala
scala> val arr = Array(1,2,3,6)
arr: Array[Int] = Array(1, 2, 3, 6)

scala> val func1 = (x:Int) => x*2
func1: Int => Int = <function1>

scala> arr.map(func1)
res0: Array[Int] = Array(2, 4, 6, 12)

``` 

 
定义函数时格式：val 变量名 = (输入参数类型和个数) => 函数实现和返回值类型
“=”表示将函数赋给一个变量
“=>”左面表示输入参数名称、类型和个数，右边表示函数的实现和返回值类型

### 1.3 匿名函数

在Scala中，你不需要给每一个函数命名，没有将函数赋给变量的函数叫做匿名函数。

``` scala
scala> arr.map((x:Int)=>x*2)
res4: Array[Int] = Array(2, 4, 6, 12)

scala> arr.map(x=>x*2)
res5: Array[Int] = Array(2, 4, 6, 12)
#终极版
scala> arr.map(_*2)
res6: Array[Int] = Array(2, 4, 6, 12)
```

## 2柯里化

### 2.1什么是柯里化

柯里化(Currying)指的是把原来接受多个参数的函数变换成接受一个参数的函数过程，并且返回接受余下的参数且返回结果为一个新函数的技术。

```  scala

scala> def m3(x:Int)=(y:Int) => x*y
m3: (x: Int)Int => Int

scala> m3(1)
res10: Int => Int = <function1>

scala> m3(4)
res11: Int => Int = <function1>

scala> res11(4)
res12: Int = 16

scala> res10(5)
res14: Int = 5

```

总结 : cala柯里化风格的使用可以简化主函数的复杂度，提高主函数的自闭性，提高功能上的可扩张性、灵活性。可以编写出更加抽象,功能化和高效的函数式代码。

## 3	闭包

#### 3.1  什么是闭包

闭包是一个函数，返回值依赖于声明在函数外部的一个或多个变量。
闭包通常来讲可以简单的认为是可以访问不在当前作用域范围内的一个函数。

``` scala
/**
  * scala中的闭包
  * 闭包是一个函数，返回值依赖于声明在函数外部的一个或多个变量。
  */
object ClosureDemo {
  def main(args: Array[String]): Unit = {
       val y=10
      //变量y不处于其有效作用域时,函数还能够对变量进行访问

        val add=(x:Int)=>{
          x+y
        }
    //在add中有两个变量：x和y。其中的一个x是函数的形式参数，
    //在add方法被调用时，x被赋予一个新的值。
    // 然而，y不是形式参数，而是自由变量
    println(add(5)) // 结果15
  }
}

```

