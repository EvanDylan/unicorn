# Unicorn是一站式Java平台幂等解决方案，它简单易用、支持事务一致性、极低的应用侵入性。

## 它具备以下特征：

1. 在开发时参考了dubbo优秀的微内核插件式的架构设计思想，所有框架中的主要功能组件都可以被任意扩展和替换。
2. 框架不强制耦合Spring生态圈，可以搭配其他应用框架使用。
3. 同时也为主流的框架Spring、Spring Boot做了相对的适配工作，可以做到开箱即用。
4. 支持强一致性事务。（业务执行失败事务回滚，不会记录幂等日志）

## 快速开始

1. 需要提前准备MySQL数据库，创建名为"unicorn"的数据库，并执行[脚本](https://github.com/EvanDylan/unicorn/tree/master/unicorn-samples/doc/samples.sql)。
2. 如果需要保证事务的强一致性，需要将日志表和业务表存放到一个库中。

参考[spring示例](https://github.com/EvanDylan/unicorn/tree/master/unicorn-samples/unicorn-samples-spring-xml)、[spring boot示例](https://github.com/EvanDylan/unicorn/tree/master/unicorn-samples/unicorn-samples-springboot)

## 核心功能介绍

unicorn主要核心功能组件有：注解、表达式解析、幂等处理策略、序列化、存储引擎等。

### Idempotent注解

```java
/**
 * declare annotated method must be idempotent.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Idempotent {

    int duration() default 1;

    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * default qualified method name
     */
    String name() default "";

    /**
     * el expression
     */
    String key() default "";

    /**
     * default idempotent return
     * @see IdempotentReturnWhenDuplicateRequestHandler
     * @see ThrowExceptionWhenDuplicateRequestHandler
     */
    String duplicateBehavior() default "default";

}
```

`Idempotent`注解用来声明方法是幂等的。`duration`和`TimeUnit`共同决定方法幂等有效时间，例如`@Idempotent(duration = 10, timeUnit = TimeUnit.SECONDS)`则表示被注解的方法在10秒钟之内，同样的请求会被当做重复请求拦截。`name`则用来标识当前方法名，默认命名规则是"当前类的权限定名+方法名"。`key`的值表示el表达式，通过表达式引擎进一步解析得最终结果作为当前请求的幂等关键字段。`duplicateBehavior`表示重复请求时处理策略，框架提供了：幂等返回、抛出`IdempotentException`异常两种策略，幂等返回作为默认策略。

### 表达式解析

框架默认集成了[spring el](https://docs.spring.io/spring-framework/docs/4.3.10.RELEASE/spring-framework-reference/html/expressions.html)、[jexl](https://commons.apache.org/proper/commons-jexl/reference/syntax.html)表达式解析引擎，默认启用Spring el引擎。当然也可以通过实现`ExpressionEngine`接口扩展集成自定义解析引擎。

### 幂等策略

框架提供了：幂等返回、抛出异常的两种策略，默认启用幂等返回策略。可以通过实现`DuplicateRequestHandler`接口扩展自定义的幂等处理策略，并在`Idempotent`注解属性中声明`duplicateBehavior`属性值为扩展名即可启用自定义处理策略。

### 序列化

当幂等策略为幂等返回时，框架需要存储方法在前一次的返回结果。对结果进行存储、读取的时候需要将Java对象进行序列化、反序列化，框架目前默认仅提供了`protostuff`作为序列化、反序列化。如有必要也可以通过实现`Serialization`接口扩展为其他的方式。

### 存储引擎：

该组件主要用来存储幂等日志，默认提供了基于MySQL关系型数据库实现的存储引擎，提供了强事务一致的特性。未来将会继续丰富其他类型的存储引擎，比如基于redis、本地文件等方式。



