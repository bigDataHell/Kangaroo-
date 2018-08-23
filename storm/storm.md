## 1 Strom是什么?

 * Storm是一个流式计算框架，数据源源不断的产生，源源不断的收集，源源不断的计算。
  （一条数据一条数据的处理）
 * Storm只负责数据的计算，不负责数据的存储。
 * 2013年前后，阿里巴巴基于storm框架，使用java语言开发了类似的流式计算框架佳作，Jstorm。2016年年底阿里巴巴将源码贡献给了Apache storm，两个项目开始合并，新的项目名字叫做storm2.x。阿里巴巴团队专注flink开发。

## 2 Storm架构

* 架构图:

![storm01](https://github.com/bigDataHell/Kangaroo-/blob/master/images/storm01.png)

## 3 Storm编程模型

![storm01](https://github.com/bigDataHell/Kangaroo-/blob/master/images/storm02.png)


**Topology**：Storm中运行的一个实时应用程序，因为各个组件间的消息流动形成逻辑上的一个拓扑结构。

**Spout**：在一个topology中产生源数据流的组件。通常情况下spout会从外部数据源中读取数据，然后转换为topology内部的源数据。Spout是一个主动的角色，其接口中有个nextTuple()函数，storm框架会不停地调用此函数，用户只要在其中生成源数据即可。

**Bolt**：在一个topology中接受数据然后执行处理的组件。Bolt可以执行过滤、函数操作、合并、写数据库等任何操作。Bolt是一个被动的角色，其接口中有个execute(Tuple input)函数,在接受到消息后会调用此函数，用户可以在其中执行自己想要的操作。

**Tuple**：一次消息传递的基本单元。本来应该是一个key-value的map，但是由于各个组件间传递的tuple的字段名称已经事先定义好，所以tuple中只要按序填入各个value就行了，所以就是一个value list.

**Stream**：源源不断传递的tuple就组成了stream。

## 4 grouping策略

`Stream grouping`：即消息的partition方法。

Stream Grouping定义了一个流在Bolt任务间该如何被切分。这里有Storm提供的6个Stream Grouping类型：

1. **随机分组(Shuffle grouping)** ：随机分发tuple到Bolt的任务，保证每个任务获得相等数量的tuple。 跨服务器通信，浪费网络资源，尽量不适用

2. **字段分组(Fields grouping)** ：根据指定字段分割数据流，并分组。例如，根据“user-id”字段，相同“user-id”的元组总是分发到同一个任务，不同“user-id”的元组可能分发到不同的任务。  跨服务器，除非有必要，才使用这种方式。

3. **全部分组(All grouping)** ：tuple被复制到bolt的所有任务。这种类型需要谨慎使用。 人手一份，完全不必要

4. **全局分组(Global grouping)** ：全部流都分配到bolt的同一个任务。明确地说，是分配给ID最小的那个task。 欺负新人

5. **无分组(None grouping)** ：你不需要关心流是如何分组。目前，无分组等效于随机分组。但最终，Storm将把无分组的Bolts放到Bolts或Spouts订阅它们的同一线程去执行(如果可能)。

6. **直接分组(Direct grouping)** ：这是一个特别的分组类型。元组生产者决定tuple由哪个元组处理者任务接收。 点名分配   AckerBolt 消息容错

7. **LocalOrShuffle 分组** :  优先将数据发送到本地的Task，节约网络通信的资源。


## 5 并发度

nimbus : 主节点

supervisor : 从节点

亦歌supervisor可以配置多个worker,每个worker可以运行多个executor.

在executor里运行一个topology的一个component（spout、bolt）叫做task。task  是storm中进行计算的最小的运行单位，表示是spout或者bolt的运行实例。

supervisor(节点)>worker(进程)>executor(线程)>task(实例)

Topology是一个实时应用程序,可以在多个worker上运行.

程序执行的最大粒度的运行单位是进程，刚才说的task也是需要有进程来运行它的，在supervisor中，运行task的进程称为worker，
Supervisor节点上可以运行非常多的worker进程，一般在一个进程中是可以启动多个线程的，所以我们可以在worker中运行多个线程，这些线程称为executor，在executor中运行task。

worker,executor,task解释
 
1个worker进程执行的是1个topology的子集（注：不会出现1个worker为多个topology服务）。1个worker进程会启动1个或多个executor线程来执行1个topology的component(spout或bolt)。因此，1个运行中的topology就是由集群中多台物理机上的多个worker进程组成的。
 
executor是1个被worker进程启动的单独线程。每个executor只会运行1个topology的1个component(spout或bolt)的task（注：task可以是1个或多个，storm默认是1个component只生成1个task，executor线程里会在每次循环里顺序调用所有task实例）。
 
task是最终运行spout或bolt中代码的单元（注：1个task即为spout或bolt的1个实例，executor线程在执行期间会调用该task的nextTuple或execute方法）。topology启动后，1个component(spout或bolt)的task数目是固定不变的，但该component使用的executor线程数可以动态调整（例如：1个executor线程可以执行该component的1个或多个task实例）。这意味着，对于1个component存在这样的条件：#threads<=#tasks（即：线程数小于等于task数目）。默认情况下task的数目等于executor线程数目，即1个executor线程只运行1个task。
 
 

