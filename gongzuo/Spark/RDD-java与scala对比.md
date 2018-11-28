
# java中 map方法和mapToPari方法的区别

 ## map方法

``` java

        JavaRDD<String> map = reduceRDD.map(new Function<Tuple2<String, Integer>, String>() {

            @Override
            public String call(Tuple2<String, Integer> stringIntegerTuple2) throws Exception {

                String word = stringIntegerTuple2._1;
                Integer count = stringIntegerTuple2._2;

                return word + "\t" + count;

            }
        });
```

## mapToPari方法

``` java
        JavaPairRDD<String, Integer> ones = wordsRDD.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) {
                return new Tuple2<String, Integer>(s, 1);
            }
        });
``` 
* 要熟悉那个方法首先要看这个方法的参数是什么，返回类型是什么，泛型是什么。最后再看源码。

__map方法接受的`参数`是一个元素为单个的数据集，而mapToPari方法接受的是一个包含二元元组的数据集。<br/>
  而map方法的返回值为包含单个元素的数据集，而mapToPari方法返回的是一个二元元组。__


# 1 map算子

map() 接收一个函数，把这个函数用于 RDD 中的每个元素，将函数的返回结果作为结果返回
RDD 中对应元素的值 map是一对一的关系 

__scala中的map方法会可以输出包含不同类型的RDDs数据集__

## scala
``` scala
     // 注意rdd2的类型
     val rdd2: RDD[String] = rdd1.flatMap(_.split(","))
     // map方法返回的是一个包含二元元组的RDD集合
     val rdd3: RDD[(String, Int)] = rdd2.map((_,1))
    
 ----------------------------
   
    // 注意rdd的类型
    val rdd4: RDD[(String, Int)] = rdd3.reduceByKey(_+_)
    //map方法返回的是一个包含字符串的RDD集合
    val rdd5: RDD[String] = rdd3.map(x => {
      x._1 + " : " + x._2
    })
```
## java

*  该方法scala中没有对应的方法。
   
``` java
        JavaPairRDD<String, Integer> ones = wordsRDD.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) {
                return new Tuple2<String, Integer>(s, 1);
            }
        });

``` 
## 2 filete算子

## scala

``` scala
    // 过滤
    val rdd5: RDD[(String, Int)] = rdd4.filter(_._2 < 4)
```

## java 

``` java
         // 单词出现次数小于4的
        JavaPairRDD<String, Integer> filterRDD = reduceRDD.filter(new Function<Tuple2<String, Integer>, Boolean>() {
            @Override
            public Boolean call(Tuple2<String, Integer> stringIntegerTuple2) throws Exception {

                return stringIntegerTuple2._2 < 4;
            }
        });
        
  ---------------------------
          // 单词中包含字符 "h" 的
          JavaPairRDD<String, Integer> filterRDD = reduceRDD.filter(new Function<Tuple2<String, Integer>, Boolean>() {
            @Override
            public Boolean call(Tuple2<String, Integer> stringIntegerTuple2) throws Exception {

               // return stringIntegerTuple2._2 < 4;
                return stringIntegerTuple2._1.contains("h");
            }
        });
``` 

## 3 union 算子

## scala
``` scala
scala> var RDD1 = sc.parallelize(List("aa","aa","bb","cc","dd"))
    scala> var RDD2 = sc.parallelize(List("aa","dd","ff"))

    scala> RDD1.collect
    res6: Array[String] = Array(aa, aa, bb, cc, dd)

    scala> RDD2.collect
    res7: Array[String] = Array(aa, dd, ff)

    scala> RDD1.union(RDD2).collect
    res8: Array[String] = Array(aa, aa, bb, cc, dd, aa, dd, ff)

```

## java

``` java

  JavaRDD<String> RDD1 = sc.parallelize(Arrays.asList("aa", "aa", "bb", "cc", "dd"));
    JavaRDD<String> RDD2 = sc.parallelize(Arrays.asList("aa","dd","ff"));
    JavaRDD<String> unionRDD = RDD1.union(RDD2);
    List<String> collect = unionRDD.collect();
    for (String str:collect) {
        System.out.print(str+", ");
    }
-----------输出---------
aa, aa, bb, cc, dd, aa, dd, ff

``` 

## 4 groupByKey 算子

groupByKey会将RDD[key,value] 按照相同的key进行分组，形成RDD[key,Iterable[value]]的形式， 有点类似于sql中的groupby，例如类似于mysql中的group_concat 

## scala
``` scala
  val scoreDetail = sc.parallelize(List(("xiaoming",75),("xiaoming",90),("lihua",95),("lihua",100),("xiaofeng",85)))
    scoreDetail.groupByKey().collect().foreach(println(_)
```
## java
``` java
JavaPairRDD<String, Iterable<Integer>> stringIterableJavaPairRDD = filterRDD.groupByKey();
``` 
## 5 

