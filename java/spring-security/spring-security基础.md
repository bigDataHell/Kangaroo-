
# spring-security 

__Spring Security 是 Spring 项目组中用来提供安全认证服务的框架__

在身份验证层面，`Spring Security`广泛支持各种身份验证模式，
这些验证模型绝大多数都由第三方提供，或则正在开发的有关标准机构提供的，例如 Internet Engineering Task
Force.作为补充，Spring Security 也提供了自己的一套验证功能。

# 1 安全包括两个主要操作。

* “认证”，是为用户建立一个他所声明的主体。主题一般式指用户，设备或可以在你系 统中执行动作的其他系
统。

* “授权”指的是一个用户能否在你的应用中执行某个操作，在到达授权判断之前，身份的主题已经由 身份验证
过程建立了。


# 2 Maven依赖

``` java
<dependencies>
<dependency>
<groupId>org.springframework.security</groupId>
<artifactId>spring-security-web</artifactId>
<version>5.0.1.RELEASE</version>
</dependency>
<dependency>
<groupId>org.springframework.security</groupId>
<artifactId>spring-security-config</artifactId>
<version>5.0.1.RELEASE</version>
</dependency>
</dependencies>
```
