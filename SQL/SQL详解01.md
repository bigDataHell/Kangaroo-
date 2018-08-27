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


---------------------------

##  WHERE 

WHERE 子句用于规定选择的标准。

* 语法: <br>
         SELECT 列名称 FROM 表名称 WHERE 列 运算符 值
         
  |操作符|描述|
  |:---:|:--:|
  |=|等于|
  |<>|不等于|
  |||
   |>|大于|
|<|小于|
|>=|大于等于|
|<=|小于等于|
|BETWEEN|在某个范围内|
|LIKE|搜索某种模式|

         在某些版本的 SQL 中，操作符 <> 可以写为 !=。

* 实例 BETWEEN 

找出字段  `COLUMN_Name` 开头首字母在 a ~ g(包含) 之间并且字段`INTEGER_IDX` > 2 的数据

`SELECT * FROM COLUMNS_V2 WHERE COLUMN_Name BETWEEN 'a' AND 'g' AND INTEGER_IDX > 2;`

结果:

![sql01](https://github.com/bigDataHell/Kangaroo-/blob/master/images/sql01.png)

* 实例  LIKE
      * 通配符
      
         % :  替代一个或多个字符
         _ : 仅替代一个字符
         [charlist] : 字符列中的任何单一字符
         [^charlist] 或者 [!charlist] : 不在字符列中的任何单一字符


 MySQL 不支持 [^charlist] 或 [!charlist] 通配符
 
包含`a`的 COLUMN_NAME <br>
`SELECT * FROM COLUMNS_V2 WHERE COLUMN_NAME LIKE  '%a%';`

以 `a` 开头的 COLUMN_NAME <br>
`SELECT * FROM COLUMNS_V2 WHERE COLUMN_NAME LIKE  'a%';`

以 `a` 结尾的COLUMN_NAME <br>
`SELECT * FROM COLUMNS_V2 WHERE COLUMN_NAME LIKE  '%a';`

查找COLUMN_NAME包含ag并且ag后追加任意字符 <br>
`SELECT * FROM COLUMNS_V2 WHERE COLUMN_NAME LIKE  'ag_';`

















----------------------------

##  limit : 查看特定部分的数据

 一般是用于select语句中用以从结果集中拿出特定的一部分数据。

* 查找前1100条数据

`SELECT * FROM tb_item_cat LIMIT 1100;`

* 查找第1101条数据,只显示一条.

`SELECT * FROM tb_item_cat LIMIT 1100,1;`

* 查找第1101 ~1103三条数据

`SELECT * FROM tb_item_cat LIMIT 1100,3;`

## distinct : 去重,统计

#### 作用 1  去重

在表中，可能会包含重复值。这并不成问题，不过，有时您也许希望仅仅列出不同（distinct）的值。
关键词 DISTINCT 用于返回唯一不同的值。

distinct会同时作用于`month`,`day`这两个字段.

`select distinct month,day from t_dim_time;`

显示该字段不重复的数据.

#### 作用 2 统计

该字段不相同的天数的个数:

`select count(distinct day) from t_dim_time;`



