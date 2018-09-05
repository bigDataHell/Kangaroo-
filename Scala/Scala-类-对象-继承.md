
## 1 类的定义

``` scala

**
  * 在Scala中，类并不用声明为public类型的。
  * Scala源文件中可以包含多个类，所有这些类都具有共有可见性。
  */
class Person {
  //用val修饰的变量是可读属性，有getter但没有setter（相当与Java中用final修饰的变量）
  val id = "9527"

  //用var修饰的变量都既有getter，又有setter
  var age: Int = 18

  //类私有字段，只能在类的内部使用或者伴生对象中访问
  private var name: String = "唐伯虎"

  //类私有字段，访问权限更加严格的，该字段在当前类中被访问
  //在伴生对象里面也不可以访问
  private[this] var pet = "小强"

}

//伴生对象（这个名字和类名相同，叫伴生对象）
object Person {
  def main(args: Array[String]): Unit = {

    val p = new Person

    //如果是下面的修改，发现下面有红线，说明val类型的不支持重新赋值，但是可以获取到值
    //p.id = "123"
    println(p.id)
    //打印age
    println(p.age)
    //打印name,伴生对象中可以在访问private变量
    println(p.name)
    //由于pet字段用private[this]修饰，伴生对象中访问不到pet变量
    //p.pet(访问不到)

  }
}

``` 

##  2 构造器

Scala中的每个类都有主构造器，主构造器的参数直接放置类名后面，与类交织在一起。
注意：主构造器会执行类定义中的所有语句。

``` scala
class Student(val name: String, var age: Int) {
  //主构造器会执行类定义的所有语句
  println("执行主构造器")
  private var gender = "male"

  def this(name: String, age: Int, gender: String) {
    //每个辅助构造器执行必须以主构造器或者其他辅助构造器的调用开始
    this(name, age)
    println("执行辅助构造器")
    this.gender = gender
  }
}

object Student {
  def main(args: Array[String]): Unit = {

    //没有执行辅助构造器
    val s1 = new Student("zhangsan", 20)

    //辅助构造器被调用
    val s2 = new Student("zhangsan", 20, "female")
  }
}
```
## 3 Scala中的object

__object 相当于 class 的单个实例，通常在里面放一些静态的 field 或者 method；
在Scala中没有静态方法和静态字段，但是可以使用object这个语法结构来达到同样的目的。
object作用：__

1.存放工具方法和常量 <br> 
2.高效共享单个不可变的实例 <br>
3.单例模式 <br>

``` scala
class Session {}

object SessionFactory {
  //相当于java中的静态块
  val session = new Session

  //在object中的方法相当于java中的静态方法

  def getSession(): Session = {
    session
  }
}

object SingletonDemo {
  def main(args: Array[String]): Unit = {

    // 单例对象,不需要new.用 单例名称.方法 调用对象中的方法.
    val session = SessionFactory.getSession()
    println(session)

    // 单例对象.变量
    val session2 = SessionFactory.session;
    println(session2)
  }
}
```

##  4 Scala中的伴生对象

* 如果有一个class文件，还有一个与class同名的object文件，那么就称这个object是class的伴生对象，class是object的伴生类；
* 伴生类和伴生对象必须存放在一个.scala文件中；
* 举例说明：

``` scala

class
Dog {
  val id = 1;
  private var name = "大白"

  def printName() = {
    // 在Dog类中可以访问伴生对象Dog的私有属性
    println(Dog.CONSTANT + name)
  }
}

object Dog {
  //在Dog类中可以访问伴生对象Dog的私有属性
  private val CONSTANT = "汪汪汪 : "

  def main(args: Array[String]): Unit = {
    val dog = new Dog
    dog.printName()

    dog.name = "小白"
    println(dog.name)
  }
}

```
## 5 Scala中的apply方法

* object 中非常重要的一个特殊方法，就是apply方法；

* apply方法通常是在伴生对象中实现的，其目的是，通过伴生类的构造函数功能，来实现伴生对象的构造函数功能；

* 通常我们会在类的伴生对象中定义apply方法，当遇到类名(参数1,...参数n)时apply方法会被调用；

* 	在创建伴生对象或伴生类的对象时，通常不会使用new class/class() 的方式，而是直接使用 class()，隐式的调用伴生对象的 apply 方法，这样会让对象创建的更加简洁；

``` scala

/**
  *  Array 类的伴生对象中，就实现了可接收变长参数的 apply 方法，
  * 并通过创建一个 Array 类的实例化对象，实现了伴生对象的构造函数功能
  */
// 指定 T 泛型的数据类型，并使用变长参数 xs 接收传参，返回 Array[T] 数组
// 通过 new 关键字创建 xs.length 长的 Array 数组
// 其实就是调用Array伴生类的 constructor进行 Array对象的初始化
//  def apply[T: ClassTag](xs: T*): Array[T] = {
//    val array = new Array[T](xs.length)
//    var i = 0
//    for (x <- xs.iterator) { array(i) = x; i += 1 }
//    array
//  }

object ApplyDemo {
  def main(args: Array[String]) {
    //调用了Array伴生对象的apply方法
    //def apply(x: Int, xs: Int*): Array[Int]
    //arr1中只有一个元素5
    val arr1 = Array(5)

    //new了一个长度为5的array，数组里面包含5个null
    var arr2 = new Array(5)
    println(arr1.toBuffer)
  }
}
```
 ## 6 ala中的main方法
 
* 同Java一样，如果要运行一个程序，必须要编写一个包含 main 方法的类；
* 在 Scala 中，也必须要有一个 main 方法，作为入口；
* Scala 中的 main 方法定义为 def main(args: Array[String])，而且必须定义在 object 中；
* 	除了自己实现 main 方法之外，还可以继承 App Trait，然后，将需要写在 main 方法中运行的代码，直接作为 object 的 constructor 代码即可，而且还可以使用 args 接收传入的参数；

``` scala
object Main_Demo {
  def main(args: Array[String]): Unit = {
    if (args.length > 0) {
      println("Hello, " + args(0))
    } else {
      println("Hello World!")
    }
  }
}

//2.使用继承App Trait ,将需要写在 main 方法中运行的代码
// 直接作为 object 的 constructor 代码即可，
// 而且还可以使用 args 接收传入的参数。
object Main_Demo2 extends App {
  if (args.length > 0) {
    println("Hello, " + args(0))
  } else {
    println("Hello World!")
  }
}
```


