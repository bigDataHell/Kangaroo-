## 1 了解搜索技术

### 1.1 什么是搜索?

  简单的说，搜索就是搜寻、查找，在IT行业中就是指用户输入关键字，通过相应的算法，查询并返回用户所需要的信息。
  
  线性匹配：<br>
  	Select * from 表名 where id= <br>
  	select * from item where title like ’%小米%’ <br>
  结果:   <br>
  结果中包含： 所有title字段中有 小米 这个词的结果集 <br>
  是否准确？ 是否高效？
  
  ### 1.2 普通的数据库搜索的缺陷  

	1、因为没有通过高效的索引方式，所以查询的速度在大量数据的情况下是很慢。
 
	2、搜索效果比较差，只能对用户输入的完整关键字首尾位进行模糊匹配。
  
	3、用户搜索时如果多输入一个字符，可能就导致查询出的结果远离用户的预期。
  
  问题：  
 	1.效率问题  不够高效    <br>
  	2.准确度的问题 不够精准
  
  ### 1.3 搜索引擎
  
  **搜索引擎**（Search Engine）是指根据一定的策略、运用特定的计算机程序从互联网上搜集信息，在对信息进行组织和处理后，为用户提供检索服务，
   将用户检索相关的信息展示给用户的系统。搜索引擎包括全文索引、目录索引、元搜索引擎、垂直搜索引擎、集合式搜索引擎、门户搜索引擎与免费链接列表等。

  ### 1.4、搜索引擎的种类
  
* 搜索引擎按照功能通常分为垂直搜索和综合搜索。 <br>

	1、垂直搜索是指专门针对某一类信息进行搜索。例如：会搜网 主要做商务搜索的，并且提供商务信息。除此之外还有爱看图标网、职友集等。 <br>
	
	2、综合搜索是指对众多信息进行综合性的搜索。例如：百度、谷歌、搜狗、360搜索等。 <br>
	
	3、站内搜索是指对网站内的信息进行的搜索。例如：京东、招聘网站等 <br>
	
	4、软件内部搜索，例如word、eclipse等 <br>

  全文检索：将非结构化的数据 转换成  结构化的数据， 在结构化数据的基础之上进行一系列处理， 建立索引，然后检索在索引库上检索。

* 数据的分类： <br>
	结构化数据：格式和大小是固定的  <br>
	非结构化数据：格式和大小不是固定的
	
 ### 1.5 搜索引擎的原理
 
  ![lucene01](https://github.com/bigDataHell/Kangaroo-/blob/master/images/lucene01.png)
  
  爬虫—》抓取网页—》临时库—》处理放到索引区—》提供搜索服务商品表—》处理放到索引区—》提供搜索服务
  
 ### 1.6 倒排索引技术
 
**倒排索引**又叫反向索引（右下图）以字或词为关键字进行索引，表中关键字所对应的记录表项，记录了出现这个字或词的所有文档，每一个表项记录该文档的ID和关键字在该文档中出现的位置情况。

  ![lucene02](https://github.com/bigDataHell/Kangaroo-/blob/master/images/lucene02.png)
  


  在实际的运用中，我们可以对数据库中原始的数据结构（临时表或者商品表），在业务空闲时事先根据左图内容，创建新的文档列表（左图）及倒排索引区域（右图）。
用户有查询需求时，先访问倒排索引数据区域（右图），得出文档编号后，通过文档文档编号即可快速，准确的通过左图找到具体的文档内容。

这一过程，可以通过我们自己写程序来实现，也可以借用已经抽象出来的通用开源技术来实现。

## 2 Lucene概述


### 2.1 什么是Lucene

   ![lucene03](https://github.com/bigDataHell/Kangaroo-/blob/master/images/lucene03.png)

* Lucene是一套用于全文检索和搜寻的开源程序库，由Apache软件基金会支持和提供 <br>
* Lucene提供了一个简单却强大的应用程序接口（API），能够做全文索引和搜寻，在Java开发环境里Lucene是一个成熟的免费开放源代码工具 <br>
* Lucene并不是现成的搜索引擎产品，但可以用来制作搜索引擎产品 <br>
* 官网：http://lucene.apache.org/

## 2.2 什么是全文检索？

计算机索引程序通过扫描文章中的每一个词，对每一个词建立一个索引，指明该词在文章中出现的次数和位置，当用户查询时，检索程序就根据事先建立的索引进行查找，并将查找的结果反馈给用户的检索方式 <br>

什么是全文检索？ ==>  全部都搜索  ==> 如何实现全部都搜索？ ==> 分词 单词永不重复 单词就是索引 单词是最小的搜索单位  
倒排索引 <br>

总结：对文档（数据）中每一个词都做索引。

## 2.3 Lucene下载及版本问题

目前最新的版本是6.x系列，但是大多数企业中依旧使用4.x版本，比较稳定。我们使用4.10.2版本

## 2.4 Lucene与Solr的关系

Lucene：底层的API，工具包 <br>
Solr：基于Lucene开发的企业级的搜索引擎产品


## 3 Lucene的基本使用
 
### 3.1 引入依赖

``` xml
dependencies>
        <!--核心库-->
        <dependency>
            <groupId>org.apache.lucene</groupId>
            <artifactId>lucene-core</artifactId>
            <version>5.2.1</version>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>
        <!--分词器-->
        <dependency>
            <groupId>org.apache.lucene</groupId>
            <artifactId>lucene-analyzers-common</artifactId>
            <version>5.2.1</version>
        </dependency>
        <!--查询解析器-->
        <dependency>
            <groupId>org.apache.lucene</groupId>
            <artifactId>lucene-queries</artifactId>
            <version>5.2.1</version>
        </dependency>
        <!--高亮显示-->
        <dependency>
            <groupId>org.apache.lucene</groupId>
            <artifactId>lucene-highlighter</artifactId>
            <version>5.2.1</version>
        </dependency>
        <!--ik-analyzer 中文分词器-->
        <dependency>
            <groupId>cn.bestwu</groupId>
            <artifactId>ik-analyzers</artifactId>
            <version>5.1.0</version>
        </dependency>
    </dependencies>
```





















