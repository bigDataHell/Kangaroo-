
## 查询概述 : 

Elasticsearch支持两种类型的查询：基本查询和复合查询。 <br>
基本查询，如词条查询用于查询实际数据。<br>
第二种查询为复合查询，如布尔查询，可以合并多个查询。<br>
查询数据 主要依赖QueryBuilder对象 ，可以通过QueryBuilders获取各种查询 ：（基于lucene）

* boolQuery() 布尔查询，可以用来组合多个查询条件 
* fuzzyQuery() 相似度查询 
* matchAllQuery() 查询所有数据 
* regexpQuery() 正则表达式查询 
* termQuery() 词条查询 
* wildcardQuery() 模糊查询 


## 1  termQuery() 和 queryStringQuery()词条查询 

__全字段查询和指定字段查询__


``` java
  @Test
  public void searchByQueryBuilders() {

    SearchResponse searchResponse =
        client
            .prepareSearch("blog")
            .setTypes("article")
            /** 全字段查询 */
            // .setQuery(QueryBuilders.queryStringQuery("独立"))
            //指定字段查询
            .setQuery(QueryBuilders.termQuery("id", "1"))
            .get();
    SearchHits hits = searchResponse.getHits();
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
## 2 fuzzyQuery() 相似度查询 

fuzzy 查询是模糊查询中的第三种类型，它基于编辑距离算法来匹配文档。编辑距离的计算
基于我们提供的查询词条和被搜索文档。此查询很占用CPU资源，但当需要模糊匹配时它很有用，
例如，当用户拼写错误时.就能派上用场.

``` java

 SearchResponse searchResponse = client
            .prepareSearch("blog")
            .setTypes("article")
            .setQuery(QueryBuilders.fuzzyQuery("content", "whats"))
            .get();

```
## 3 wildcardQuery() 模糊查询 

 指定字段
 
``` java
SearchResponse searchResponse =
        client
            .prepareSearch("blog")
            .setTypes("article")
            /** 指定字段查询 * 表示任意的字符 ? 表示一个字符   得不到结果? */
            .setQuery(QueryBuilders.wildcardQuery()("content", "*单元?"))
            .get();
```

## 4 分词器

* 分词器			

		在文档存储的时候，将文档的内容进行分词，并放置到索引中				 <br>									
		在文档搜索的时候，将查询的条件，到索引中查找是否有对应的词，如果有就再到数据区域查找结果，如果没有就查询不到													

例如：“ElasticSearch是一个基于Lucene的搜索服务器” 

	分词（好的）： ElasticSearch、是、一个、基于、Lucene、搜索、服务、服务器  <br>
	默认单字分词（差的）： ElasticSearch、 是、一、个、基、于、搜、索










