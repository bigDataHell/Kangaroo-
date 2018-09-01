
scope : 范围

## Maven  Scope

 * 依赖范围scope 用来控制依赖和编译，测试，运行的classpath的关系.

 * 目前<scope>可以使用5个值：
 
    * compile，缺省值，适用于所有阶段，会随着项目一起发布。
    * provided，类似compile，期望JDK、容器或使用者会提供这个依赖。如servlet.jar。
    
      如果在jar中加入这个,则相当于这个jar包没有添加.在IDE上运行会报找不到jar包
      期待在集群上或容器中运行时这个jar包存在.
      
    * runtime，只在运行时使用，如JDBC驱动，适用运行和测试阶段。
    * test，只在测试时使用，用于编译和运行测试代码。不会随项目发布。
    * system，类似provided，需要显式提供包含依赖的jar，Maven不会在Repository中查找它。  -->
      
