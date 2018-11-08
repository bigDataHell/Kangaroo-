
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
   * TreeMap : 可以自然排序或者自定义排序.
  

  
  
 

 
 

 
