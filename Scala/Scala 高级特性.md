
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

## 4 隐式转换和隐式参数

#### 4.1	  隐式转换

Scala提供的隐式转换和隐式参数功能，是非常有特色的功能。是Java等编程语言所没有的功能。它可以允许你手动指定，将某种类型的对象转换成其他类型的对象或者是给一个类增加方法。通过这些功能，可以实现非常强大、特殊的功能。

Scala的隐式转换，其实最核心的就是定义隐式转换方法，即implicit conversion function。定义的隐式转换方法，只要在编写的程序内引入，就会被Scala自动使用。Scala会根据隐式转换方法的签名，在程序中使用到隐式转换方法接收的参数类型定义的对象时，会自动将其传入隐式转换方法，转换为另外一种类型的对象并返回。这就是“隐式转换”。其中所有的隐式值和隐式方法必须放到object中。

然而使用Scala的隐式转换是有一定的限制的，总结如下：

* implicit关键字只能用来修饰方法、变量（参数)。
* 隐式转换的方法在当前范围内才有效。如果隐式转换不在当前范围内定义（比如定义在另一个类中或包含在某个对象中），那么必须通过import语句将其导。

#### 4.2 隐式参数

所谓的隐式参数，指的是在函数或者方法中，定义一个用implicit修饰的参数，此时Scala会尝试找到一个指定类型的，用implicit修饰的参数，即隐式值，并注入参数。

Scala会在两个范围内查找：

* 当前作用域内可见的val或var定义的隐式变量；
* 	一种是隐式参数类型的伴生对象内的隐式值；

#### 4.3隐式转换方法作用域与导入

（1）Scala默认会使用两种隐式转换，一种是源类型，或者目标类型的伴生对象内的隐式转换方法；一种是当前程序作用域内的可以用唯一标识符表示的隐式转换方法。

（2）如果隐式转换方法不在上述两种情况下的话，那么就必须手动使用import语法引入某个包下的隐式转换方法，比如`import test._`。通常建议，仅仅在需要进行隐式转换的地方，用import导入隐式转换方法，这样可以缩小隐式转换方法的作用域，避免不需要的隐式转换。

#### 4.4隐式转换的时机

（1）当对象调用类中不存在的方法或成员时，编译器会自动将对象进行隐式转换
（2）当方法中的参数的类型与目标类型不一致时 

#### 4.5 隐式转换和隐式参数案例


* 案例一 : （让File类具备RichFile类中的read方法）

``` scala
object MyPredef{
  //定义隐式转换方法
  implicit def file2RichFile(file: File)=new RichFile(file)
}
class RichFile(val f:File) {
  def read()=Source.fromFile(f).mkString
}
object RichFile{
  def main(args: Array[String]) {
    val f=new File("D:\\wordcount\\input\\3.txt")
    //使用import导入隐式转换方法
    import MyPredef._
    //通过隐式转换，让File类具备了RichFile类中的方法
    val content=f.read()
    println(content)

  }
}

```

* 案例二 （超人变身）
``` scala
class Man(val name:String)
class SuperMan(val name: String) {
  def heat=print("超人打怪兽")
}
object SuperMan{
  //隐式转换方法
  implicit def man2SuperMan(man:Man)=new SuperMan(man.name)
  def main(args: Array[String]) {
    val hero=new Man("hero")
    //Man具备了SuperMan的方法
    hero.heat
  }
}
```

* 案例三 (重要) （一个类隐式转换成具有相同方法的多个类）

``` scala
class A(c:C) {
  def readBook(): Unit ={
    println("A说：好书好书...")
  }
}
class B(c:C){
  def readBook(): Unit ={
    println("B说：看不懂...")
  }
  def writeBook(): Unit ={
    println("B说：不会写...")
  }
}
class C

object AB{
  //创建一个类的2个类的隐式转换
  implicit def C2A(c:C)=new A(c)
  implicit def C2B(c:C)=new B(c)
}
object B{
  def main(args: Array[String]) {
    //导包
    //1. import AB._ 会将AB类下的所有隐式转换导进来
    //2. import AB._C2A 只导入C类到A类的的隐式转换方法
    //3. import AB._C2B 只导入C类到B类的的隐式转换方法
    import AB._

    val c=new C
    //由于A类与B类中都有readBook()，只能导入其中一个，否则调用共同方法时代码报错
    //c.readBook()
    //C类可以执行B类中的writeBook()
    c.writeBook()
  }
}

```
* 案例四  （员工领取薪水）

``` scala
object Company{
  //在object中定义隐式值    注意：同一类型的隐式值只允许出现一次，否则会报错
  implicit  val aaa="zhangsan"
  implicit  val bbb=10000.00
}
class Boss {
  //注意参数匹配的类型   它需要的是String类型的隐式值
  def callName()(implicit name:String):String={
    name+" is coming !"
  }
  //定义一个用implicit修饰的参数
  //注意参数匹配的类型    它需要的是Double类型的隐式值
  def getMoney()(implicit money:Double):String={
    " 当月薪水："+money
  }
}
object Boss extends App{
  //使用import导入定义好的隐式值，注意：必须先加载否则会报错
  import Company._
  val boss =new Boss
  // 如果传参则报错
  println(boss.callName()+boss.getMoney())

}
```



