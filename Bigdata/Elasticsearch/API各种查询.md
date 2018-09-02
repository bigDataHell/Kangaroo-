
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
* prefix_length ：此参数可以控制模糊查询的行为。有关此参数值的更多信息，
*  max_expansions ：此参数可以控制模糊查询的行为。
* zero_terms_query ：该参数允许指定当所有的词条都被分析器移除时（例如，因为停
止词），查询的行为。它可以被设置为 none 或 all ，默认值是 none 。在分析器移除所有查
询词条时，该参数设置为 none ，将没有文档返回；设置为 all ，则将返回所有文档。
* cutoff_frequency ：该参数允许将查询分解成两组：一组低频词和一组高频词


 __match_phrase 查询可以使用的参数:__
 
* slop ：这是一个整数值，该值定义了文本查询中的词条和词条之间可以有多少个未知词
条，以被视为跟一个短语匹配。此参数的默认值是 0 ，这意味着，不允许有额外的词条
* analyzer ：这个参数定义了分析查询文本时用到的分析器的名字。默认值为 default
analyzer 。


__match_phrase_prefix 查询可以使用的参数:__

* max_expansions 。这个参数控制有多少前缀将被重写成最后的词条

我们没有提供完整的“crime and punishment”短语，而只是提供“crime and punishm”，该查询仍将匹配我们的文档。










  
