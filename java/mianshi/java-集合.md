
# java 集合面试题

![集合框架体系统](https://github.com/bigDataHell/Kangaroo-/blob/master/images/%E9%9B%86%E5%90%88%E6%A1%86%E6%9E%B6%E4%BD%93%E7%B3%BB%E5%9B%BE.png)

## 1 集合和数组的区别

  * 1.1  存储
  
      数组可以存储基本数据类型和对象.<br>
      集合只用于存储对象.
  * 1.2  长度
  
    数组长度是固定的.<br>
    集合长度是可变的.
    
## 2 Iterator 接口

 所有的集合类，都实现了Iterator接口，这是一个用于遍历集合中元素的接口，主要包含以下三种方法：
 
     1.hasNext()是否还有下一个元素。
     2.next()返回下一个元素。
     3.remove()删除当前元素。
     
     
 ## 3 几种重要的接口和类简介
 
 ### 3.1 List集合 : 有索引, 有序, 可重复 .
     
  * ArrayList : 动态数组(可增长数组)
    
    __适合随机访问,访问快,增删慢__
    
  * LinkedList : 链表(双向链表)
    
    __适合新增和删除操作,查询满,比如用作数据库连接池__
 
 ### 3.2 set集合 : 无索引,无序,不可重复.
 
 
  * HashSet : 无序,不重复,底层为 Hash表实现.
    * LinkedHashSet : 保持集合的插入循序.
  * TreeSet : 可以对元素进行排序,底层为二叉排序树的插入算法
  
  ### 3.3 Map 接口
  
   * HashMap : 键值对, 键不可重复,值可以重复. 无序.
     * LinkedHashMap : 有序.
   * TreeMap : 可以自然排序或者自定义排序. 红黑树实现.
  
## 4 可排序集合的排序方法

要想改变TreeMap的默认比较次序，我们可以在其构造函数中传入一个自己的比较器。TreeMap的比较器构造函数如下：

注意 : 
  * 返回0,则默认直插入一个值.
  * 返回1,按正序排序(ASC)
  * 返回0,按倒序排序(DESC)
``` java
  public TreeMap(Comparator<? super K> comparator)
``` 

``` java
/*排序,想到treeSet,比较简单,按姓名排序,按年龄为自然排序*/
        Map<Student,String> classTreeMap = new TreeMap<Student,String>(new Comparator<Student>() {

            @Override
            public int compare(Student o1, Student o2) {
                int temp = o1.getName().compareTo(o2.getName());
                return temp==0?o1.compareTo(o2):temp;
            }
        });
```

## 5 集合的遍历


### 5.1 Map集合遍历

``` java
        HashMap<String,String> map = new HashMap<>();

        map.put("1","2");
        map.put("3","2");
        map.put("4","4");
        map.put("5","3");
        map.put("6","2");

        for (String key : map.keySet()) {
            String value = map.get(key);
            System.out.println(value);
        }
        
```
### 5.2List集合遍历

``` java 
        // 集合演示
        List<String> list = new ArrayList<>();

        list.add("13");
        list.add("35352");
        list.add("25252");
        // 插入,把元素插入到第几个位置,其余后排
        list.add(2,"3424");
        
        Iterator<String> iterator = list.iterator();

        while(iterator.hasNext()){
            String next = iterator.next();
            System.out.println(next);
        }
```


  
  
 

 
 

 
