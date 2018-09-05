##  1 将trait作为接口使用

* Scala中的trait是一种特殊的概念；
* 首先先将trait作为接口使用，此时的trait就与Java中的接口 (interface)非常类似；
* 在trait中可以定义抽象方法，就像抽象类中的抽象方法一样，只要不给出方法的方法体即可；
* 类可以使用extends关键字继承trait，注意，这里不是 implement，而是extends ，在Scala中没有 implement 的概念，无论继承类还是trait，统一都是 extends；
* 类继承后，必须实现其中的抽象方法，实现时，不需要使用 override 关键字；
* Scala不支持对类进行多继承，但是支持多重继承 trait，使用 with 关键字即可。

``` scala
trait HelloTrait {
  def sayHello(): Unit
}
trait MakeFriendsTrait {
  def makeFriends(c: Children): Unit
}
//多重继承 trait
class Children(val name: String) extends HelloTrait with MakeFriendsTrait with Cloneable with Serializable{
  def sayHello() =println("Hello, " + this.name)
  def makeFriends(c: Children) = println("Hello, my name is " + this.name + ", your name is " + c.name)
}
object Children{
  def main(args: Array[String]) {
    val c1=new Children("tom")
    val c2=new Children("jim")
    c1.sayHello()//Hello, tom
    c1.makeFriends(c2)//Hello, my name is tom, your name is jim
  }
}
```

## 2 在trait中定义具体的方法(重要)

* Scala中的trait不仅可以定义抽象方法，还可以定义具体的方法，此时 trait 更像是包含了通用方法的工具，可以认为trait还包含了类的功能。

``` scala
trait Logger {
  def log(message: String): Unit = println(message)
}

class PersonForLog(val name: String) extends Logger {
  def makeFriends(other: PersonForLog) = {
    println("Hello, " + other.name + "! My name is " + this.name + ", I miss you!!")
    this.log("makeFriends method is invoked with parameter PersonForLog[name = " + other.name + "]")
  }
}

object PersonForLog {
  def main(args: Array[String]) {
    val p1 = new PersonForLog("jack")
    val p2 = new PersonForLog("rose")
    p1.makeFriends(p2)
    //Hello, rose! My name is jack, I miss you!!
    // 使用参数PersonForLog [name = rose]调用makeFriends方法
    //makeFriens method is invoked with parameter PersonForLog[name = rose]
  }
}
```

## 3 在trait中定义具体field

* Scala 中的 trait 可以定义具体的 field，此时继承 trait 的子类就自动获得了 trait 中定义的 field；

* 但是这种获取 field 的方式与继承 class 的是不同的。 如果是继承 class 获取的 field ，实际上还是定义在父类中的；而继承 trait获取的 field，就直接被添加到子类中了。

``` scala

trait  PersonForField{
  val age : Int = 50
}
//继承 trait 获取的field直接被添加到子类中

class StudentExtendsField(name:String) extends PersonForField{
    
  def print: Unit ={

    println(this.name +"的年龄为"+this.age)
  }
}
object  Test extends  App{

  val stu : StudentExtendsField = new StudentExtendsField("大白")

  stu.print
}
```
## 4 在trait中定义抽象field

* Scala中的trait也能定义抽象field， 而trait中的具体方法也能基于抽象field编写；
* 继承trait的类，则必须覆盖抽象field，提供具体的值；

``` scala
rait SayHelloTrait {
  // 抽象field
  val msg: String

  // 方法会随着msg的改变而改变,不推荐这种方式.
  def sayHello(name: String) = println(msg + ", " + name)
}

class PersonForAbstractField(val name: String) extends SayHelloTrait {
  //必须覆盖抽象 field
  val msg = "Hello,trait"

  def makeFriends(other: PersonForAbstractField) = {
    this.sayHello(other.name)
    println("I'm " + this.name + ", I want to make friends with you!!")
  }
}

object PersonForAbstractField {
  def main(args: Array[String]) {
    val p1 = new PersonForAbstractField("Tom")
    val p2 = new PersonForAbstractField("Rose")
    p1.makeFriends(p2)
  }
}
```
## 5 在实例对象指定混入某个trait

* 可在创建类的对象时，为该对象指定混入某个trait，且只有混入了trait的对象才具有trait中的方法，而其他该类的对象则没有；
* 在创建对象时，使用 with 关键字指定混入某个 trait；	

``` scala
trait LoggedTrait {
  // 该方法为实现的具体方法
  def log(msg: String) = {}
}

trait MyLogger extends LoggedTrait {
  // 覆盖 log() 方法
  override def log(msg: String) = println("log: " + msg)
}

class PersonForMixTraitMethod(val name: String) extends LoggedTrait {
  def sayHello = {
    println("Hi, I'm " + this.name)
    log("sayHello method is invoked!")
  }
}

object PersonForMixTraitMethod {
  def main(args: Array[String]) {
    val tom = new PersonForMixTraitMethod("Tom").sayHello //结果为：Hi, I'm Tom
    // 使用 with 关键字，指定混入MyLogger trait
    val rose = new PersonForMixTraitMethod("Rose") with MyLogger
    rose.sayHello
    // 结果为：     Hi, I'm Rose
    // 结果为：     log: sayHello method is invoked!
  }
}
```
## 6 trait 调用链

* Scala中支持让类继承多个trait后，可依次调用多个trait中的同一个方法，只要让多个trait中的同一个方法，__在最后都依次执行 super 关键字即可；__
* 类中调用多个trait中都有的这个方法时，首先会从最右边的trait的方法开始执行，然后依次往左执行，形成一个调用链条；
* 这种特性非常强大，其实就是设计模式中责任链模式的一种具体实现；

``` scala
trait HandlerTrait {
  def handle(data: String) = {
    println("last one")
  }
}

trait DataValidHandlerTrait extends HandlerTrait {
  override def handle(data: String) = {
    println("check data: " + data)
    super.handle(data)
  }
}

trait SignatureValidHandlerTrait extends HandlerTrait {
  override def handle(data: String) = {
    println("check signature: " + data)
    super.handle(data)
  }
}

class PersonForRespLine(val name: String) extends SignatureValidHandlerTrait with DataValidHandlerTrait {
  def sayHello = {
    println("Hello, " + this.name)
    this.handle(this.name)
  }
}

object PersonForRespLine {
  def main(args: Array[String]) {
    val p = new PersonForRespLine("tom")
    p.sayHello
    //执行结果：
    //    Hello, tom
    //    check data: tom
    //    check signature: tom
    //    last one
  }
}
```

## 7 混合使用 trait 的具体方法和抽象方法

* 在 trait 中，可以混合使用具体方法和抽象方法；
* 可以让具体方法依赖于抽象方法，而抽象方法则可放到继承 trait的子类中去实现；
* 这种 trait 特性，其实就是设计模式中的模板设计模式的体现

``` scala
trait ValidTrait {
  //抽象方法
  def getName: String

  //具体方法，具体方法的返回值依赖于抽象方法
  def valid: Boolean = {
    "Tom".equals(this.getName)
  }
}

class PersonForValid(val name: String) extends ValidTrait {
  def getName: String = this.name
}

object PersonForValid {
  def main(args: Array[String]): Unit = {
    val person = new PersonForValid("Rose")
    println(person.valid)
  }
}
```

## 8 rait的构造机制

* 在Scala中，trait也是有构造代码的，即在trait中，不包含在任何方法中的代码；
* 继承了trait的子类，其构造机制如下：
* 父类的构造函数先执行， class 类必须放在最左边；多个trait从左向右依次执行；构造trait时，先构造父 trait，如果多个trait继承同一个父trait，则父trait只会构造一次；所有trait构造完毕之后，子类的构造函数最后执行。

``` scala

class Person_One {
  println("Person's constructor!")
}

trait Logger_One {
  println("Logger's constructor!")
}

trait MyLogger_One extends Logger_One {
  println("MyLogger's constructor!")
}

trait TimeLogger_One extends Logger_One {
  println("TimeLogger's contructor!")
}

class Student_One extends Person_One with MyLogger_One with TimeLogger_One {
  println("Student's constructor!")
}

object exe_one {
  def main(args: Array[String]): Unit = {
    val student = new Student_One
    //执行结果为：
    //      Person's constructor!
    //      Logger's constructor!
    //      MyLogger's constructor!
    //      TimeLogger's contructor!
    //      Student's constructor!
  }
}

```
## 9 trait 继承 class

* 在Scala中trait 也可以继承 class，此时这个 class 就会成为所有继承该 trait 的子类的超级父类。
``` scala
class MyUtil {
  def printMsg(msg: String) = println(msg)
}

trait Logger_Two extends MyUtil {
  def log(msg: String) = this.printMsg("log: " + msg)
}

class Person_Three(val name: String) extends Logger_Two {
  def sayHello {
    this.log("Hi, I'm " + this.name)
    this.printMsg("Hello, I'm " + this.name)
  }
}

object Person_Three {
  def main(args: Array[String]) {
    val p = new Person_Three("Tom")
    p.sayHello
    //执行结果：
    //      log: Hi, I'm Tom
    //      Hello, I'm Tom
  }
}
```







