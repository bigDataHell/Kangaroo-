## 1 连接Java操作客户端

 Elasticsearch 的 Java 客户端非常强大；它可以建立一个嵌入式实例并在必要时运行管理任务。
 
运行一个 Java 应用程序和 Elasticsearch 时，有两种操作模式可供使用。该应用程序可在 Elasticsearch 集群中扮演更加主动或更加被动的角色。
在更加主动的情况下（称为 Node Client），应用程序实例将从集群接收请求，确定哪个节点应处理该请求，就像正常节点所做的一样。
应用程序甚至可以托管索引和处理请求。）另一种模式称为 Transport Client，它将所有请求都转发到另一个 Elasticsearch 节点，由后者来确定最终目标。

当直接在ElasticSearch 建立文档对象时，如果索引不存在的，默认会自动创建，映射采用默认方式 

		ElasticSearch 服务默认端口 9300  <br>
		Web 管理平台端口 9200 

使用org.elasticsearch.client.Client连接服务器。所以任何操作都需要用上，我们把它的创建放置到@Before中，操作最后别忘记执行client.close()方法关闭

**code :**

``` java
  private Client client;

    /** 获取client */
    @Before
    public void getClient() throws Exception {
        // 默认为服务端口 : 9300
        // 如果时集群就用 nodeClient
        client = TransportClient.builder().build()
            .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("hadoop-node-1"), 9300));
    }

    @After
    public void closeClient(){
        client.close();
    }

```
## 2、	建立文档 

__自动创建索引映射__

#### 方式一: 使用json创建

``` java
/** 创建文档,自动创建索引,自动创建映射 */
  @Test
  public void createDocument() {
    // json格式的字符串
    String json =
        "{"
            + "\"id\":\"1\","
            + "\"title\":\"基于Lucene的搜索服务器\","
            + "\"content\":\"它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口\""
            + "}";
    // 创建文档,定义索引名称,文档类型,主键唯一标识ID
    IndexResponse response = client.prepareIndex("blog", "article", "1").setSource(json).get();
    // 获取响应信息
    System.out.println("索引名称 : "+response.getIndex());
    System.out.println("文档类型 : "+response.getType());
    System.out.println("Id : "+response.getId());
    System.out.println("版本 : "+response.getVersion());
    System.out.println("是否创建成功 : "+response.isCreated());
  }

```

#### 方式二 : 使用map创建

``` java
 /** Map : 创建文档,自动创建索引,自动创建映射 */
  @Test
  public void createDocumentByMap() {

    Map<String, Object> source = new HashMap<>();

    source.put("id", "5");
    source.put("title", "Lucene的搜素服务器");
    source.put("content", "它提供了一个分布式多用户能力的全文搜索引擎");

    /** 如果主键唯一标识ID已经存在,则是更新文档 ;如果不存在,则是创建文档 */
    // 创建文档,定义索引名称,文档类型,主键唯一标识ID
    IndexResponse response = client.prepareIndex("blog", "article", "1").setSource(source).get();
    // 获取响应信息
    System.out.println("索引名称 : " + response.getIndex());
    System.out.println("文档类型 : " + response.getType());
    System.out.println("Id : " + response.getId());
    System.out.println("版本 : " + response.getVersion());
    System.out.println("是否创建成功 : " + response.isCreated());
  }
```

#### 方式三 : 使用es的帮助类，创建json对象

``` java 
@Test
  public void createDocument03() throws IOException {

    XContentBuilder xContentBuilder =
        XContentFactory.jsonBuilder()
            .startObject()
            .field("id", "1")
            .field("title", "测试单元")
            .field("content", "测试单元中的每个方法必须可以独立测试，方法间不能有任何依赖")
            .endObject();

    /** 如果主键唯一标识ID已经存在,则是更新文档 ;如果不存在,则是创建文档 */
    // 创建文档,定义索引名称,文档类型,主键唯一标识ID
    IndexResponse response = client.prepareIndex("blog", "article", "4").setSource(xContentBuilder).get();
    // 获取响应信息
    System.out.println("索引名称 : " + response.getIndex());
    System.out.println("文档类型 : " + response.getType());
    System.out.println("Id : " + response.getId());
    System.out.println("版本 : " + response.getVersion());
    System.out.println("是否创建成功 : " + response.isCreated());
  }

```

## 3 搜索文档数据

#### 单个索引

``` java
  /** 单个索引搜素 */
  @Test
  public void getDocuemntData_1() {
    GetResponse getFields = client.prepareGet("blog", "article", "4").get();
    System.out.println(getFields.getSourceAsString());
  }

  
```

#### 多个索引

``` java
/** 多个索引搜素 */
  @Test
  public void getDocuemntData_2() {
    MultiGetResponse multiGetItemResponses =
        client.prepareMultiGet().add("blog", "article", "1")
            .add("blog", "article", "2", "3")
            .get();
    for (MultiGetItemResponse multiGetItemRespons : multiGetItemResponses) {
        GetResponse response = multiGetItemRespons.getResponse();
        if(response.isExists()){
        System.out.println(response.getSourceAsString());
        }
    }
  }

``` 

## 更新文档数据

### 方式一

``` java
  /** 更新文档数据 */
  @Test
  public void updateDoucment_02() throws Exception {

    client
        .update(
            new UpdateRequest("blog", "article", "2")
                .doc(
                    XContentFactory.jsonBuilder()
                        .startObject()
                        .field("id", "1")
                        .field("title", "更新222测试单元")
                        .field("content", "更新222测试单元中的每个方法必须可以独立测试，方法间不能有任何依赖")
                        .endObject()))
        .get();
  }
```

#### 方式二
``` java
  /** 更新文档数据 */
  @Test
  public void updateDoucment_01() throws Exception {
    UpdateRequest updateRequest = new UpdateRequest();
    updateRequest.index("blog"); // 索引名称
    updateRequest.type("article"); // 文档类型
    updateRequest.id("1"); // 更新的id
    updateRequest.doc(
        XContentFactory.jsonBuilder()
            .startObject()
            .field("id", "1")
            .field("title", "更新测试单元")
            .field("content", "更新测试单元中的每个方法必须可以独立测试，方法间不能有任何依赖")
            .endObject());
    client.update(updateRequest).get();
  }
  
```

#### 方式三
``` java
@Test
  public void updateDoucment_03_upsert() throws Exception {

    // 针对同一个索引,查找到了则更新UpdateRequest中的文档数据,
    // 查找不到则添加IndexRequest中的文档数据,一个索引id只会对应一个资源
    // 通过查询,来判断是更新,还是新增.

    // 设置一个查询条件,使用id查询,如果查找不到数据,就会添加IndexRequest中的文档数据
    IndexRequest indexRequest =
        new IndexRequest("blog", "article", "5")
            .source(
                XContentFactory.jsonBuilder()
                    .startObject()
                    .field("id", "2")
                    .field("title", "有人说你是王老五")
                    .field("content", "哈哈哈哈")
                    .endObject());

    // 设置更新的数据,使用ID查询,如果查找到了数据,就会更新UpdateRequest中的文档数据,
    UpdateRequest updateRequest =
        new UpdateRequest("blog", "article", "5")
            .doc(
                XContentFactory.jsonBuilder()
                    .startObject()
                    .field("id", "2")
                    .field("title", "你是王老五吗?")
                    .field("content", "你才是王老五!!!")
                    .endObject())
            .upsert(indexRequest);

    client.update(updateRequest).get();
  }
```


