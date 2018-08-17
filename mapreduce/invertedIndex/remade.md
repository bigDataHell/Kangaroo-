# mapreduce倒排索引的实现

## 什么是倒排索引?

   倒排索引是文档检索系统中最常用的数据结构，被广泛地应用于全文搜索引擎。 它主
要是用来存储某个单词（或词组） 在一个文档或一组文档中的存储位置的映射，即提供了
一种根据内容来查找文档的方式。由于不是根据文档来确定文档所包含的内容，而是进行相
反的操作，因而称为倒排索引（ `Inverted Index`）。
## 实例描述

   通常情况下，倒排索引由一个单词（或词组）以及相关的文档列表组成，文档列表中的
文档或者是标识文档的 ID 号，或者是指文档所在位置的 URL。如下图所示：

![图片01](https://github.com/bigDataHell/Kangaroo-/blob/master/images/invertedIndex01.png)


   从上图可以看出，单词 1 出现在{文档 1，文档 3，文档 12， ……}中，单词 2 出现在
{文档 4，文档 1，文档 12， ……}中，而单词 3 出现在{文档 6，文档 4，文档 3， ……}
中。在实际应用中，还需要给每个文档添加一个权值，用来指出每个文档与搜索内容的相关
度，如下图所示：

![图片02](https://github.com/bigDataHell/Kangaroo-/blob/master/images/invertedIndex02.png)

   最常用的是使用词频作为权重，即记录单词在文档中出现的次数。以英文为例，如下图
所示，索引文件中的“ MapReduce”一行表示：“ MapReduce”这个单词在文本 T0 中 出
现过 1 次，T1 中出现过 1 次，T2 中出现过 2 次。当搜索条件为“ MapReduce”、“ is”、
“ Simple” 时，对应的集合为： {T0， T1， T2}∩{T0， T1}∩{T0， T1}={T0， T1}，
即文档 T0 和 T1 包 含了所要索引的单词，而且只有 T0 是连续的。

![图片03](https://github.com/bigDataHell/Kangaroo-/blob/master/images/invertedIndex03.png)

## 设计思路

 我们最终要得到的文档内容的形式为: 单词  文件1:次数;文件2:次数;文件3:次数.....


![图片04](https://github.com/bigDataHell/Kangaroo-/blob/master/images/invertedIndex04.png)

* *具体实现请看代码*

    [代码](https://github.com/bigDataHell/Kangaroo-/blob/master/mapreduce/invertedIndex/InvertedIndex.java)
