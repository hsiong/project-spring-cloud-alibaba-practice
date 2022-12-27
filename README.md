

# 序言
重新写本文的意义在于, 深入学习微服务, 掌握"蓝绿、灰度、路由、限流、熔断、降级、隔离、追踪、流量染色、故障转移、多活"等概念和实现方式, 以及从原理上深入学习alibaba如何实现的这些功能

参考书籍目前主要有以下几个:
+ [Spring Cloud Alibaba 笔记: 马士兵](./Spring_Cloud_Alibaba_笔记:马士兵.pdf)
+ [Spring Cloud 构建微服务架构: 翟永超](https://blog.didispace.com/spring-cloud-learning/)
+ [SpringBoot-Labs: 芋道源码(Ruoyi owner)](https://github.com/YunaiV/SpringBoot-Labs)

# 解决问题

1. 应用拆分: 将一个单体应用按模块拆成多个应用, 实现**并发流量分担**和**功能水平拓展**

![image](https://user-images.githubusercontent.com/37357447/148905888-9da0a5d0-9ce4-40ec-aadd-654f4807bc9f.png)
2. 将**重复的代码**抽取出来， 做成独⽴的服务对外暴露，前端控制层(表现层)通过服务注册中心调⽤服务(服务层)

![image](https://user-images.githubusercontent.com/37357447/148906950-7bd61a3d-1a7c-4a47-891a-8f9f23e11835.png)

3. 每个服务都是一个可以独立运行的项目, **没有依赖关系**, 避免服务雪崩(上下游服务的崩溃, 导致整条服务链的崩溃)

![image](https://user-images.githubusercontent.com/37357447/148908164-33024b85-cf76-405e-93a7-0cc3f17e385c.png)

# 服务治理与负载均衡

## 预备工作
1. 先建一个单体应用
2. 将**重复代码**按模块抽离, 做成单体服务
3. 假设我们有以下三个微服务
    + shop-common 公共模块
    + shop-user 用户微服务  【端口: 807x】
    + shop-product 商品微服务  【端口: 808x】
    + shop-order 订单微服务  【端口: 809x】

## 用 RestTemplate 改造
1. 通过 `RestTemplate` 进行项目间的通讯

   ```java
   //通过restTemplate调用商品微服务
   String product = restTemplate.getForObject( "http://ip:shop-product-port/product/" + pid， String.class);
   ```

2. 但是 RestTemplate 把服务提供者的网络地址(ip，端口)等硬编码到了代码中, 存在以下问题
+ 一旦服务提供者地址变化，就需要手工修改代码
+ 一旦是多个服务提供者，无法实现负载均衡功能
+ 一旦服务变得越来越多，人工维护调用关系困难

## 引入服务治理

服务治理就是进行服务的自动化管理，服务治理是微服务架构中最核心最基本的模块。用于实现各个微服务的自动化注册与发现。 在[用 RestTemplate 改造](#用 RestTemplate 改造)的基础上，增加了注册中心，用以说明服务治理体系。

![image](https://user-images.githubusercontent.com/37357447/149058031-ee424fcd-7849-4b8d-b141-91bddf9fced6.png)

### 服务注册中心

服务注册中心是微服务架构非常重要的一个组件，在微服务架构里主要起到了协调者的一个作用。注册中心一般包含如下几个功能：

#### 服务发现

服务实例通过注册中心，获取到注册到其中的服务实例的信息，通过这些信息去请求它们提供的服务。

+ 服务注册：每个服务单元向注册中心登记自己提供服务的详细信息。并在注册中心形成一张服务的清单，服务注册中心需要以心跳的方式去监测清单中的服务是否可用，如果不可用，需要在服务清单中剔除不可用的服务。
+ 服务订阅：服务调用者订阅服务提供者的信息，注册中心向订阅者推送提供者的信息

#### 服务配置：

+ 配置订阅：服务提供者和服务调用者订阅微服务相关的配置
+ 配置下发：主动将配置推送给服务提供者和服务调用者

#### 服务健康检测

+ 检测服务提供者的健康情况，如果发现异常，执行服务剔除

### ### 常见的注册中心

+ Zookeeper  
  zookeeper是一个分布式服务框架，是 Apache Hadoop 的一个子项目，它主要是用来解决分布式应用中经常遇到的一些数据管理问题，如：统一命名服务、状态同步服务、集群管理、分布式应用 配置项的管理等。
+ Eureka  
  Eureka是Springcloud Netﬂix中的重要组件，主要作用就是做服务注册和发现。但是现在已经闭源。
+ Consul  
  Consul是基于GO语言开发的开源工具，主要面向分布式/服务化的系统提供服务注册、服务发现和配置管理的功能。  
  Consul的功能都很实用，其中包括：服务注册/发现、健康检查、Key/Value存储、多数据中心和分布式一致性保证等特性。  
  Consul本身只是一个二进制的可执行文件，所以安装和部署都非常简单，只需要从官网下载后，在执行对应的启动脚本即可。
+ Nacos  
  Nacos是一个更易于构建云原生应用的动态服务发现、配置管理和服务管理平台。它是Spring Cloud Alibaba组件之一，负责服务注册发现和服务配置，可以这样认为nacos=eureka+conﬁg。

### 使用 Nacos 改造

#### 本地搭建 Nacos 环境

1. 下载 [Nacos Release](https://github.com/alibaba/nacos/releases)

2. 启动 Nacos: ```sh startup.sh -m standalone```
3. 打开浏览器输入[http://localhost:8848/nacos](http://localhost:8848/nacos)，即可访问服务，默认密码是nacos/nacos

#### 项目集成 Nacos

1. Maven

   ```java
           <dependency>
               <groupId>com.alibaba.cloud</groupId>
               <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
               <version>${spring-alibaba.version}</version>
           </dependency>
   ```

2. Enable Nacos

   ```
   @EnableDiscoveryClient // 将该服务注册到 Nacos
   ```

3. 在 spring-boot 中配置 Nacos

   ```java
   spring:
     cloud:
       nacos:
         discovery:
           server-addr: Nacos-ip:Nacos-port
   ```

4. Nacos 服务调用

   ```Java
   // 从 Nacos 中获取服务地址(其中为实例名)
   ServiceInstance serviceInstance = discoveryClient.getInstances("shop-product").get(0);
   String url = serviceInstance.getHost() + ":" + serviceInstance.getPort();
   log.info(">>从nacos中获取到的微服务地址为:" + url);
   
   // 通过 restTemplate 调用商品微服务
   String product = restTemplate.getForObject("http://" + url + "/product/" + pid，String.class);
   log.info(">>商品信息，查询结果:" + JSON.toJSONString(product));
   orderService.save(product);
   return product;
   ```

## 实现服务调用中的负载均衡

### 什么是负载均衡

负载均衡是将负载(工作任务，访问请求)进行分摊到多个操作单元(服务器，组件)上进行执行。

根据负载均衡发生位置的不同，一般分为<b>服务端负载均衡</b>和<b>客户端负载均衡</b>。

+ 服务端负载均衡指的是发生在<u>服务提供者</u>一方，比如常见的nginx负载均衡.
+ 客户端负载均衡指的是发生在<u>服务请求</u>的一方，也就是在发送请求之前已经选好了由哪个实例处理请求。

我们在微服务调用关系中一般会选择客户端负载均衡，也就是在服务消费者来决定服务由哪个提供者执行。

![image](https://user-images.githubusercontent.com/37357447/149073780-5b2b9477-23c8-420f-85a9-90b99cafe209.png)



### 自定义实现负载均衡

1. 启动两个 shop-product 微服务，设置其端口分别为 8081 和 8082
2. 通过nacos查看微服务的启动情况，可以看到微服务 shop-product 的实例数变为了2

![image](https://user-images.githubusercontent.com/37357447/149077903-d87324c4-9c45-4b78-bafe-c565d6a2367c.png)

3. 修改服务调用代码, 实现负载均衡

   ```
       // 从nacos中获取服务地址
       ServiceInstance serviceInstance = discoveryClient.getInstances("shop-product").get(0);
   ```

   改为

   ```
       // 通过负载随机从nacos中获取服务地址
       List<ServiceInstance> instances = discoveryClient.getInstances("shop-product");
       int index = new Random().nextInt(instances.size());
       ServiceInstance serviceInstance = instances.get(index);
   ```

### 基于Ribbon实现负载均衡

1. 在服务调用(shop-order)的RestTemplate的生成方法上添加@LoadBalanced注解

```java
    @Bean
    @LoadBalanced // 如果RestTemplate上面有这个注解，那么这个RestTemplate调用的远程地址，会走负载均衡器。
    public RestTemplate restTemplate() {
    return new RestTemplate();
    }
```

2. 修改服务调用代码, 实现负载均衡

```java
/ 通过负载随机从nacos中获取服务地址
String url = "shop-product";

// 通过restTemplate调用商品微服务
// 由于restTemplate已经集成@LoadBalanced，那么会自动从注册中心拿到对应的地址
String product = restTemplate.getForObject("http://" + url + "/product/" + pid，String.class);
```

#### Ribbon支持的负载均衡策略

Ribbon内置了多种负载均衡策略，内部负载均衡的顶级接口为com.netflix.Loadbalancer.IRule，具体的负载策略如下图所示:

![image](https://user-images.githubusercontent.com/37357447/149099393-949b8855-c90b-43dd-aa77-6e1545f7bcb4.png)

|          策略名           |                           策略描述                           |                           实现说明                           |
| :-----------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
|         RetryRule         |               对选定的负载均衡策略加上重试机制               | [负载均衡策略之ROUNDROBINRULE和RETRYRULE源码解读](https://www.freesion.com/article/9980145836/) |
|      RoundRobinRule       |                轮询规则: 每次都取下一个服务器                |             轮询index，选择index对应位置的server             |
| WeightedResponseTimeRule  | 加权规则: 根据相应时间分配一个weight，server的响应时间越长，weight越小，被选中的可能性越低 | 开始的时候还没有权重列表，采用父类的轮询方式，有一个默认每30秒更新一次权重列表的定时任务，该定时任务会根据实例的响应时间来更新权重列表，choose方法做的事情就是，用一个(0，1)的随机double数乘以最大的权重得到randomWeight，然后遍历权重列表，找出第一个比randomWeight大的实例下标，然后返回该实例[负载均衡策略之WEIGHTEDRESPONSETIMERULE源码解读](https://www.freesion.com/article/3613148138/) |
| AvailabilityFilteringRule | 先过滤出故障的或并发请求大于阈值的服务实例，再以线性轮询的方式从过滤后的实例清单中选出一个 | [RIBBON过滤器AVAILABILITYFILTERINGRULE源码解读](https://www.freesion.com/article/1707151963/) |
|     BestAvailableRule     |                选择一个最小的并发请求的server                | [RIBBON源码之BESTAVAILABLERULE解读](https://www.freesion.com/article/4064150697/) |
|        RandomRule         |                 随机规则: 随机选择一个server                 |      随机选择一个数作为index，选择index对应位置的server      |
|     ZoneAvoidanceRule     | Ribbon默认规则: 先使用主过滤条件(区域负载器，选择最优区域)对所有实例过滤并返回过滤后的实例清单，依次使用次过滤条件列表中的过滤条件对主过滤条件的结果进行过滤，判断最小过滤数(默认1)和最小过滤百分比(默认0)，最后对满足条件的服务器则使用RoundRobinRule(轮询方式)选择一个服务器实例 | [RIBBON过滤器ZONEAVOIDANCERULE源码解读](https://www.freesion.com/article/5359151961/) |

> 使用方式: 除了ZoneAvoidanceRule外， 用以下方式将负载均衡策略注册到spring容器即可。

```java
    /**
     * 设置Ribbon的策略
     * @return
     */
    @Bean
    public IRule myRule(){
        // 选择一个最小的并发请求的server
        return new BestAvailableRule();
    }
```

## 基于 Feign 实现服务调用

Feign是 Spring Cloud 提供的一个声明式的伪 Http客 户端，它使得调用远程服务就像调用本地服务一样简单，只需要创建一个接口并添加一个注解即可。Nacos 很好的兼容了 Feign，Feign 默认集成了 Ribbon，所以在 Nacos 下使用 Fegin 默认就实现了负载均衡的效果。

1. 加入feign的依赖

```
        <!-- feign 版本与 spring-alibaba.version 一致 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
            <version>${spring-alibaba.version}</version>
        </dependency>
```

2. 在服务调用(shop-order)的启动类上加入@EnableFeignClients

```java
@SpringBootApplication
@EnableDiscoveryClient // 将该服务注册到nacos
@EnableFeignClients //开启Fegin
@Slf4j
public class OrderApplication {
```

3. 创建 ProductService， 通过 feign 调用商品微服务

```java
@FeignClient("shop-product") // 声明服务提供者的name
public interface ProductService {

    @GetMapping(value = "/product/{pid}")
    String product(@PathVariable("pid") String pid);

}
```

4. 修改服务调用如下

可以看到， 通过feign我们实现了两个微服务之间类似接口的调用， 而不用再借助restTemplate通过固定的http地址进行访问。

```java
@RestController
@Slf4j
public class OrderController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    //准备买1件商品
    @GetMapping("/order/prod/{pid}")
    public String order(@PathVariable("pid") String pid) {

        log.info(">>客户下单，这时候要调用商品微服务查询商品信息");

        // 通过feign调用
        String product = productService.product(pid);
        log.info(">>商品信息，查询结果:" + JSON.toJSONString(product));
        orderService.save(product);
        return product;
    }


}
```

> 若将feign的依赖单独放在微服务模块中，项目启动时会报 `NoSuchMethodError: com.google.common.collect.Sets$SetView.iterator()Lcom/google/common/collect/UnmodifiableIterator` 异常，猜测是因为feign依赖了ribbon导致

> 若重启过程中， 提示"No Feign Client for LoadBalancing defined.Did you forget to include spring-cloud-starter-Loadbalance"， 请优先修改feign的版本， 使feign的版本与您的spring-cloud-alibaba版本一致。  
> 请不要用LoadBalancing替换ribbon。在一段时间内， 仅支持轮询策略的Loadbalance还不能替代ribbon。

# 服务容错

## 高并发的问题


我们来模拟一个高并发的场景:

1. 改造OrderController
```java
@RestController
@Slf4j
public class OrderController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    //准备买1件商品
    @GetMapping("/order/prod/{pid}")
    public String order(@PathVariable("pid") String pid) {

        log.info(">>客户下单，这时候要调用商品微服务查询商品信息");

        // 通过feign调用
        String product = productService.product(pid);
        log.info(">>商品信息，查询结果:" + JSON.toJSONString(product));
        orderService.save(product);

        //模拟一次网络延时
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return product;
    }

}
```
2. 修改配置文件中tomcat的并发数
```java
server:
  port: 8091
  tomcat:
    threads:
      max: 50 #tomcat的最大并发值修改为50，默认是200
```
3. 使用jmeter进行压力测试  
   [Jmeter下载地址](https://jmeter.apache.org/downLoad_jmeter.cgi)  
   [Jmeter性能测试的基本操作](https://www.cnblogs.com/color-cc/p/13585144.html)
    + 设置线程并发数
      ![image](https://user-images.githubusercontent.com/37357447/149298447-0a20d5b6-8d81-455f-afa2-ec3fa13f171d.png)

    + 运行，打开结果树，观察结果. 此时会发现，由于order方法囤积了大量请求，导致ｍessage方法的访问出现了问题，这就是服务雪崩的雏形。
      ![image](https://user-images.githubusercontent.com/37357447/149298759-98c3c119-2cf1-4ae6-ab8a-38dd8d84dceb.png)

## 服务雪崩

在微服务架构中，我们将业务拆分成一个个的服务，服务与服务之间可以相互调用; 但是由于网络原因或自身的原因，服务一般无法保证100%可用。如果一个服务出现了问题，调用这个服务就会出现线程阻塞的情况，此时若有大量的请求涌入，就会出现多条线程阻塞等待，进而导致服务瘫痪。

由于服务与服务之间的依赖性，故障会传播，会对整个微服务系统造成灾难性的严重后果，这就是服务故障的 “雪崩效应”。  
雪崩发生的原因多种多样，有不合理的容量设计，或者是高并发下某一个方法响应变慢，亦或是某台机器的资源耗尽。我们无法完全杜绝雪崩源头的发生，故此，必须做好足够的`服务容错`，保证在一个服务发生问题，不会影响到其它服务的正常运行。

![image](https://user-images.githubusercontent.com/37357447/149300154-a1a80124-593f-4cf7-9d58-b2d44130cf18.png)

## 服务容错方案

### 常见的容错思路

常见的容错思路有隔离、超时、限流、熔断、降级这几种

+ 隔离  
  它是指将系统按照一定的原则划分为若干个服务模块，各个模块之间相对独立，无强依赖。当有故障发生时，能将问题和影响隔离在某个模块内部，而不扩散风险，不波及其它模块，不影响整体的系统服务。常见的隔离方式有：线程池隔离和信号量隔离。  
  ![image](https://user-images.githubusercontent.com/37357447/149301353-b7032ee5-2e35-40c5-9741-c6927efed0ba.png)
+ 超时  
  在上游服务调用下游服务的时候，设置一个最大响应时间，如果超过这个时间，下游未作出反应，就断开请求，释放掉线程。
+ 限流  
  限流就是限制系统的输入和输出流量已达到保护系统的目的。为了保证系统的稳固运行,一旦达到的需要限制的阈值,就需要限制流量并采取少量措施以完成限制流量的目的。
+ 熔断  
  在互联网系统中，当下游服务响应变慢或失败，上游服务为了保护系统整体的可用性，可以暂时切断对下游服务的调用。这种牺牲局部，保全整体的措施就叫做熔断。  
  ![image](https://user-images.githubusercontent.com/37357447/149302314-85a245e4-47ac-4375-909e-11c83f494e23.png)  
  服务熔断一般有三种状态：
    + 熔断关闭状态(Closed)  
      服务没有故障时，熔断器所处的状态，对调用方的调用不做任何限制
    + 熔断开启状态(Open)  
      后续对该服务接口的调用不再经过网络，直接执行本地的fallback方法
    + 半熔断状态(Half-Open)  
      尝试恢复服务调用，允许有限的流量调用该服务，并监控调用成功率。如果成功率达到预期，则说明服务已恢复，进入熔断关闭状态；如果成功率仍旧很低，则重新进入熔断关闭状态。
+ 降级  
  降级其实就是为服务提供一个备用方案，一旦服务无法正常调用，就使用备用方案。  
  ![image](https://user-images.githubusercontent.com/37357447/149303212-9dcd0961-cffd-4445-9d5d-365c99bf269b.png)

### 常见的容错组件
|     比较项     |                        Sentinel                        |        Hystrix        |           resilience4j           |
| :------------: | :----------------------------------------------------: | :-------------------: | :------------------------------: |
|    隔离策略    |               信号量隔离(并发线程数限流)               | 线程池隔离/信号量隔离 |            信号量隔离            |
|  熔断降级策略  |             基于响应时间、异常比率、异常数             |     基于异常比率      |      基于异常比率、响应时间      |
|  实时统计实现  |                  滑动窗口(LeapArray)                   | 滑动窗口(基于RxJava)  |         Ring Bit Buffer          |
|  动态规则配置  |                     支持多种数据源                     |    支持多种数据源     |              支持少              |
|     扩展性     |                          丰富                          |         丰富          |        仅支持接口形式扩展        |
|      限流      |            基于QPS，支持基于调用关系的限流             |        支持少         |           Rate Limiter           |
|    流量整形    |         支持预热模式、匀速器模式、预热排队模式         |           X           |      简单的Rate Limiter模式      |
| 系统自适应保护 |                           √                            |           X           |                X                 |
|     控制台     | 开箱即用的控制台，可配置规则、查看秒级监控、机器发现等 |    简单的监控查看     | 不提供控制台，可对接其它监控系统 |

## 监控 & 容错 Sentinal

1. 安装Sentinel控制台

[下载jar包](https://github.com/alibaba/Sentinel/releases)

2. 启动 Sentinal

```shell
java -Dserver.port=8060 -Dcsp.sentinel.dashboard.server=localhost:8060 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.8.1.jar
```

3. 在服务调用(shop-order)集成 Sentinal

> application.yml

```java
spring:
  application:
    name: shop-order
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
    sentinel:
      transport:
        port: 8061 #跟控制台交流的端口 ,随意指定一个未使用的端口即可
        dashboard: Sentinal-ip:Sentinal-port # 指定控制台服务的地址
```

### Sentinal 基本概念

+ 资源  
  资源就是Sentinel要保护的东西。它可以是Java应用程序中的任何内容，可以是一个服务(接口)，也可以是一个方法，甚至可以是一段代码。

+ 规则  
  规则就是用来定义如何进行保护资源的，作用在资源之上, 定义以什么样的方式保护资源，主要包括流量控制规则、熔断降级规则以及系统保护规则。

### Sentinal 重要功能

我们需要做的事情，就是在 Sentinel 的资源上配置各种各样的规则，来实现各种容错的功能。

1. 流量控制(包括隔离、限流、超时、流量整形等等)  
   流量控制用于调整网络包的数据。任意时间到来的请求往往是随机不可控的，而系统的处理能力是有限的。我们需要根据系统的处理能力对流量进行控制。Sentinel作为一个调配器，可以根据需要把随机的请求调整成合适的形状。
2. 熔断/降级
   当检测到调用链路中某个资源出现不稳定的表现，例如请求响应时间长或异常比例升高的时候，则对这个资源的调用进行限制，让请求快速失败，避免影响到其它的资源而导致级联故障。  
   Sentinel 对这个问题采取了两种手段:
    + 通过并发线程数进行限制  
      Sentinel通过限制资源并发线程的数量，来减少不稳定资源对其它资源的影响。当某个资源出现不稳定的情况下，例如响应时间变长，对资源的直接影响就是会造成线程数的逐步堆积。当线程数在特定资源上堆积到一定的数量之后，对该资源的新请求就会被拒绝。堆积的线程完成任务后才开始继续接收请求。
    + 通过响应时间对资源进行降级  
      除了对并发线程数进行控制以外，Sentinel还可以通过响应时间来快速降级不稳定的资源。当依赖的资源出现响应时间过长后，所有对该资源的访问都会被直接拒绝，直到过了指定的时间窗口之后才重新恢复。
3. 系统负载保护
   Sentinel同时提供系统维度的自适应保护能力。当系统负载较高的时候，如果还持续让请求进入可能会导致系统崩溃无法响应。在集群环境下，会把本应这台机器承载的流量转发到其它的机器上去。如果这个时候其它的机器也处在一个边缘状态的时候，Sentinel提供了对应的保护机制，让系统的入口流量和系统的负载达到一个平衡，保证系统在能力范围之内处理最多的请求。
4. 实时统计实现等
