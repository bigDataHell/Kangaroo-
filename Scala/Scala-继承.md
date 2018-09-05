
##  1 cala中继承(extends)的概念

* Scala 中，让子类继承父类，与 Java 一样，也是使用 extends 关键字；
* 继承就代表，子类可继承父类的 field 和 method ，然后子类还可以在自己的内部实现父类没有的，子类特有的 field 和method，使用继承可以有效复用代码；
* 子类可以覆盖父类的 field 和 method，但是如果父类用 final 修饰，或者 field 和 method 用 final 修饰，则该类是无法被继承的，或者 field 和 method 是无法被覆盖的。
* private 修饰的 field 和 method 不可以被子类继承，只能在类的内部使用；
* field 必须要被定义成 val 的形式才能被继承，并且还要使用 override 关键字。 因为 var 修饰的 field 是可变的，在子类中可直接引用被赋值，不需要被继承；即 val 修饰的才允许被继承，var 修饰的只允许被引用。继承就是改变、覆盖的意思。
* Java 中的访问控制权限，同样适用于 Scala

 ||内部类|本包|子类| 外部包|
|:---:|:---:|:---:|:---:|:---:|
|public|√|√|√|√|
|protected|√|√|√|x|
|default|√|√|x|x|
|private|√|√|x|x|

``` scala
class Person {
  val name = "super"
  def getName = this.name
}

class Student extends Person {
  //继承加上关键字
  override
  val name = "sub"
  //子类可以定义自己的field和method
  val score = "A"

  def getScore = this.score
}
``` 

## 2  Scala中override 和 super 关键字

* Scala中，如果子类要覆盖父类中的一个非抽象方法，必须要使用 override 关键字；子类可以覆盖父类的 val 修饰的field，只要在子类中使用 override 关键字即可。

* override 关键字可以帮助开发者尽早的发现代码中的错误，比如， override 修饰的父类方法的方法名拼写错误。

* 此外，在子类覆盖父类方法后，如果在子类中要调用父类中被覆盖的方法，则必须要使用 super 关键字，显示的指出要调用的父类方法。


``` scala
class Person1 {
  //只能类内部使用
  private val name = "leo"
  val age = 50

  def getName = this.name
}

class Student extends Person1 {
  private val score = "A"
  //子类可以覆盖父类的 val field,使用override关键字
  override val age = 30

  def getScore = this.score

  //覆盖父类非抽象方法，必须要使用 override 关键字
  //同时调用父类的方法，使用super关键字
  override def getName = "your name is " + super.getName
}
```

## 3 Scala中isInstanceOf 和 asInstanceOf

如果实例化了子类的对象，但是将其赋予了父类类型的变量，在后续的过程中，又需要将父类类型的变量转换为子类类型的变量，应该如何做？

* 首先，需要使用 isInstanceOf 判断对象是否为指定类的对象，如果是的话，则可以使用 asInstanceOf 将对象转换为指定类型；
* 注意： p.isInstanceOf[XX] 判断 p 是否为 XX 对象的实例；p.asInstanceOf[XX] 把 p 转换成 XX 对象的实例
* 注意：如果没有用 isInstanceOf 先判断对象是否为指定类的实例，就直接用 asInstanceOf 转换，则可能会抛出异常；
* 注意：如果对象是 null，则 isInstanceOf 一定返回 false， asInstanceOf 一定返回 null；
* Scala与Java类型检查和转换


## 4  Scala中getClass 和 classOf

* __isInstanceOf 只能判断出对象是否为指定类以及其子类的对象，而不能精确的判断出，对象就是指定类的对象.__
* 如果要求精确地判断出对象就是指定类的对象，那么就只能使用 getClass 和 classOf 了；
* p.getClass 可以精确地获取对象的类，classOf[XX] 可以精确的获取类，然后使用 == 操作符即可判断；

``` scala
class Person4 {}
class Student4 extends Person4
object Student4{
  def main(args: Array[String]) {
    val p:Person4=new Student4
    //判断p是否为Person4类的实例
    println(p.isInstanceOf[Person4])//true
    //判断p的类型是否为Person4类
    println(p.getClass == classOf[Person4])//false
    //判断p的类型是否为Student4类
    println(p.getClass == classOf[Student4])//true

    val stu : Student4=new Student4
    println(stu.getClass == classOf[Student4]) //true
  }
}
```

## 5 Scala中使用模式匹配进行类型判断

* 在实际的开发中，比如 spark 源码中，大量的地方使用了模式匹配的语法进行类型的判断，这种方式更加地简洁明了，而且代码的可维护性和可扩展性也非常高；
* 使用模式匹配，功能性上来说，与 isInstanceOf 的作用一样，__主要判断是否为该类或其子类的对象即可，不是精准判断。__
* 等同于 Java 中的 switch case 语法；

``` scala
class Person5 {}
class Student5 extends Person5
object Student5{
  def main(args: Array[String]) {
    val p:Person5=new Student5
    p match {
      // 匹配是否为Person类或其子类对象
      // person就代表ps,可以在 => 后使用这个变量
      case person:Person5 => println("This is a Person5's Object!")
      // 匹配所有剩余情况
      case _  =>println("Unknown type!")
    }
  }
}

```

## 6 Scala中protected

* 跟 Java 一样，Scala 中同样可使用 protected 关键字来修饰 field 和 method。在子类中，可直接访问父类的 field 和 method，而不需要使用 super 关键字；

* 还可以使用 protected[this] 关键字， 访问权限的保护范围：只允许在当前子类中访问父类的 field 和 method，不允许通过其他子类对象访问父类的 field 和 method。

``` scala
class Person6 {
  protected var name: String = "tom"
  protected[this] var hobby: String = "game"
  protected def sayBye = println("再见...")
}

class Student6 extends Person6 {
  //父类使用protected 关键字来修饰 field可以直接访问
  def sayHello = println("Hello " + name)

  //父类使用protected 关键字来修饰method可以直接访问
  def sayByeBye = sayBye

  def makeFriends(s: Student6) = {
    println("My hobby is " + hobby + ", your hobby is UnKnown")
  }
}

object Student6 {
  def main(args: Array[String]) {
    val s: Student6 = new Student6
    s.sayHello
    s.makeFriends(s)
    s.sayByeBye
  }
}
```

## 7 Scala中调用父类的constructor

* Scala中，每个类都可以有一个主constructor和任意多个辅助constructor，而且每个辅助constructor的第一行都必须调用其他辅助constructor或者主constructor代码；因此子类的辅助constructor是一定不可能直接调用父类的constructor的；

* 只能在子类的主constructor中调用父类的constructor。

* 如果父类的构造函数已经定义过的 field，比如name和age，子类再使用时，就不要用 val 或 var 来修饰了，否则会被认为，子类要覆盖父类的field，且要求一定要使用 override 关键字。

``` scala

class Person7(val name:String,val age:Int){
  var score :Double=0.0
  var address:String="beijing"
  def this(name:String,score:Double)={
    //每个辅助constructor的第一行都必须调用其他辅助constructor或者主constructor代码
    //主constructor代码
    this(name,30)
    this.score=score
  }
  //其他辅助constructor
  def this(name:String,address:String)={
    this(name,100.0)
    this.address=address
  }
}
//student7的主构造器调用了父类的辅助构造器
class Student7(name:String,score:Double) extends Person7(name,score)
```
## 8 Scala中匿名内部类

* 在Scala中，匿名内部类是非常常见的，而且功能强大。Spark的源码中大量的使用了匿名内部类；

* 匿名内部类，就是定义一个没有名称的子类，并直接创建其对象，然后将对象的引用赋予一个变量，即匿名内部类的实例化对象。然后将该对象传递给其他函数使用。

作用: 1 给外部类动态的添加方法 <br>
      2 重写外部类的方法.


``` scala

class Person8(val name: String) {
  def sayHello = "Hello ,I'm " + name
}

class GreetDemo {
  //接受Person8参数，并规定Person8类只含有一个返回String的sayHello方法
  def greeting(p: Person8 {
    def sayHello: String}) = {
    println(p.sayHello)
  }
}

object GreetDemo {
  def main(args: Array[String]) {
    //创建Person8的匿名子类对象
    val p = new Person8("tom") {
      override def sayHello: String = {
        "hello,mmmmmmmmmmmm"
      }
    }
    val g = new GreetDemo
    g.greeting(p)
    println(p.sayHello)
  }
}
```

## 9 Scala中抽象类

* 如果在父类中，有某些方法无法立即实现，而需要依赖不同的子类来覆盖，重写实现不同的方法。此时，可以将父类中的这些方法编写成只含有方法签名，不含方法体的形式，这种形式就叫做抽象方法;
* 一个类中，如果含有一个抽象方法或抽象field，就必须使用abstract将类声明为抽象类，该类是不可以被实例化的;
* 在子类中覆盖抽象类的抽象方法时，可以不加override关键字;
* 如果在父类中，定义了field，但是没有给出初始值，则此field为抽象field；

``` scala
abstract class Person9(val name: String) {
  //必须指出返回类型，不然默认返回为Unit
  def sayHello: String
  def sayBye: String
  //抽象fields
  val age: Int
}
class Student9(name: String) extends Person9(name) {
  val age = 0;
  //必须指出返回类型，不然默认
  def sayHello: String = "Hello," + name
  def sayBye: String = "Bye," + name
}

object Student9 {
  def main(args: Array[String]) {
    val s = new Student9("tom")
    println(s.sayHello)
    println(s.sayBye)
  }
}
```










