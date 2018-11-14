

# Maven面试准备

## 1 Maven是什么?

__Maven是一个Apache下的项目管理工具,包含项目管理，插件以及目标的逻辑等。__

## 2 Maven的一系列执行流程：

 * 清理 
 * 编译 
 * 测试 
 * 报告
 * 打包 
 * 部署
 
 ## 3 Maven 的坐标
 
 ### 3.1 什么是坐标
 
  * 在平面几何中坐标（x,y）可以标识平面中唯一的一点
  
### 3.2 	Maven坐标主要组成

  * `groupId`：定义当前Maven项目隶属项目
  * `artifactId`：定义实际项目中的一个模块
  * `version`：定义当前项目的当前版本
  * `packaging`：定义该项目的打包方式
  
### 3.3	Maven为什么使用坐标？

 * Maven世界拥有大量构建，我们需要找一个用来唯一标识一个构建的统一规范
 * 拥有了统一规范，就可以把查找工作交给机器
 
## 4 Maven的依赖管理与依赖范围 

 * Scope :范围

 * 依赖范围scope 用来控制依赖和编译，测试，运行的classpath的关系.

 * 目前<scope>可以使用5个值：
 
    * `compile`，缺省值，适用于`所有阶段`，会随着项目一起发布。
    
    * `test`，只在`测试`时使用，用于编译和运行测试代码。不会随项目发布。
    
    * `provided`，`编译`、`测试`有效，`运行`无效.期望JDK容器或使用者会提供这个依赖。如servlet.jar。
    
      __如果在jar中加入这个,则相当于这个jar包没有添加.在IDE上运行会报找不到jar包
      期待在集群上或容器中运行时这个jar包存在.__
      
    * `runtime`，只在运行时使用，如JDBC驱动，适用`运行`和`测试`阶段。

    * `system`，类似provided，需要显式提供包含依赖的jar，Maven不会在Repository中查找它。 
    
## 5 maven仓库管理

### 5.1	何为Maven仓库？

  * 用来统一存储所有Maven共享构建的位置就是仓库
  
  * Maven仓库布局
  
     * 根据Maven坐标定义每个构建在仓库中唯一存储路径
     * 大致为：`groupId/artifactId/version/artifactId-version.packaging`
     
## 6 Maven的生命周期

### 6.1 何为生命周期？

  * Maven生命周期就是为了对所有的构建过程进行抽象和统一
  * 包括项目清理，初始化，编译，打包，测试，部署等几乎所有构建步骤
  
### 6.2 Maven三大生命周期

 * Maven三大生命周期
   * `clean`：清理项目的
   * `default`：构建项目的
   * `site`：生成项目站点的












      
