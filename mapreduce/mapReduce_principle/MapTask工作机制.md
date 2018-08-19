## 详细步骤：

* 首先，读取数据组件 InputFormat （默认 TextInputFormat）会通过 getSplits
方法对输入目录中文件进行逻辑切片规划得到 splits，有多少个 split 就对
应启动多少个 MapTask。split 与 block 的对应关系默认是一对一。

* 将 输 入 文 件 切 分 为 splits 之 后 ， 由 RecordReader 对 象 （ 默 认
LineRecordReader）进行读取，以\n 作为分隔符，读取一行数据，返回<key，
value>。Key 表示每行首字符偏移值，value 表示这一行文本内容。

* 读取 split 返回<key,value>，进入用户自己继承的 Mapper 类中，执行用户
重写的 map 函数。RecordReader 读取一行这里调用一次。

* map 逻辑完之后，将 map 的每条结果通过 context.write 进行 collect 数据
收集。在 collect 中，会先对其进行分区处理，默认使用 HashPartitioner。
MapReduce 提供 Partitioner 接口，它的作用就是根据 key 或 value 及 reduce 的数量
来决定当前的这对输出数据最终应该交由哪个 reduce task 处理。默认对 key hash 后再以
reduce task 数量取模。默认的取模方式只是为了平均 reduce 的处理能力，如果用户自己
对 Partitioner 有需求，可以订制并设置到 job 上。

* 接下来，会将数据写入内存，内存中这片区域叫做环形缓冲区，缓冲区的作
用是批量收集 map 结果，减少磁盘 IO 的影响。我们的 key/value 对以及
Partition 的结果都会被写入缓冲区。当然写入之前，key 与 value 值都会
被序列化成字节数组。

  * 环形缓冲区其实是一个数组，数组中存放着 key、value 的序列化数据和 key、value 的
元数据信息，包括 partition、key 的起始位置、value 的起始位置以及 value 的长度。环
形结构是一个抽象概念。

  * 缓冲区是有大小限制，默认是 100MB。当 map task 的输出结果很多时，就可能会撑爆
内存，所以需要在一定条件下将缓冲区中的数据临时写入磁盘，然后重新利用这块缓冲区。
这个从内存往磁盘写数据的过程被称为 Spill，中文可译为溢写。这个溢写是由单独线程来
完成，不影响往缓冲区写 map 结果的线程。溢写线程启动时不应该阻止 map 的结果输出，所
以整个缓冲区有个溢写的比例 spill.percent。这个比例默认是 0.8，也就是当缓冲区的数
据已经达到阈值（buffer size * spill percent = 100MB * 0.8 = 80MB），溢写线程启动，
锁定这 80MB 的内存，执行溢写过程。Map task 的输出结果还可以往剩下的 20MB 内存中写，
互不影响。

* 当溢写线程启动后，需要对这 80MB 空间内的 key 做排序(Sort)。排序是
MapReduce 模型默认的行为，这里的排序也是对序列化的字节做的排序。
如果 job 设置过 Combiner，那么现在就是使用 Combiner 的时候了。将
有相同 key 的 key/value 对的 value 加起来，减少溢写到磁盘的数据量。
Combiner 会优化 MapReduce 的中间结果，所以它在整个模型中会多次使用。
那哪些场景才能使用 Combiner 呢？从这里分析，Combiner 的输出是 Reducer 的输入，
Combiner 绝不能改变最终的计算结果。Combiner 只应该用于那种 Reduce 的输入 key/value
与输出 key/value 类型完全一致，且不影响最终结果的场景。比如累加，最大值等。Combiner
的使用一定得慎重，如果用好，它对 job 执行效率有帮助，反之会影响 reduce 的最终结果。

* 每次溢写会在磁盘上生成一个临时文件（写之前判断是否有 combiner），如
果 map 的输出结果真的很大，有多次这样的溢写发生，磁盘上相应的就会有
多个临时文件存在。当整个数据处理结束之后开始对磁盘中的临时文件进行
merge 合并，因为最终的文件只有一个，写入磁盘，并且为这个文件提供了
一个索引文件，以记录每个 reduce 对应数据的偏移量。
