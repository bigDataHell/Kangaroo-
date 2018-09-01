

Elasticsearch支持两种类型的查询：基本查询和复合查询。 <br>
基本查询，如词条查询用于查询实际数据。<br>
第二种查询为复合查询，如布尔查询，可以合并多个查询。<br>
查询数据 主要依赖QueryBuilder对象 ，可以通过QueryBuilders获取各种查询 ：（基于lucene）

## 1 全字段查询和指定字段查询

``` java
  @Test
  public void searchByQueryBuilders() {

    SearchResponse searchResponse =
        client
            .prepareSearch("blog")
            .setTypes("article")
            /** 全字段查询 */
            // .setQuery(QueryBuilders.queryStringQuery("独立"))
            /** 指定字段查询 * 表示任意的字符 ? 表示一个字符   得不到结果? */
            .setQuery(QueryBuilders.termQuery("content", "*单元?"))
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
