
## 1 查询所有 :  matchAllQuery()匹配所有文件

``` java
@Test
  public void searchAll() {

    SearchResponse searchResponse =
        client
            .prepareSearch("blog2")
            .setTypes("article")
            .setQuery(QueryBuilders.matchAllQuery())
            .get();

    SearchHits hits = searchResponse.getHits();
    this.searchValue(hits);

  }

  // 把显示结果德代码进行了封装
  private void searchValue(SearchHits hits) {

      System.out.println("\n" + "查询的结果数量:" + hits.getTotalHits() + "\n");

      Iterator<SearchHit> iterator = hits.iterator();
    // 遍历每条数据
    while (iterator.hasNext()) {

      SearchHit searchHit = iterator.next();

      System.out.println("JSON格式的数据:" + searchHit.getSourceAsString());

      System.out.println("id:" + searchHit.getSource().get("id"));
      System.out.println("title:" + searchHit.getSource().get("title"));
      System.out.println("content:" + searchHit.getSource().get("content"));
      System.out.println("------------------------------------------------");
      // 下边是干啥的?
      for (Iterator<SearchHitField> ite = searchHit.iterator(); ite.hasNext(); ) {
        SearchHitField next = ite.next();
        System.out.println(next.getValues());
      }
    }
  }
  ```
  ## 2 解析查询字符串 : queryStringQuery()
  
  相比其他可用的查询，query_string查询支持全部的Apache Lucene查询语法
  针对多字段的query_string查询
  
``` java
SearchResponse searchResponse =
        client
            .prepareSearch("blog2")
            .setTypes("article")
            // 查询特定词条 查询对象 : 所有文档
            //.setQuery(QueryBuilders.queryStringQuery("轻松"))
            // 指定字段,可以指定多个.
            //.setQuery(QueryBuilders.queryStringQuery("文档").field("title").field("content"))
            .setQuery(QueryBuilders.queryStringQuery("sp?").field("title"))
            .get();

    SearchHits hits = searchResponse.getHits();
    this.searchValue(hits);
  ```

## 3 通配符查询 : wildcardQuery

*匹配多个字符，?匹配1个字符
注意：避免\* 开始, 会检索大量内容造成效率缓慢

``` java
 .setQuery(QueryBuilders.wildcardQuery("title","sk*"))
```

## 4 词条查询（termQuery）

词条查询是Elasticsearch中的一个简单查询。它仅匹配在给定字段中含有该词条的文档，而
且是确切的、未经分析的词条

termQuery("key", obj) 完全匹配 <br>
termsQuery("key", obj1, obj2..)   一次匹配多个值，只要有一个值是正确的，就可以查询出数据 <br>
__是 "或" 而不是 "与"__

``` java
 // 单词条
 // .setQuery(QueryBuilders.termQuery("title","api"))
 // 多词条
.setQuery(QueryBuilders.termsQuery("title", "api", "sku"))
```

##  4 字段匹配查询 match查询

matchQuery("key", Obj) 单个匹配, field不支持通配符, 前缀具高级特性

match查询把query参数中的值拿出来，加以分析，然后构建相应的查询。使用match查询
时，Elasticsearch将对一个字段选择合适的分析器，所以可以确定，传给match查询的词条将被建立索引时相同的分析器处理。

multiMatchQuery("text", "field1", "field2"..);  匹配多个字段, field有通配符忒行

multi_match 查询和 match 查询一样，不同的是它不是针对单个字段，而是可以通过
fields 参数针对多个字段查询。当然， match 查询中可以使用的所有参数同样可以在 multi_
match 查询中使用。所以，如果想修改 match 查询，让它针对 title 和 otitle 字段运行.

除了之前提到的参数， multi_match 查询还可以使用以下额外的参数来控制它的行为。
 * use_dis_max ：该参数定义一个布尔值，设置为 true 时，使用析取最大分查询，设置为
false 时，使用布尔查询。默认值为 true 。3.3.18节将讨论更多细节。
* tie_breaker ：只有在 use_dis_max 参数设为 true 时才会使用这个参数。它指定低分数
项和最高分数项之间的平衡。3.3.18节将介绍更多细节。


__matchQuery() 可以使用的参数:__

* `operator` ：此参数可以接受 or 和 and ，控制用来连接创建的布尔条件的布尔运算符。默
认值是 or 。如果希望查询中的所有条件都匹配，可以使用 and 运算符。
* `analyzer` ：这个参数定义了分析查询文本时用到的分析器的名字。默认值为 default
analyzer 。
* `fuzziness` ：可以通过提供此参数的值来构建模糊查询（fuzzy query）。它为字符串类型
提供从0.0到1.0的值。构造模糊查询时，该参数将用来设置相似性。
* `slop` ：这是一个整数值，该值定义了文本查询中的词条和词条之间可以有多少个未知词
条，以被视为跟一个短语匹配。此参数的默认值是 0 ，这意味着，不允许有额外的词条
`slop为1时，“a b”和“a and b”被视为匹配`
* `prefix_length` ：此参数可以控制模糊查询的行为。有关此参数值的更多信息，
* ` max_expansions` ：此参数可以控制模糊查询的行为。
* `zero_terms_query` ：该参数允许指定当所有的词条都被分析器移除时（例如，因为停
止词），查询的行为。它可以被设置为 none 或 all ，默认值是 none 。在分析器移除所有查
询词条时，该参数设置为 none ，将没有文档返回；设置为 all ，则将返回所有文档。
* `cutoff_frequency` ：该参数允许将查询分解成两组：一组低频词和一组高频词


 __match_phrase 查询可以使用的参数:__
 
* `slop` ：这是一个整数值，该值定义了文本查询中的词条和词条之间可以有多少个未知词
条，以被视为跟一个短语匹配。此参数的默认值是 0 ，这意味着，不允许有额外的词条
* analyzer ：这个参数定义了分析查询文本时用到的分析器的名字。默认值为 default
analyzer 。


__match_phrase_prefix 查询可以使用的参数:__

* `match_phrase` 查询公开的参数
* `max_expansions` 。这个参数控制有多少前缀将被重写成最后的词条,它允许查询文本的最后一个词条只做前缀匹配

我们没有提供完整的“crime and punishment”短语，而只是提供“crime and punishm”，该查询仍将匹配我们的文档。

``` java
             // .setQuery(QueryBuilders.matchQuery("title","文档
            // 使用").operator(Operator.AND).analyzer("ik"))
            // 相似度越低,查询数据匹配读越高
            // .setQuery(QueryBuilders.matchQuery("content","对转换").slop(1))
            .setQuery(QueryBuilders.matchPhrasePrefixQuery("content", "js").maxExpansions(20))
```

## 5 只查询ID（标识符查询）

``` java
//根据文档的id查询,可以查询多个
.setQuery(QueryBuilders.idsQuery().ids("1").ids("4"))
```

## 6 相似度查询

fuzzy查询是模糊查询中的第三种类型，它基于编辑距离算法来匹配文档

``` java 
.setQuery(QueryBuilders.fuzzyQuery("content","elasticsear"))
```
## 7 范围查询

    范围查询使我们能够找到在某一字段值在某个范围里的文档，字段可以是数值型，也可以是基于字符串的
    
*  gte ：范围查询将匹配字段值大于或等于此参数值的文档。
*  gt ：范围查询将匹配字段值大于此参数值的文档。
*  lte ：范围查询将匹配字段值小于或等于此参数值的文档。
*  lt ：范围查询将匹配字段值小于此参数值的文档。
    
includeLower(true)：包含上界 <br>
IncludeUpper(true)：包含下界

 ``` java
// 查询id 为 2 3 4 的文档
//.setQuery(QueryBuilders.rangeQuery("id").gte(2).lte(4))

.setQuery(QueryBuilders.rangeQuery("content").from("鼠标").to("空格").includeLower(true).includeUpper(true))
 ```
## 8 跨度查询

下面代码表示，从该字段content的首字母开始，往后查找300个词,如果包含"稳定"这个词,则能查询出数据    

``` java
 .setQuery(QueryBuilders.spanFirstQuery(QueryBuilders.spanTermQuery("content","稳定"),30))
```

## 9 组合查询（复杂查询）

* must(QueryBuilders) : AND
* mustNot(QueryBuilders): NOT
* should(QueryBuilders):OR 

``` java
 SearchResponse searchResponse =
        client
            .prepareSearch("blog2")
            .setTypes("article")
            .setQuery(
                QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery("title", "文档"))
                    // id 为字段
                    .must(QueryBuilders.rangeQuery("id").from("1").to("3")))
            .get();

    SearchHits hits = searchResponse.getHits();
    this.searchValue(hits);
  ``` 

## 10 排序查询

``` java
        client
            .prepareSearch("blog2")
            .setTypes("article")
            .setQuery(
                QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery("title", "文档"))
                    // id 为字段
                    .must(QueryBuilders.rangeQuery("id").from("1").to("4")))
            // 根据哪个字段排序  如果排序
            .addSort("id", SortOrder.DESC)
            .get();

```

## 11 查询文档分页操作

 查询所有的方法
	searchRequestBuilder 的 setFrom【从0开始】 和 setSize【查询多少条记录】方法实现
  
elasticsearch如果不指定分页,默认是一页10条.

``` java
SearchRequestBuilder searchRequestBuilder =
        client
            .prepareSearch("blog2")
            .setTypes("article")
            .setQuery(QueryBuilders.matchAllQuery())
            .addSort("id", SortOrder.ASC);

    // 从0开始检索,到20
    searchRequestBuilder.setFrom(20).setSize(20);

    SearchResponse searchResponse = searchRequestBuilder.get();

    SearchHits hits = searchResponse.getHits();
    this.searchValue(hits);
```
## 12 高亮显示



  
