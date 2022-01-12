
# spring-cloud-alibaba-base
spring cloud alibaba learning

# 第一章 微服务介绍
## 1.1 系统架构演变
单体应用架构--->垂直应用架构--->分布 式架构--->SOA(资源调度和治理中心)架构--->微服务架构--->Service Mesh(服务网格化)  
### 1.1.1 单体应用架构
互联网早期，一般的网站应用流量较小，只需一个应用，将所有功能代码都部署在一起就可以，这样可以减少开发、部署和维护的成本。   

![image](https://user-images.githubusercontent.com/37357447/148877457-e20d81ea-4bd5-4fa0-92e1-5678b69eef8f.png)

+ 优点：
    + 项目架构简单，小型项目的话，开发成本低
    + 项目部署在一个节点上，维护方便  

+ 缺点：
    + 全部功能集成在一个工程中，对于大型项目来讲不易开发和维护
    + 项目模块之间紧密耦合，单点容错率低
    + 无法针对不同模块进行针对性优化和水平扩展
### 1.1.2 垂直应用架构
随着访问量的逐渐增大，单一应用很难应付比较大的访问量。  
这时候需要将原来的一个应用拆成互不相干的几个应用，以提升效率。  
这样拆分完毕之后，一旦用户访问量变大，只需要增加系统的节点就可以了，而无需额外部署。  

![image](https://user-images.githubusercontent.com/37357447/148905888-9da0a5d0-9ce4-40ec-aadd-654f4807bc9f.png)

+ 优点：
    + 系统拆分实现了流量分担，解决了并发问题，而且可以针对不同模块进行优化和水平扩展 一个系统的问题不会影响到其他系统，提高容错率
+ 缺点：
    + 系统之间相互独立，无法进行相互调用
    + 系统之间相互独立，会有重复的开发任务

### 1.1.3 分布式架构
当垂直应用越来越多，重复的业务代码就会越来越多。这时候，我们就思考可不可以将重复的代码抽取出来，做成统一的业务层作为独立的服务，然后由前端控制层调用不同的业务层服务呢？ 
这就产生了新的分布式系统架构。它将把工程拆分成<b style='color:red'>表现层</b>和<b style='color:red'>服务层</b>两个部分，服务层中包含业务逻辑。表现层只需要处理和页面的交互，业务逻辑都是调用服务层的服务来实现。  

![image](https://user-images.githubusercontent.com/37357447/148906950-7bd61a3d-1a7c-4a47-891a-8f9f23e11835.png)

+ 优点:
    + 使用注册中心解决了服务间调用关系的自动调节
+ 缺点:
    + 服务间会有依赖关系，一旦某个环节出错会影响较大(服务雪崩)(因为所有服务都部署在同一个服务层上, 并且具有上下游关系)
    + 服务关心复杂，运维、测试部署困难

### 1.1.5 微服务架构
微服务架构在某种程度上是面向服务的架构SOA继续发展的下一步，它更加强调服务的"彻底拆分"，拆分成更小的服务，每个服务都是一个可以独立运行的项目。  

![image](https://user-images.githubusercontent.com/37357447/148908164-33024b85-cf76-405e-93a7-0cc3f17e385c.png)

+ 优点：
    + 服务原子化拆分，独立打包、部署和升级，保证每个微服务清晰的任务划分，利于扩展微服务之间采用Restful等轻量级http协议相互调用
+ 缺点：
    + 分布式系统开发的技术成本高（容错、分布式事务等）

## 1.2 微服务架构介绍
### 1.2.1 引入
在微服务化后, 伴随<b style='color:red'>服务划分粒度</b>越来越细, 我们也由原先的单体应用, 逐步转变成了多个微服务构成的服务集群, 那么, 势必会出现以下问题, 以及目前主流的解决思路。

|问题|解决思路|解决办法|
|:-:|:-:|:-:|
|如何管理他们?|服务治理 注册中心[服务注册 发现 剔除]|nacos|
|他们之间如何通讯?|restful rpc||
|客户端怎么访问他们?|网关||
|一旦出现问题，应该如何自处理?|服务容错||
|一旦出现问题，应该如何排错?|链路追踪||
|伴随新的架构体系，应如何架构设计?|事务驱动 领域驱动||  

![image](https://user-images.githubusercontent.com/37357447/148911880-67edd711-a1cb-4a5d-b8e9-546038b48fa0.png)

### 1.2.2 常用概念
#### 1.2.2.1 服务治理
服务治理就是进行服务的自动化管理，其核心是服务的自动注册与发现。  
+ <b style='color:red'>服务注册</b>：服务实例将自身服务信息注册到注册中心。  
+ <b style='color:red'>服务发现</b>：服务实例通过注册中心，获取到注册到其中的服务实例的信息，通过这些信息去请求它们提供的服务。  
+ <b style='color:red'>服务剔除</b>：服务注册中心将出问题的服务自动剔除到可用列表之外，使其不会被调用到。  

![image](https://user-images.githubusercontent.com/37357447/148913750-13ef5a3a-bfaa-4dd3-b824-e39b49f16276.png)

#### 1.2.2.2 服务调用
在微服务架构中，通常存在多个服务之间的远程调用的需求。目前主流的远程调用技术有基于HTTP的<b style='color:red'>RESTFUL接口</b>以及基于TCP的<b style='color:red'>RPC协议</b>。一般来说, RESTFUL接口即可满足我们大多数开发需求, 配置和适用上也更方便、灵活。
+ REST(Representational State Transfer)  
这是一种HTTP调用的格式，更标准，更通用，无论哪种语言都支持http协议
+ RPC(Remote Promote Call)  
一种进程间通信方式。允许像调用本地服务一样调用远程服务。RPC框架的主要目标就是让远程服务调用更简单、透明。  
RPC框架负责屏蔽底层的传输方式、序列化方式和通信细节。开发人员在使用的时候只需要了解谁在什么位置提供了什么样的远程服务接口即可，并不需要关心底层通信细节和调用过程。  

|比较项|RESTFUL|RPC|
|:-:|:-:|:-:|
|通讯协议|HTTP|常用TCP, 亦支持UDP HTTP|
|性能|较低|较高|
|灵活度|高|低|
|应用|微服务|SOA|

#### 1.2.2.3 服务网关
随着微服务的不断增多，不同的微服务一般会有不同的网络地址，而外部客户端可能需要调用多个服务的接口才能完成一个业务需求, 如果让客户端直接与各个微服务通信可能出现：
+ 客户端需要调用不同的url地址，增加难度
+ 在一定的场景下，存在跨域请求的问题                                    
+ 每个微服务都需要进行单独的身份认证  

为了解决这些问题, 我们引入了<b style='color:red'>API网关</b>。  
API网关是将所有API调用统一接入到API网关层，由网关层统一接入和输出。有了网关之后，各个API服务提供团队可以专注于自己的的业务逻辑处理，而API网关更专注于安全、流量、路由等问题。  
一个API网关的基本功能包括：
+ 统一接入
+ 安全防护
+ 协议适配
+ 流量管控
+ 长短链接支持
+ 容错能力

![image](https://user-images.githubusercontent.com/37357447/148919726-5e2e391e-57c0-4398-9813-4796e7532cea.png)

#### 1.2.2.4 服务容错
在微服务当中，一个请求经常会涉及到调用几个服务，如果其中某个服务不可用，没有做服务容错的话，极有可能会造成一连串的服务不可用，这就是雪崩效应。我们没法预防雪崩效应的发生，只能尽可能去做好容错。  
<b style='color:red'>服务容错</b>的三个核心思想是：
+ 不被<b style='color:red'>外界</b>环境影响
+ 不被<b style='color:red'>上游</b>请求压垮
+ 不被<b style='color:red'>下游</b>响应拖垮  

![image](https://user-images.githubusercontent.com/37357447/148924546-1bc740eb-14ab-4016-b8d2-6f7a90f3f8da.png)

#### 1.2.2.5 链路追踪                                                               
随着微服务架构的流行，服务按照不同的维度进行拆分，一次请求往往需要涉及到多个服务。互联网应用构建在不同的软件模块集上，这些软件模块，有可能是由不同的团队开发、可能使用不同的编程语言来实现、有可能布在了几千台服务器，横跨多个不同的数据中心。因此，就需要对一次请求涉及的多个服务链路进行日志记录，性能监控即<b style='color:red'>链路追踪</b>。  

### 1.2.3 微服务架构的常见解决方案
1. ServiceComb
2. SpringCloud
3. SpringCloud Alibaba

## 1.3 SpringCloud Alibaba介绍
<b style='color:red'>Spring Cloud Alibaba</b> 致力于提供微服务开发的一站式解决方案。此项目包含开发分布式应用微服务的必需组件，方便开发者通过 Spring Cloud 编程模型轻松使用这些组件来开发分布式应用服务。  
依托 Spring Cloud Alibaba，您只需要添加一些注解和少量配置，就可以将 Spring Cloud 应用接入阿里微服务解决方案，通过阿里中间件来迅速搭建分布式应用系统。

### 1.3.1 主要功能
+ <b style='color:red'>服务注册与发现</b> ：适配 Spring Cloud 服务注册与发现标准，默认集成了 Ribbon 的支持。  
+ <b style='color:red'>服务限流降级</b> ：默认支持 WebServlet、  WebFlux，   OpenFeign、  RestTemplate、  Spring Cloud
Gateway，   Zuul，   Dubbo 和 RocketMQ 限流降级功能的接入，可以在运行时通过控制台实时修改限流降级规则，还支持查看限流降级 Metrics 监控。  
+ <b style='color:red'>分布式配置管理</b> ：支持分布式系统中的外部化配置，配置更改时自动刷新。  
+ <b style='color:red'>消息驱动能力</b> ：基于 Spring Cloud Stream 为微服务应用构建消息驱动能力。  
+ <b style='color:red'>分布式事务</b> ：使用 @GlobalTransactional 注解，   高效并且对业务零侵入地解决分布式事务问题。  
+ <b style='color:red'>分布式任务调度</b>：提供秒级、精准、高可靠、高可用的定时（基于 Cron 表达式）任务调度服务。 同时提供分布式的任务执行模型，如网格任务。网格任务支持海量子任务均匀分配到所有 Worker（schedulerx-client）上执行。  
+ 阿里云对象存储：阿里云提供的海量、安全、低成本、高可靠的云存储服务。支持在任何应用、任 何时间、任何地点存储和访问任意类型的数据。  
+ 阿里云短信服务：覆盖全球的短信服务，友好、高效、智能的互联化通讯能力，帮助企业迅速搭建客户触达通道。

### 1.3.2 组件
+ <b style='color:red'>Nacos</b>：一个更易于构建云原生应用的动态服务发现、配置管理和服务管理平台。
+ <b style='color:red'>Sentinel</b>：把流量作为切入点，从流量控制、熔断降级、系统负载保护等多个维度保护服务的稳定性。
+ <b style='color:red'>Dubbo</b>：Apache Dubbo™ 是一款高性能 Java RPC 框架。
+ <b style='color:red'>Seata</b>：阿里巴巴开源产品，一个易于使用的高性能微服务分布式事务解决方案。
+ <b style='color:red'>RocketMQ</b>：一款开源的分布式消息系统，基于高可用分布式集群技术，提供低延时的、高可靠的消息发布与订阅服务。
+ <b style='color:red'>Alibaba Cloud ACM</b>：一款在分布式架构环境中对应用配置进行集中管理和推送的应用配置中心产品。
+ <b style='color:red'>Alibaba Cloud SchedulerX</b>: 阿里中间件团队开发的一款分布式任务调度产品，提供秒级、精准、高可靠、高可用的定时（基于 Cron 表达式）任务调度服务。
+ Alibaba Cloud OSS: 阿里云对象存储服务（Object Storage Service，简称 OSS），是阿里云提供的海量、安全、低成本、高可靠的云存储服务。您可以在任何应用、任何时间、任何地点存储和 访问任意类型的数据。
+ Alibaba Cloud SMS: 覆盖全球的短信服务，友好、高效、智能的互联化通讯能力，帮助企业迅速搭建客户触达通道。

# 第二章 微服务环境搭建
以电商项目中的商品、订单、用户为例, 进行学习。
## 2.1 案例准备
### 2.1.1 技术选型

|中间件|版本|
|:-:|:-:|
|java|1.8|
|encoding|utf-8|
|maven|3.6.3|
|spring|2.3.12.RELEASE|
|spring-cloud|Hoxton.SR12|
|spring-cloud-alibaba|2.2.7.RELEASE|
|nacos|2.0.3|
|fastjson|1.2.56|
|lombok|1.18.20|
|MySQL|5.7(任意版本都可以)|

 <b style='color:red'>注意: </b>若未满足版本对应关系, 将会出现各种问题 [spring cloud alibaba 版本对应](https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E)  
        
### 2.1.2 模块设计
+ spring-cloud-alibaba-base 父工程    
    + shop-common 公共模块  
    + shop-user 用户微服务  【端口: 807x】 
    + shop-product 商品微服务  【端口: 808x】  
    + shop-order 订单微服务  【端口: 809x】 

### 2.1.3 微服务调用
在微服务架构中，最常见的场景就是微服务之间的相互调用。  
我们以电商系统中常见的用户下单为例来演示微服务的调用：客户向订单微服务发起一个下单的请求，在进行保存订单之前需要调用商品微服务查询商品的信息。  
我们一般把服务的主动调用方称为 <b style='color:red'>服务消费者</b>，把服务的被调用方称为 <b style='color:red'>服务提供者</b>。

![image](https://user-images.githubusercontent.com/37357447/148930992-aec96c32-f60f-4e7b-81d0-ab7791ed9559.png)

## 2.2 创建父工程
创建一个maven工程, 名字为spring-cloud-alibaba-base，pom.xml如下

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>spring-cloud-alibaba-base</artifactId>
    <version>1.0-SNAPSHOT</version>
    <modules>
        <module>shop-common</module>
        <module>shop-user</module>
        <module>shop-product</module>
        <module>shop-order</module>
    </modules>


    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.12.RELEASE</version>
    </parent>
    <packaging>pom</packaging>
    <properties>
        <java.version>1.8</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <spring.version>2.3.12.RELEASE</spring.version>
        <spring-cloud.version>Hoxton.SR12</spring-cloud.version>
        <spring-alibaba.version>2.2.7.RELEASE</spring-alibaba.version>
        <!--
        spring cloud alibaba 版本对应
        https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E
        nacos 2.0.3
        -->
    </properties>
    <dependencies>

<!--        &lt;!&ndash; 本地maven配置问题, parent不会下载, 故引入后下载包后注释 &ndash;&gt;
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-parent</artifactId>
            <version>${spring.version}</version>
        </dependency>-->

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>

        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-alibaba-dependencies</artifactId>
            <version>${spring-alibaba.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>
    
</project>
```

## 2.3 创建公共模块shop-common
创建shop-common模块. pom.xml如下

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>spring-cloud-alibaba-base</artifactId>
        <groupId>org.example</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>

    <modelVersion>4.0.0</modelVersion>
    <artifactId>shop-common</artifactId>
    <dependencies>
<!--        <dependency>-->
<!--            <groupId>org.springframework.boot</groupId>-->
<!--            <artifactId>spring-boot-starter-data-jpa</artifactId>-->
<!--        </dependency>-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.56</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.48</version>
        </dependency>
    </dependencies>

</project>
```

## 2.4 创建微服务
### 2.4.1 创建微服务步骤
 <b style='color:red'>注意: </b>我们认为您已经掌握了创建和部署spring-boot单体应用的能力, 故此只做简要配置说明 
步骤如下:
1. 创建模块
2. 创建application.yml文件
```
server:
  port: 端口号
spring:
  application:
    name: 模块名
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/mysql?useUnicode=true&characterEncoding=utf8&autoReconnect=true&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true
    username: root
    password:

```
3. 创建SpringBoot主类 xxApplication
```
@SpringBootApplication
@Slf4j
public class xxApplication {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(xxApplication.class, args);

        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostName();
        String port = env.getProperty("server.port");
        String path = "/" + env.getProperty("spring.application.name");
        log.warn("\n----------------------------------------------------------\n\t" +
                 "Application xxx-Boot is running! Access URLs:\n\t" +
                 "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
                 "----------------------------------------------------------");

    }

    @Bean
    public RestTemplate getRestTemplate() {
        // 将RestTemplate注册到spring容器
        return new RestTemplate();
    }

}
```
4. 加入配置文件 pom.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>spring-cloud-alibaba-base</artifactId>
        <groupId>org.example</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>模块名</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.example</groupId>
            <artifactId>shop-common</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>

</project>

```
5. 创建必要的接口 xxController

### 2.4.2 创建用户微服务
#### 2.4.2.1 创建模块
完成2.4.1所示的1-4步
#### 2.4.2.2 创建用户接口
```
@RestController
@Slf4j
public class UserController {

    @GetMapping("/user/{pid}")
    public String product(@PathVariable("pid") String pid) {
        // 向服务消费者返回用户信息
        String msg = "用户" + pid;
        return msg;
    }

}
```
### 2.4.3 创建商品微服务
#### 2.4.3.1 创建模块
完成2.4.1所示的1-4步
#### 2.4.3.2 创建商品接口
```
@RestController
@Slf4j
public class ProductController {

    @GetMapping("/product/{pid}")
    public String product(@PathVariable("pid") String pid) {
        String msg = "商品" + pid;
        log.info(msg);
        return msg;
    }

}
```
### 2.4.4 创建订单微服务
#### 2.4.4.1 创建模块
完成2.4.1所示的1-4步
#### 2.4.4.2 创建保存订单服务
##### 2.4.4.2.1 新建接口OrderService
```
public interface OrderService {

    void save(String msg);

}
```
##### 2.4.4.2.2 新建实现类OrderServiceImple
```
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Override
    public void save(String msg) {
        log.info("保存订单");
    }

}
```
#### 2.4.4.3 创建订单接口

```
@RestController
@Slf4j
@EnableDiscoveryClient
public class OrderController {

    @Autowired
    private RestTemplate restTemplate; @Autowired private OrderService orderService;

    //准备买1件商品
    @GetMapping("/order/prod/{pid}")
    public String order(@PathVariable("pid") Integer pid) {
        log.info(">>客户下单，这时候要调用商品微服务查询商品信息");
        //通过restTemplate调用商品微服务
        String product = restTemplate.getForObject( "http://localhost:8081/product/" + pid, String.class);
        log.info(">>商品信息,查询结果:" + JSON.toJSONString(product));
        orderService.save(product);
        return product;
    }
}
```

### 2.4.5 总结
至此, 我们就完成了项目的基础搭建, 具体源码可以参考base分支

