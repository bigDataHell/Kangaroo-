
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

##  4 字段匹配查询

matchQuery("key", Obj) 单个匹配, field不支持通配符, 前缀具高级特性
     match查询把query参数中的值拿出来，加以分析，然后构建相应的查询。使用match查询
时，Elasticsearch将对一个字段选择合适的分析器，所以可以确定，传给match查询的词条将被建立索引时相同的分析器处理。
multiMatchQuery("text", "field1", "field2"..);  匹配多个字段, field有通配符忒行












  
