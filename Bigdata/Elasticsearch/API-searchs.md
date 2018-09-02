
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
	
#### 4.1 基于ik分词器 重新创建索引

``` java
  @Test
  public void createIndexByIK() {
    // 创建索引
    client.admin().indices().prepareCreate("blog2").get();
    // 删除索引
    // client.admin().indices().prepareDelete("blog","blog2").get();

  }
```
#### 4.2 创建映射

``` java
    @Test
    // 映射操作
    public void createMapping() throws Exception {
        // 创建索引
        // client.admin().indices().prepareCreate("blog02").execute().actionGet();
        // 添加映射
        XContentBuilder builder = XContentFactory.jsonBuilder()
            // startObject num = endObject num
            .startObject()
            .startObject("article")
            .startObject("properties")
            .startObject("id").field("type", "integer").field("store", "yes").endObject()
            .startObject("title").field("type", "string").field("store", "yes").field("analyzer", "ik").endObject()
            .startObject("content").field("type", "string").field("store", "yes").field("analyzer", "ik").endObject()
            .endObject()
            .endObject()
            .endObject();
        // 索引必须已经存在.
        PutMappingRequest mapping = Requests.putMappingRequest("blog2").type("article").source(builder);
        client.admin().indices().putMapping(mapping).get();
    }
```

#### 结果 : 

![elasticsearch01](https://github.com/bigDataHell/Kangaroo-/blob/master/images/elasticsearch01.png)


## 5 IK文档相关操作 

#### 5.2 创建文档:

``` java
@Test
    public void createDocumentByMapping() throws IOException {

        XContentBuilder builder = XContentFactory.jsonBuilder()
            .startObject()
	    // id为int类型
            .field("id", 1)
            .field("title", "ElasticSearch是一个基于Lucene的搜索服务器")
            .field("content",
                "它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。Elasticsearch是用Java开发的，并作为Apache许可条款下的开放源码发布，是当前流行的企业级搜索引擎。设计用于云计算中，能够达到实时搜索，稳定，可靠，快速，安装使用方便。")
            .endObject();


        /** 如果主键唯一标识ID已经存在,则是更新文档 ;如果不存在,则是创建文档 */
        // 创建文档,定义索引名称,文档类型,主键唯一标识ID
            client.prepareIndex("blog2", "article", "2").setSource(builder).get();
    }

```
创建以后则通配符查询能查询出数据

#### 5.2 建立文档数据（Jackson）

问题：如何将Article对象，转换为json数据 ---- Jackson 转换开发包  <br>
Jackson 是一个 Java 用来处理 JSON 格式数据的类库，性能非常好。 <br>
    Jackson可以轻松的将Java对象转换成json对象和xml文档，同样也可以将json、xml转换成Java对象。Jackson库于2012.10.8号发布了最新的2.1版。 <br>
    Jackson源码目前托管于GitHub，地址：https://github.com/FasterXML/ <br>
    Jackson 2.x介绍 <br>
    Jackson 2.x版提供了三个JAR包供下载： <br>
    
* 1. Core库：streaming parser/generator，即流式的解析器和生成器。
下载： <br>
http://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.1.0/jackson-core-2.1.0.jar

* 2. Databind库：ObjectMapper, Json Tree Model，即对象映射器，JSON树模型。
下载： <br>
http://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.1.0/jackson-databind-2.1.0.jar

* 3. Annotations库：databinding annotations，即带注释的数据绑定包。
下载： <br>
http://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.1.0/jackson-annotations-2.1.0.jar

从Jackson 2.0起， <br>
核心组件包括：jackson-annotations、jackson-core、jackson-databind。 <br>
数据格式模块包括：Smile、CSV、XML、YAML。









