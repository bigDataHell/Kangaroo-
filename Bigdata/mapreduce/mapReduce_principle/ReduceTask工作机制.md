
## reduceTask

![reduceTask01](https://github.com/bigDataHell/Kangaroo-/blob/master/images/reduceTask01.png)

## 详细步骤：

* Copy 阶段，简单地拉取数据。Reduce 进程启动一些数据 copy 线程(Fetcher)，
通过 HTTP 方式请求 maptask 获取属于自己的文件。

* Merge 阶段。这里的 merge 如 map 端的 merge 动作，只是数组中存放的是不
同 map 端 copy 来的数值。Copy 过来的数据会先放入内存缓冲区中，这里的
缓冲区大小要比 map 端的更为灵活。merge 有三种形式：**内存到内存**；**内存
到磁盘**；**磁盘到磁盘**。默认情况下第一种形式不启用。当内存中的数据量到
达一定阈值，就启动内存到磁盘的 merge。与 map 端类似，这也是溢写的过
程，这个过程中如果你设置有 Combiner，也是会启用的，然后在磁盘中生成
了众多的溢写文件。第二种 merge 方式一直在运行，直到没有 map 端的数据
时才结束，然后启动第三种磁盘到磁盘的 merge 方式生成最终的文件。

* 把分散的数据合并成一个大的数据后，还会再对合并后的数据排序。

* 对排序后的键值对调用 reduce 方法，键相等的键值对调用一次 reduce 方法，
每次调用会产生零个或者多个键值对，最后把这些输出的键值对写入到 HDFS文件中。



## 整个MapReducer阶段

![MapReduce](https://github.com/bigDataHell/Kangaroo-/blob/master/images/MapReduce.png)
