## SQL 是一种声明式语言
SQL 语言是为计算机声明了一个你想从原始数据中获得什么样的结果的一个范例，而不是告诉计算机如何能够得到结果。
SQL 语言声明的是结果集的属性，计算机会根据 SQL 所声明的内容来从数据库中挑选出符合声明的数据，而不是像传统编程思维去指示计算机如何操作。

## SQL 中的语法顺序与执行顺序

* SQL 语句的语法顺序是：
1.	SELECT[DISTINCT]
2.	FROM
3.	WHERE
4.	GROUP BY
5.	HAVING
6.	UNION
7.	ORDER BY
* 执行顺序为：
1.	FROM
2.	WHERE
3.	GROUP BY
4.	HAVING
5.	SELECT
6.	DISTINCT
7.	UNION
8.	ORDER BY

* 关于 SQL 语句的执行顺序，有三个值得我们注意的地方：
    * FROM 才是 SQL 语句执行的第一步，并非 SELECT 。数据库在执行 SQL 语句的第一步是将数据从硬盘加载到数据缓冲区中，以便对这些数据进行操作。
    * SELECT 是在大部分语句执行了之后才执行的，严格的说是在 FROM 和 GROUP BY 之后执行的。理解这一点是非常重要的，这就是你不能在 WHERE 中使用在             SELECT 中设定别名的字段作为判断条件的原因。

* 无论在语法上还是在执行顺序上， UNION 总是排在在 ORDER BY 之前。很多人认为每个 UNION 段都能使用 ORDER BY 排序，但是根据 SQL 语言标准和各个数据库 SQL 的执行差异来看，这并不是真的。尽管某些数据库允许 SQL 语句对子查询（subqueries）或者派生表（derived tables）进行排序，但是这并不说明这个排序在 UNION 操作过后仍保持排序后的顺序。
* ：并非所有的数据库对 SQL 语句使用相同的解析方式。如 MySQL、PostgreSQL和 SQLite 中就不会按照上面第二点中所说的方式执行。


##  limit : 查看特定部分的数据

* 一般是用于select语句中用以从结果集中拿出特定的一部分数据。

* 查找前1100条数据
SELECT * FROM tb_item_cat LIMIT 1100;

* 查找第1101条数据,只显示一条.
`SELECT * FROM tb_item_cat LIMIT 1100,1;`

* 查找第1101 ~1103三条数据
`SELECT * FROM tb_item_cat LIMIT 1100,3;`

