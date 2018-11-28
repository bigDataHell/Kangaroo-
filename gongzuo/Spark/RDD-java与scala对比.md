
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
``` java
        JavaPairRDD<String, Integer> ones = wordsRDD.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) {
                return new Tuple2<String, Integer>(s, 1);
            }
        });

``` 


