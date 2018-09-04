
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



