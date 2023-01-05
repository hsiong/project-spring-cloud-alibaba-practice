- [序言](#序言)
- [解决问题](#解决问题)
- [技术选型](#技术选型)
- [服务治理与负载均衡](#服务治理与负载均衡)
  - [预备工作](#预备工作)
  - [用 RestTemplate 改造](#用-resttemplate-改造)
  - [引入服务治理](#引入服务治理)
    - [服务注册中心](#服务注册中心)
      - [服务发现](#服务发现)
      - [服务配置：](#服务配置)
      - [服务健康检测](#服务健康检测)
    - [常见的注册中心](#常见的注册中心)
    - [使用 Nacos](#使用-nacos)
      - [使用 docker 搭建 Nacos 环境](#使用-docker-搭建-nacos-环境)
      - [项目集成 Nacos](#项目集成-nacos)
  - [实现服务调用中的负载均衡](#实现服务调用中的负载均衡)
    - [什么是负载均衡](#什么是负载均衡)
    - [自定义实现负载均衡](#自定义实现负载均衡)
    - [基于Ribbon实现负载均衡](#基于ribbon实现负载均衡)
      - [Ribbon支持的负载均衡策略](#ribbon支持的负载均衡策略)
  - [基于 Feign 实现服务调用](#基于-feign-实现服务调用)
- [服务容错](#服务容错)
  - [高并发的问题](#高并发的问题)
  - [服务雪崩](#服务雪崩)
  - [服务容错方案](#服务容错方案)
    - [常见的容错思路](#常见的容错思路)
    - [常见的容错组件](#常见的容错组件)
  - [Sentinal 简介](#sentinal-简介)
    - [Sentinal 基本概念](#sentinal-基本概念)
    - [Sentinal 重要功能](#sentinal-重要功能)
  - [Sentinel 流控规则](#sentinel-流控规则)
    - [流控模式实战](#流控模式实战)
    - [@SentinelResource 注解实战](#sentinelresource-注解实战)
    - [流控模式-直接](#流控模式-直接)
    - [流控模式-关联](#流控模式-关联)
    - [流控模式-链路](#流控模式-链路)
  - [Sentinel 降级规则](#sentinel-降级规则)
    - [慢调用比例](#慢调用比例)
    - [异常比例](#异常比例)
    - [异常数](#异常数)
  - [Sentinel 热点规则](#sentinel-热点规则)
    - [热点规则基础](#热点规则基础)
    - [热点规则高级规则](#热点规则高级规则)
  - [Sentinel 授权规则](#sentinel-授权规则)
  - [Sentinel 系统规则](#sentinel-系统规则)
    - [背景](#背景)
    - [系统规则概述](#系统规则概述)
    - [系统规则原理](#系统规则原理)
    - [系统规则实战](#系统规则实战)
  - [Sentinel 自定义异常](#sentinel-自定义异常)
    - [背景](#背景-1)
    - [实战](#实战)
  - [Sentinel 规则持久化](#sentinel-规则持久化)
    - [API 模式：使用客户端规则 API 配置规则](#api-模式使用客户端规则-api-配置规则)
    - [拉模式：使用文件配置规则](#拉模式使用文件配置规则)
    - [推模式](#推模式)
      - [推模式：使用 Nacos 配置规则](#推模式使用-nacos-配置规则)
      - [推模式：使用 ZooKeeper 配置规则](#推模式使用-zookeeper-配置规则)
      - [推模式：使用 Apollo 配置规则](#推模式使用-apollo-配置规则)
      - [推模式：使用 Redis 配置规则](#推模式使用-redis-配置规则)
    - [规则持久化实战(以 Nacos 为例)](#规则持久化实战以-nacos-为例)
  - [Sentinel整合Feign](#sentinel整合feign)
    - [使用 Fallback 实现](#使用-fallback-实现)
    - [使用 FallbackFactory 实现](#使用-fallbackfactory-实现)


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

# 技术选型

|          中间件           |      版本      |
| :-----------------------: | :------------: |
|           java            |       17       |
|         encoding          |     utf-8      |
|           maven           |     3.6.3      |
|         fastjson          |    任意版本    |
|          lombok           |    任意版本    |
|          spring           | 2.3.12.RELEASE |
|       spring-cloud        |  Hoxton.SR12   |
|   spring-cloud-alibaba    | 2.2.9.RELEASE  |
|           feign           | 2.2.9.RELEASE  |
|           nacos           |     2.1.0      |
|         sentinel          |     1.8.4      |
| sentinel-datasource-nacos |     1.8.4      |

<! -- vscode 格式化 -->

注意: 若未满足版本对应关系， 将会出现各种问题 [spring cloud alibaba 版本对应](https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E)

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

### 常见的注册中心

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

### 使用 Nacos

#### 使用 docker 搭建 Nacos 环境
```shell
docker run --name nacos-standalone \
-e MODE=standalone \
--restart=always \
-p 8848:8848 \
-p 9848:9848 \
-p 9849:9849 \
-d nacos/nacos-server:v2.1.0-slim
```

> 非 mac m1 chip 改为 **v2.1.0**

> 若出现 `Client not connected, current status:STARTING`, 请尝试将 Spring Alibaba Version 改为 2.2.6.RELEASE
>
> https://www.cnblogs.com/life-x-yk/articles/16186158.html

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

|          策略名           |                                                                                                                                      策略描述                                                                                                                                      |                                                                                                                                                                                            实现说明                                                                                                                                                                                            |
| :-----------------------: | :--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------: |
|         RetryRule         |                                                                                                                          对选定的负载均衡策略加上重试机制                                                                                                                          |                                                                                                                                                [负载均衡策略之ROUNDROBINRULE和RETRYRULE源码解读](https://www.freesion.com/article/9980145836/)                                                                                                                                                 |
|      RoundRobinRule       |                                                                                                                           轮询规则: 每次都取下一个服务器                                                                                                                           |                                                                                                                                                                              轮询index，选择index对应位置的server                                                                                                                                                                              |
| WeightedResponseTimeRule  |                                                                                             加权规则: 根据相应时间分配一个weight，server的响应时间越长，weight越小，被选中的可能性越低                                                                                             | 开始的时候还没有权重列表，采用父类的轮询方式，有一个默认每30秒更新一次权重列表的定时任务，该定时任务会根据实例的响应时间来更新权重列表，choose方法做的事情就是，用一个(0，1)的随机double数乘以最大的权重得到randomWeight，然后遍历权重列表，找出第一个比randomWeight大的实例下标，然后返回该实例[负载均衡策略之WEIGHTEDRESPONSETIMERULE源码解读](https://www.freesion.com/article/3613148138/) |
| AvailabilityFilteringRule |                                                                                             先过滤出故障的或并发请求大于阈值的服务实例，再以线性轮询的方式从过滤后的实例清单中选出一个                                                                                             |                                                                                                                                                 [RIBBON过滤器AVAILABILITYFILTERINGRULE源码解读](https://www.freesion.com/article/1707151963/)                                                                                                                                                  |
|     BestAvailableRule     |                                                                                                                           选择一个最小的并发请求的server                                                                                                                           |                                                                                                                                                       [RIBBON源码之BESTAVAILABLERULE解读](https://www.freesion.com/article/4064150697/)                                                                                                                                                        |
|        RandomRule         |                                                                                                                            随机规则: 随机选择一个server                                                                                                                            |                                                                                                                                                                       随机选择一个数作为index，选择index对应位置的server                                                                                                                                                                       |
|     ZoneAvoidanceRule     | Ribbon默认规则: 先使用主过滤条件(区域负载器，选择最优区域)对所有实例过滤并返回过滤后的实例清单，依次使用次过滤条件列表中的过滤条件对主过滤条件的结果进行过滤，判断最小过滤数(默认1)和最小过滤百分比(默认0)，最后对满足条件的服务器则使用RoundRobinRule(轮询方式)选择一个服务器实例 |                                                                                                                                                     [RIBBON过滤器ZONEAVOIDANCERULE源码解读](https://www.freesion.com/article/5359151961/)                                                                                                                                                      |

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

> 若重启过程中， 提示"No Feign Client for LoadBalancing defined.Did you forget to include spring-cloud-starter-Loadbalance"， 请优先修改feign的版本，使feign的版本与您的spring-cloud-alibaba版本一致。

> 请不要用LoadBalancing替换ribbon。在一段时间内，仅支持轮询策略的Loadbalance还不能替代ribbon。

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

服务容错的三个核心思想是：

+ 不被外界环境影响
+ 不被上游请求压垮
+ 不被下游响应拖垮

![image](https://user-images.githubusercontent.com/37357447/148924546-1bc740eb-14ab-4016-b8d2-6f7a90f3f8da.png)

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

## Sentinal 简介

1. docker 启动 sentinal

```shell
docker run --name sentinel -p 8858:8858 -d royalwang/sentinel-dashboard
```
> 访问: http://localhost:8858, sentinel/sentinel

> 该镜像支持 linux/arm64, 即支持 Mac m1 chip

2. 在服务调用(shop-order)集成 Sentinal

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

3. 在需要流控的服务调用方 shop-order, 新增OrderSentinelController

```java
@RestController
public class OrderSentinelController {

    @GetMapping("/sentinel/message")
    public String message() {
        return "1";
    }

    @GetMapping("/sentinel/message2")
    public String message2() {
        return "2";
    }
}
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

## Sentinel 流控规则

流量控制，其原理是监控应用流量的QPS(每秒查询率)或并发线程数等指标，当达到指定的阈值时对流量进行控制，以避免被瞬时的流量高峰冲垮，从而保障应用的高可用性。  
注意：

+ Sentinel本身是一个流量监控服务，需要有对应微服务的接口访问，才会在控制台中显示。
+ 流控规则在Sentinel重启或者微服务重启有可能会被删除，以后会有专门的章节([Sentinal规则持久化](#sentinal规则持久化))来介绍流控规则的持久化配置。

点击簇点链路，我们就可以看到访问过的接口地址，然后点击对应的流控按钮，进入流控规则配置页面。新增流控规则界面如下

![image](https://user-images.githubusercontent.com/37357447/149312642-cb3fb7ee-f212-44dd-87bf-615b9fa5b0a2.png)

+ 资源名(：需要限制的请求路径、方法等等
+ 针对来源：指定对哪个微服务进行限流，指default，意思是不区分来源，全部限制
+ 阈值类型/单机阈值：
    + QPS(每秒请求数量)：当调用该接口的QPS达到阈值的时候，进行限流
    + 线程数：当调用该接口的线程数达到阈值的时候，进行限流
+ 是否集群：暂不需要集群
+ 流控模式：sentinel共有三种流控模式，分别是：
    + 直接(默认)：资源达到限流条件时，开启限流
    + 关联：当关联的资源达到限流条件时，开启限流[当接口A达到限流阈值时, 接口B也限流]
    + 链路：面向service层用@SentinelResource("xxx")标记的资源，当从某个接口过来的资源达到限流条件时，开启限流(一般来说，该配置不常用)
+ 流控效果:
    + 快速失败(默认): 直接失败，抛出异常，不做任何额外的处理，是最简单的效果
    + Warm Up：它从开始阈值到最大QPS阈值会有一个缓冲阶段，一开始的阈值是最大QPS阈值的1/3 ，然后慢慢增长，直到最大阈值，适用于将突然增大的流量转换为缓步增长的场景
    + 排队等待：让请求以均匀的速度通过，单机阈值为每秒通过数量，其余的排队等待.它还会让设置一个超时时间，当请求超过超时间时间还未处理，则会被丢弃。

资料参考:

[sentinel流控设置--关联限流](https://blog.csdn.net/qq_41813208/article/details/107003787)  
[Sentinel限流规则-流控模式之链路模式](https://www.cnblogs.com/linjiqin/p/15369091.html)

### 流控模式实战

1. 将OrderService改为如下
```java
public interface OrderService {

    void save(String msg);

    /**
     * sentinel-资源测试方法
     */
    String sentinelResourceTest();

    /**
     * sentinel-链路测试方法
     * @return
     */
    String sentinelLinkTest();
}
```

2. 将OrderServiceImpl改为如下
```java
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Override
    public void save(String msg) {
        log.info("保存订单");
    }

    @SentinelResource(value = "sentinelResourceTest", blockHandler = "tipHandler")
    @Override
    public String sentinelResourceTest() {
        String msg = "sentinel-resource-test";
        log.info(msg);
        return msg;
    }

    @SentinelResource(value = "sentinelLinkTest", blockHandler = "tipHandler")
    @Override
    public String sentinelLinkTest() {
        String msg = "sentinel-link-test";
        log.info(msg);
        return msg;
    }

    /**blockHandler 函数会在原方法被限流/降级/系统保护的时候调用*****/
    public static String tipHandler(BlockException be) {
        String msg = "您访问的太频繁了，请稍后再试！";
        log.warn(msg);
        return msg;
    }

}
```

3. 将OrderSentinelController修改成如下代码
```java
@RestController
@RequestMapping("sentinel")
public class OrderSentinelController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/direct")
    public String direct() {
        return "直接";
    }

    @GetMapping("/relate1")
    public String relate1() {
        return "关联1";
    }

    @GetMapping("/relate2")
    public String relate2() {
        return "关联2";
    }

    @GetMapping("ResourceTest")
    public String sentinelLinkTest() {
        return "sentinelLinkTest " + orderService.sentinelResourceTest();
    }

    @GetMapping("/link1")
    public String link1() {
        return "链路1" + orderService.sentinelLinkTest();
    }

    @GetMapping("/link2")
    public String link2() {
        return "链路2" + orderService.sentinelLinkTest();
    }
}
```

> 备注：
>
> + [Jmeter性能测试NoHttpResponseException (the target server failed to respond)](https://blog.csdn.net/just_lion/article/details/46923775)
> + [Jmeter不能保存文件处理办法](https://blog.csdn.net/TestGiao/article/details/119530351)

### @SentinelResource 注解实战

> 注意: 该注解非常重要, 在实际使用时, 常用该注解而不是直接使用接口路径作为路径, 以避免限流降级接口返回`Whitelable Error Page`, 在本文档中, 出于方便学习的目的, 多用接口路径直接作讲解

@SentinelResource用于标记特定的资源，在Sentinel对其进行流控, 格式为 @SentinelResource(value = "资源名",blockHandler = "兜底方法名")

+ 资源名作为资源的名字，在Sentinel中与接口路径一样，可作为资源进行流控
+ 兜底方法用于降级后的服务调用

一旦使用@SentinelResource注解，则兜底方法不可或缺，否则流控相应的资源, 会直接抛出相应异常

兜底方法要求：

1. `blockHandler = "兜底方法名"`与`实际方法名`一致
2. 兜底方法必须用`public static`修饰
3. 兜底方法的入参为原方法的入参加上`BlockException be`
4. 兜底方法的返回值必须与原方法一致

代码例子如下

```java
    @SentinelResource(value = "sentinelLinkTest", blockHandler = "tipHandler")
@Override
public String sentinelLinkTest() {
    String msg = "sentinel-link-test";
    log.info(msg);
    return msg;
    }

/**blockHandler 函数会在原方法被限流/降级/系统保护的时候调用*****/
public static String tipHandler(BlockException be) {
    String msg = "您访问的太频繁了，请稍后再试！";
    log.warn(msg);
    return msg;
    }

```

1. 对资源`sentinelLinkTest`新增流控规则
   ![image](https://user-images.githubusercontent.com/37357447/149492164-c27dc29d-3fed-48b3-8a81-2701d9256522.png)

2. jmeter 测试 http://你的ip:8091/sentinel/sentinelResourceTest, 观察控制台如下
   ![image](https://user-images.githubusercontent.com/37357447/149492494-c79629bd-bac0-4528-8e46-b456389e208c.png)

3. jmeter 结果如下

   <img width="958" alt="Screen Shot 2022-12-29 at 15 25 20" src="https://user-images.githubusercontent.com/37357447/209917782-caa59f6e-ff60-480e-806e-f57e2b2b8332.png">

### 流控模式-直接

直接模式的作用是资源达到限流条件时，直接对该资源开启限流

1. 在[Sentinel控制台](http://127.0.0.1:8058/)-簇点链路中，对`/sentinel/direct`资源添加流控规则，为了效果更为明显，将阈值设定为1
   ![image](https://user-images.githubusercontent.com/37357447/149469910-3c61c96e-4c6c-4dfa-b10c-856be391910b.png)
2. 直接访问 http://你的ip:8091/sentinel/direct ，结果如下  
   `Blocked by Sentinel (flow limiting)`

### 流控模式-关联

关联模式的作用是，流控资源2需要监控资源1

1. 添加规则如下，流控资源`/sentinel/relate1`，关联资源`/sentinel/relate2`
   ![image](https://user-images.githubusercontent.com/37357447/149471677-d6bbda43-4b35-4cb5-baf3-89d2c639950d.png)

2. 设置jmter, `relateTest1`对应资源`/sentinel/relate1`, `relateTest1`对应资源`/sentinel/relate2`

3. 将`relateTest2`置为Disable，点击start单独测试`relateTest1`, 可以发现`relateTest1`不受流控规则限制
4. 将`relateTest2`置为Enable，点击start同时测试relateTest1和relateTest2, `relateTest2`不受流控规则限制  
   而`relateTest1`由于`relateTest2`超过了阈值，结果变为  
   `Blocked by Sentinel (flow limiting)`

### 流控模式-链路

链路监控一般用于对`@SentinelResource("xxx")`标记的资源进行流控

1. Sentinel默认会将Controller方法做context整合，导致链路模式的流控失效，需要修改shop-order的application.yml，添加配置：

```java
spring:
    cloud:
    sentinel:
    transport:
    port: 8061 #跟控制台交流的端口 ,随意指定一个未使用的端口即可
    dashboard: localhost:8058 # 指定控制台服务的地址
    web-context-unify: false # 关闭context整合
```

2. 配置链路规则如下
   ![image](https://user-images.githubusercontent.com/37357447/149493320-3bac6d54-9744-4630-ab4e-278cfe983fa5.png)

3. 启动测试, 可以看到`linkTest2`正常, 而受限的链路`linkTest1`进入了兜底函数
   ![image](https://user-images.githubusercontent.com/37357447/149493813-927de642-3066-473c-a56b-9de329ae3ecf.png)

## Sentinel 降级规则

+ 对调用链路中不稳定的资源进行熔断降级也是保障高可用的重要措施之一。
+ 对不稳定的弱依赖服务调用进行熔断降级，暂时切断不稳定调用，避免局部不稳定因素导致整体的雪崩。
+ 熔断降级作为保护自身的手段，通常在客户端（调用端）进行配置。

服务调用方 `shop-order` 的 OrderSentinelController 中添加以下代码

```java
    /*********************** 降级规则测试 ***************************/

/**
 * 慢调用比例(RT)测试
 * @return
 * @throws InterruptedException
 */
@GetMapping("slowRequestRadio")
public String slowRequestRadio() throws InterruptedException {
    double random = Math.random();
    if (random > 0.5) {
    Thread.sleep(2000);
    }
    return "slowRequestRadio";
    }

/**
 * 异常比例测试
 * @return
 */
@GetMapping("errorRadio")
public String errorRadio() {
    double random = Math.random();
    if (random > 0.5) {
    throw new IllegalArgumentException("error");
    }
    return "errorRadio";
    }

/**
 * 异常数测试
 * @return
 */
@GetMapping("errorCount")
public String errorCount() {
    double random = Math.random();
    if (random > 0.5) {
    throw new IllegalArgumentException("error");
    }
    return "errorCount";
    }
```

### 慢调用比例

选择以慢调用比例作为阈值，需要设置允许的`慢调用 RT`，请求的响应时间大于该值则统计为慢调用。

当`统计时长`内请求数目大于设置的`最小请求数目`，并且`慢调用比例`大于阈值，则接下来的`熔断时长`内请求会自动被熔断。

经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求响应时间小于设置的慢调用 RT 则结束熔断，若大于设置的慢调用 RT 则会再次被熔断。

+ 最大RT（即最大的响应时间）: 最大请求响应时间(单位:ms)
+ 比例阈值: 慢调用统计数对于最小请求数的占有比例
+ 熔断时长: 超过时间后会尝试恢复
+ 最小请求数: 触发熔断的最小请求数目，若当前统计窗口内的请求数小于此值，即使达到了熔断条件也不会触发
+ 统计时长（statIntervalMs）: 监控的一个时间段

1. 配置降级规则如下, 这里我们为了效果更加明显, 将各值都调整到较明显的值
   ![image](https://user-images.githubusercontent.com/37357447/149705530-1d255220-8862-4174-a39b-9989785ad6df.png)

2. 使用 jmeter 测试 `/sentinel/slowRequestRadio` 接口, 结果如下
   ![image](https://user-images.githubusercontent.com/37357447/149705594-0ce5cce6-51ec-495f-8f2d-52db16b2d77d.png)

### 异常比例

当单位统计时长（statIntervalMs）内请求数目大于设置的最小请求数目，并且异常的比例大于阈值，则接下来的熔断时长内请求会自动被熔断。经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求成功完成（没有错误）则结束熔断，否则会再次被熔断。

+ `异常比例的阈值范围是 [0.0, 1.0]，代表 0% - 100%。``

1. 配置降级规如下, 操作和结果与[慢调用比例](#慢调用比例)类似, 不再累述
   ![image](https://user-images.githubusercontent.com/37357447/149705730-417036f4-4032-435d-8f17-805844c66202.png)

### 异常数

当单位统计时长内的异常数目超过阈值之后会自动进行熔断。经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求成功完成（没有错误）则结束熔断，否则会再次被熔断。

![image](https://user-images.githubusercontent.com/37357447/149705962-6ce84fc4-c99a-4232-bff3-fe7e759f91ca.png)

## Sentinel 热点规则

何为热点？热点即经常访问的数据。很多时候我们希望统计某个热点数据中访问频次最高的Top K数据，并对其访问进行限制。比如：

+ 商品(shopId)为参数，统计一段时间内最常购买的商品ID并进行限制
+ 用户(userId)为参数，针对一段时间内频繁访问的用户ID进行限制

热点参数限流会统计传入参数中的热点参数，并根据配置的限流阈值与模式，对包含热点参数的资源调用进行限流。热点参数限流可以看做是一种特殊的流量控制，仅对包含热点参数的资源调用生效。

### 热点规则基础

1. 在`OrderSentinelController`中添加代码

```java
    /*********************** 热点规则测试 ***************************/

/**
 * 热点规则测试
 * 热点参数限流对默认的SpringMVC资源无效
 *
 * @return
 */
@GetMapping("paramBlock")
@SentinelResource(value = "paramBlock", blockHandler = "paramBlockHandler")
public String paramBlock(@RequestParam(name = "param", required = false) String param,
@RequestParam(name = "index", required = false) Integer index) {
    return "paramBlock";
    }

public String paramBlockHandler(String param, Integer index, BlockException ex) {
    throw new IllegalArgumentException();
    }
```

2. 配置如图, 我们在这里设置对 param 参数进行限流控制
    + 参数索引: 对第几个参数做限流控制, 类似数组, 从0开始, 参数索引0代表接口中的第1个参数
    + 单机阈值: QPS
    + 统计窗口时长: 单元统计时长

> 注意: 热点参数限流对默认的SpringMVC资源无效(即对接口地址等无效), 仅对`@SentinelResource`标记的资源生效

![image](https://user-images.githubusercontent.com/37357447/149720798-4c178d3c-b3d9-459d-9239-f38369a5223a.png)

3. jmeter 配置如下

![image](https://user-images.githubusercontent.com/37357447/149721041-13d56780-b76d-4a9b-a225-5d3d72e244d9.png)

4. 运行结果, 可以发现当param这个参数存在时, 代码进入了限流兜底方法`paramBlockHandler`

![image](https://user-images.githubusercontent.com/37357447/149721328-1e9db4b6-67de-4a7e-a92b-56d8f415e08f.png)

### 热点规则高级规则

上面讲了热点key的参数限流，第一个参数当QPS超过1秒1次点击后马上被限流，这是普通的案例超过1秒钟一个后，达到阈值1后马上被限流，但是也有参数例外的情况。  
当我们期望第一个参数当它是某个特殊值时，它的限流值和平时不一样，假如当p1的值等于5时，它的阈值可以达到200，这种参数例外的情况，我们就使用到了热点配置的高级属性。

1. 配置如图
    + 参数值: 当参数的值为该值时, 进行限流
    + 注意: 此处的参数类型只能为基本类型和String类型。


![image](https://user-images.githubusercontent.com/37357447/149736479-e28b2f4e-0d07-4d82-9218-75decc707db3.png)

2. jmeter测试, 修改param的值, 可以发现, 当param值为123时, 系统未限流, 而其他值的结果与[热点规则基础](#热点规则基础)一致

## Sentinel 授权规则

很多时候，我们需要根据调用来源来判断该次请求是否允许放行，这时候可以使用Sentinel的来源访问控制的功能。来源访问控制根据资源的请求来源（origin）限制资源是否通过。
简单地说, 授权某个接口给上游某些微服务调用。

+ 授权应用:  授权应用的名称由`RequestOriginParser`确定
+ 资源名: 以`@SentinelResource`定义的资源
+ 授权类型:
    + 若配置白名单，则只有请求来源位于白名单内时才可通过
    + 若配置黑名单，则请求来源位于黑名单时不通过，其余的请求通过

1. 新增`RequestOriginParserDefinition`用于定义授权应用名

```java
@Component
public class RequestOriginParserDefinition implements RequestOriginParser {

    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest) {
        // 指定授权应用名为参数servicename
        String serviceName = httpServletRequest.getParameter("servicename");
        return serviceName;
    }
}
```

2. 授权规则定义如下, 定义应用名为1234的服务, 为黑名单, 禁止访问
   ![image](https://user-images.githubusercontent.com/37357447/149887328-cfefcedd-0efc-42b6-9246-f32493e0b308.png)
3. 访问 http://localhost:8091/sentinel/authRule?servicename=1234, 发现代码进入了兜底函数 `authRuleHandler`

<img width="1063" alt="Screen Shot 2022-12-29 at 17 01 51" src="https://user-images.githubusercontent.com/37357447/209928484-e45a3f13-9079-4df9-96c5-54db2d27d069.png">

## Sentinel 系统规则

### 背景

在开始之前，我们先了解一下系统保护的目的：

+ 保证系统不被拖垮
+ 在系统稳定的前提下，保持系统的吞吐量



长期以来，系统保护的思路是根据硬指标，即系统的负载(Load)来做系统过载保护。当系统负载高于某个阈值，就禁止或者减少流量的进入；当 Load 开始好转，则恢复流量的进入。这个思路给我们带来了不可避免的两个问题：

+ 有延迟。

​	Load 是一个“结果”，如果根据 Load 的情况来调节流量的通过率，那么就始终有延迟性。也就意味着通过率的任何调整，都会过一段时间才能看到效果。当前通过率是使 Load 恶化的一个动作，那么也至少要过1秒之后才能观测到；同理，如果当前通过率调整是让 Load 好转的一个动作，也需要1秒之后才能继续调整，这样就浪费了系统的处理能力。所以我们看到的曲线，总是会有抖动。

+ 恢复慢。

​	想象一下这样的一个真实场景: 出现了这样一个问题，下游应用不可靠，导致应用 RT 很高，从而 Load 到了一个很高的点。过了一段时间之后下游应用恢复了，应用 RT 也相应减少。这个时候，其实应该大幅度增大流量的通过率；但是由于这个时候 Load 仍然很高，通过率的恢复仍然不高。

TCP BBR 的思想给了我们一个很大的启发。我们`应该根据系统能够处理的请求，和允许进来的请求，来做平衡`，而不是根据一个间接的指标（系统 Load）来做限流。 最终我们追求的目标是: 在系统不被拖垮的情况下，提高系统的吞吐率，而不是 Load 一定要到低于某个阈值。如果我们还是按照固有的思维，超过特定的 Load 就禁止流量进入，系统 Load 恢复就放开流量，这样做的结果是无论我们怎么调参数，调比例，都是按照果来调节因，都无法取得良好的效果。



Sentinel 在系统自适应保护的做法是，用 `Load` 作为启动自适应保护的因子，而允许通过的流量由`处理请求的能力`(即请求的响应时间 & 当前系统正在处理的请求速率)来决定。

### 系统规则概述

系统保护规则是从应用级别的入口流量进行控制，从单台机器的`总体Load`、`RT`、`入口QPS`、`CPU使用率`和`线程数`五个维度监控应用数据，让系统尽可能跑在最大吞吐量的同时保证系统整体的稳定性。

+ Load(仅对 Linux/Unix-like 机器生效)：当系统Load1超过阈值，且系统当前的并发线程数超过系统容量时才会触发系统保护。
  系统容量由系统的 maxQps * minRt 计算得出。设定参考值一般是CPU cores * 2.5。
+ 平均RT：当单台机器上所有入口流量的平均RT达到阈值即触发系统保护，单位是毫秒。
+ 线程数：当单台机器上所有入口流量的并发线程数达到阈值即触发系统保护。
+ 入口 QPS：当单台机器上所有入口流量的QPS达到阈值即触发系统保护。
+ CPU使用率：当单台机器上所有入口流量的CPU使用率达到阈值即触发系统保护。

系统保护规则是`应用整体维度`的，而不是资源维度的，并且仅对入口流量(进入应用的流量)生效。

### 系统规则原理

![image](https://user-images.githubusercontent.com/9434884/50813887-bff10300-1352-11e9-9201-437afea60a5a.png)

我们把系统处理请求的过程想象为一个水管，到来的请求是往这个水管灌水，当系统处理顺畅的时候，请求不需要排队，直接从水管中穿过，这个请求的RT是最短的；反之，当请求堆积的时候，那么处理请求的时间则会变为：排队时间 + 最短处理时间。

1. 推论一:
    + 如果我们能够保证水管里的水量，能够让水顺畅的流动，则不会增加排队的请求；也就是说，这个时候的系统负载不会进一步恶化。我们用 T 来表示系统处理容量，用 RT 来表示请求的处理时间，用 P 来表示进来的请求数，那么一个请求从进入水管道到从水管出来，这个水管会需要 P * RT 的容量。换一句话来说，`当 T ≈ QPS * Avg(RT) 的时候，我们可以认为系统的处理能力和允许进入的请求个数达到了平衡，系统的负载不会进一步恶化。`
    + 接下来的问题是，水管的水位是可以达到了一个平衡点，但是这个平衡点只能保证水管的水位不再继续增高，但是还面临一个问题，就是在达到平衡点之前，这个水管里已经堆积了多少水。`如果之前水管的水已经在一个量级了，那么这个时候系统允许通过的水量可能只能缓慢通过，RT会大，之前堆积在水管里的水会滞留；反之，如果之前的水管水位偏低，那么又会浪费了系统的处理能力`。

1. 推论二:
    + 当保持入口的流量是水管出来的流量的最大的值的时候，可以最大利用水管的处理能力。然而，和 TCP BBR 的不一样的地方在于，还需要用一个系统负载的值（load）来激发这套机制启动。

注：这种系统自适应算法对于低 load 的请求，它的效果是一个“兜底”的角色。对于不是应用本身造成的 load 高的情况（如其它进程导致的不稳定的情况），效果不明显。

### 系统规则实战

1. 配置如图
   ![image](https://user-images.githubusercontent.com/37357447/149897700-7c044ebe-7e96-4e2f-b837-b80fe5661c2f.png)
2. 使用jmeter测试任意接口, 当`QPS>10`, 接口提示`Block by Sentinel (flow limiting)`

> 注意: 系统规则实战只是简单的举例, 而实际使用中, 需要考虑各类 load & blance 算法, 在不同的具体环境下, 进行合适的配置

## Sentinel 自定义异常

### 背景

在之前的测试中, 我们发现了以下问题

+ Sentinel一旦发生流控, 默认都会返回`Block by Sentinel (flow limiting)`, 这对我们的实际开发是非常不利的
+ 虽然可以用`blockHandler=xxx`指定兜底函数, 但是有以下问题
    + 仅对`@SentinelResource`标记的资源有用
    + 需要每个方法分别指定, 十分麻烦
    + 不支持系统规则流控

这样, 我们就在想, 有没有一种办法, 能够像java的`全局异常处理`一样, 在某个地方统一处理 Sentinel 流控后的结果, 返回我们自定义的结果?
于是, 我们引入了Sentinel自定义异常`BlockExceptionHandler`

> BlockException的实现类
>
> + FlowException  限流异常
> + DegradeException  降级异常
> + ParamFlowException  参数限流异常
> + AuthorityException  授权异常
> + SystemBlockException  系统负载异常

> 根据[Sentinel的注解支持 - @SentinelResource使用详解](https://juejin.cn/post/6945832676578295838)
>
> @SentinelResource 若未配置 `blockHandler`、`fallback` 和 `defaultFallback`，则被限流降级时会将 `BlockException` **直接抛出**。
>
> 而这个异常不能被 `BlockExceptionHandler` 捕获, 而是由全局异常捕获器捕获

### 实战

1. 新增 Sentinel 异常捕获器 `SentinelExceptionHandler` 如下

``` java
@Component
public class SentinelExceptionHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                       BlockException e) throws Exception {
        // 在这里定义您流控的返回值
        String result = "";
        /**
         * //  FlowException  限流异常
         * //  DegradeException  降级异常
         * //  ParamFlowException  参数限流异常
         * //  AuthorityException  授权异常
         * //  SystemBlockException  系统负载异常
         */
        if (e instanceof FlowException) {
            result = "接口限流了";
        } else if (e instanceof DegradeException) {
            result = "服务降级了";
        } else if (e instanceof ParamFlowException) {
            result = "热点参数限流了";
        } else if (e instanceof AuthorityException) {
            result = "授权规则不通过";
        } else if (e instanceof SystemBlockException) {
            result = "系统规则（负载/...不满足要求）";
        }
        // http状态码
        httpServletResponse.setStatus(500);
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setHeader("Content-Type", "application/json;charset=utf-8");
        httpServletResponse.setContentType("application/json;charset=utf-8");
        // spring mvc自带的json操作工具，jackson
        new ObjectMapper().writeValue(httpServletResponse.getWriter(), result);
    }

}
```

2. 定义全局异常捕获器 `ExceptionHandler`

```java
@RestControllerAdvice
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public String handleException(Exception e){
        log.error(e.getMessage(), e);
        return "异常" + e.getMessage();
    }

}
```

2. 结合之前所学, 定义任意规则, 使用jmeter测试

```java
    /*********************** 自定义规则测试 ***************************/

@GetMapping("exception")
@SentinelResource(value = "exception")
public String exception() throws BlockException {
    return "paramBlock ";
    }

/*********************** 自定义规则测试 ***************************/

@GetMapping("exception2")
public String exception() throws BlockException {
    return "paramBlock ";
    }
```

3. 使用授权规则进行测试

<img width="1039" alt="Screen Shot 2022-12-29 at 17 48 16" src="https://user-images.githubusercontent.com/37357447/209933899-279ecaa9-8c1f-4225-8d71-a7fc6fce9e3d.png">

4. 访问 http://localhost:8091/sentinel/exception?servicename=1234 & http://localhost:8091/sentinel/exception2?servicename=1234

   可以看到 exception 进入了全局异常捕获器 `ExceptionHandler`, 而 exception2 则由 Sentinel 异常捕获器 `SentinelExceptionHandler`捕获

## Sentinel 规则持久化

参考资料:

+ [动态规则扩展](https://github.com/alibaba/Sentinel/wiki/%E5%8A%A8%E6%80%81%E8%A7%84%E5%88%99%E6%89%A9%E5%B1%95)
+ [生产环境使用 Sentinel](https://github.com/alibaba/Sentinel/wiki/%E5%9C%A8%E7%94%9F%E4%BA%A7%E7%8E%AF%E5%A2%83%E4%B8%AD%E4%BD%BF%E7%94%A8-Sentinel)
+ [动态数据拓展](https://github.com/alibaba/Sentinel/wiki/%E5%8A%A8%E6%80%81%E8%A7%84%E5%88%99%E6%89%A9%E5%B1%95)



在前文提过, 虽然我们可以直接通过 Sentinel-Dashboard 来为每个 Sentinel 客户端设置各种各样的规则, 但是这些规则默认是存放在内存中，极不稳定, 而且当微服务重启后, 对应的规则就丢失了, 那么有没有一种办法, 让这些规则持久化呢?

Sentinel 的理念是开发者只需要关注资源的定义，当资源定义成功后可以动态增加各种流控降级规则。Sentinel 提供两种方式修改规则：

- 通过 API 直接修改 (`loadRules`)
- 通过 `DataSource` 适配不同数据源修改

通过 API 直接修改 (`loadRules` 硬编码)一般仅用于测试和演示，生产上一般通过动态规则源的方式(即实现 `DataSource` 接口)来动态管理规则。

alibaba 官方推荐**通过控制台设置规则后将规则推送到统一的规则中心，客户端实现** `ReadableDataSource` **接口端监听规则中心实时获取变更**，流程如下：

![imgag](https://user-images.githubusercontent.com/9434884/45406233-645e8380-b698-11e8-8199-0c917403238f.png)

`DataSource` 扩展常见的实现方式有:

- **拉模式**：客户端主动向某个规则管理中心定期轮询拉取规则，这个规则中心可以是 RDBMS、文件，甚至是 VCS 等。这样做的方式是简单，缺点是无法及时获取变更；
- **推模式**：规则中心统一推送，客户端通过注册监听器的方式时刻监听变化，比如使用 Nacos、Zookeeper 等配置中心。这种方式有更好的实时性和一致性保证。

### API 模式：使用客户端规则 API 配置规则

[Sentinel Dashboard](https://github.com/alibaba/Sentinel/tree/master/sentinel-dashboard) 通过客户端自带的[规则 API](https://github.com/alibaba/Sentinel/wiki/%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8#%E8%A7%84%E5%88%99%E7%9A%84%E7%A7%8D%E7%B1%BB)来实时查询和更改内存中的规则。

注意: 要使客户端具备规则 API，需在客户端引入以下依赖：

```xml
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-transport-simple-http</artifactId>
    <version>x.y.z</version>
</dependency>
```

### 拉模式：使用文件配置规则

从文件获取规则信息。[`FileRefreshableDataSource`](https://github.com/alibaba/Sentinel/blob/master/sentinel-extension/sentinel-datasource-extension/src/main/java/com/alibaba/csp/sentinel/datasource/FileRefreshableDataSource.java) 会周期性的读取文件以获取规则，当文件有更新时会及时发现，并将规则更新到内存中。使用时只需添加以下依赖：

```xml
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-datasource-extension</artifactId>
    <version>x.y.z</version>
</dependency>
```

示例如下:

```java
/**
 * <p>
 * This Demo shows how to use {@link FileRefreshableDataSource} to read {@link Rule}s from file. The
 * {@link FileRefreshableDataSource} will automatically fetches the backend file every 3 seconds, and
 * inform the listener if the file is updated.
 * </p>
 * <p>
 * Each {@link ReadableDataSource} has a {@link SentinelProperty} to hold the deserialized config data.
 * {@link PropertyListener} will listen to the {@link SentinelProperty} instead of the datasource.
 * {@link Converter} is used for telling how to deserialize the data.
 * </p>
 * <p>
 * {@link FlowRuleManager#register2Property(SentinelProperty)},
 * {@link DegradeRuleManager#register2Property(SentinelProperty)},
 * {@link SystemRuleManager#register2Property(SentinelProperty)} could be called for listening the
 * {@link Rule}s change.
 * </p>
 * <p>
 * For other kinds of data source, such as <a href="https://github.com/alibaba/nacos">Nacos</a>,
 * Zookeeper, Git, or even CSV file, We could implement {@link ReadableDataSource} interface to read these
 * configs.
 * </p>
 *
 * @author Carpenter Lee
 * @author Eric Zhao
 */
public class FileDataSourceDemo {

    public static void main(String[] args) throws Exception {
        FileDataSourceDemo demo = new FileDataSourceDemo();
        demo.listenRules();

        /*
         * Start to require tokens, rate will be limited by rule in FlowRule.json
         */
        FlowQpsRunner runner = new FlowQpsRunner();
        runner.simulateTraffic();
        runner.tick();
    }

    private void listenRules() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        String flowRulePath = URLDecoder.decode(classLoader.getResource("FlowRule.json").getFile(), "UTF-8");
        String degradeRulePath = URLDecoder.decode(classLoader.getResource("DegradeRule.json").getFile(), "UTF-8");
        String systemRulePath = URLDecoder.decode(classLoader.getResource("SystemRule.json").getFile(), "UTF-8");

        // Data source for FlowRule
        FileRefreshableDataSource<List<FlowRule>> flowRuleDataSource = new FileRefreshableDataSource<>(
            flowRulePath, flowRuleListParser);
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());

        // Data source for DegradeRule
        FileRefreshableDataSource<List<DegradeRule>> degradeRuleDataSource
            = new FileRefreshableDataSource<>(
            degradeRulePath, degradeRuleListParser);
        DegradeRuleManager.register2Property(degradeRuleDataSource.getProperty());

        // Data source for SystemRule
        FileRefreshableDataSource<List<SystemRule>> systemRuleDataSource
            = new FileRefreshableDataSource<>(
            systemRulePath, systemRuleListParser);
        SystemRuleManager.register2Property(systemRuleDataSource.getProperty());
    }

    private Converter<String, List<FlowRule>> flowRuleListParser = source -> JSON.parseObject(source,
                                                                                              new TypeReference<List<FlowRule>>() {});
    private Converter<String, List<DegradeRule>> degradeRuleListParser = source -> JSON.parseObject(source,
                                                                                                    new TypeReference<List<DegradeRule>>() {});
    private Converter<String, List<SystemRule>> systemRuleListParser = source -> JSON.parseObject(source,
                                                                                                  new TypeReference<List<SystemRule>>() {});
}
```

### 推模式

#### 推模式：使用 Nacos 配置规则

Nacos 是阿里中间件团队开源的服务发现和动态配置中心。Sentinel 针对 Nacos 作了适配，底层可以采用 Nacos 作为规则配置数据源。使用时只需添加以下依赖：

```xml
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-datasource-nacos</artifactId>
    <version>x.y.z</version>
</dependency>
```

然后创建 `NacosDataSource` 并将其注册至对应的 RuleManager 上即可。比如：

```java
// remoteAddress 代表 Nacos 服务端的地址
// groupId 和 dataId 对应 Nacos 中相应配置
ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new NacosDataSource<>(remoteAddress, groupId, dataId,
    source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {}));
    FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
```

注意：如果希望初始化 Nacos 数据源时携带更多的配置（如鉴权配置），可通过带 Properties 的构造函数进行传入。

详细示例可以参见 [sentinel-demo-nacos-datasource](https://github.com/alibaba/Sentinel/tree/master/sentinel-demo/sentinel-demo-nacos-datasource)。

#### 推模式：使用 ZooKeeper 配置规则

Sentinel 针对 ZooKeeper 作了相应适配，底层可以采用 ZooKeeper 作为规则配置数据源。使用时只需添加以下依赖：

```xml
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-datasource-zookeeper</artifactId>
    <version>x.y.z</version>
</dependency>
```

然后创建 `ZookeeperDataSource` 并将其注册至对应的 RuleManager 上即可。比如：

```java
// remoteAddress 代表 ZooKeeper 服务端的地址
// path 对应 ZK 中的数据路径
ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new ZookeeperDataSource<>(remoteAddress, path, source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {}));
    FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
```

详细示例可以参见 [sentinel-demo-zookeeper-datasource](https://github.com/alibaba/Sentinel/tree/master/sentinel-demo/sentinel-demo-zookeeper-datasource)。

#### 推模式：使用 Apollo 配置规则

Sentinel 针对 [Apollo](https://github.com/ctripcorp/apollo) 作了相应适配，底层可以采用 Apollo 作为规则配置数据源。使用时只需添加以下依赖：

```xml
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-datasource-apollo</artifactId>
    <version>x.y.z</version>
</dependency>
```

然后创建 `ApolloDataSource` 并将其注册至对应的 RuleManager 上即可。比如：

```java
// namespaceName 对应 Apollo 的命名空间名称
// ruleKey 对应规则存储的 key
// defaultRules 对应连接不上 Apollo 时的默认规则
ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new ApolloDataSource<>(namespaceName, ruleKey, defaultRules, source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {}));
    FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
```

详细示例可以参见 [sentinel-demo-apollo-datasource](https://github.com/alibaba/Sentinel/tree/master/sentinel-demo/sentinel-demo-apollo-datasource)。

#### 推模式：使用 Redis 配置规则

Sentinel 针对 Redis 作了相应适配，底层可以采用 Redis 作为规则配置数据源。使用时只需添加以下依赖：

```xml
<!-- 仅支持 JDK 1.8+ -->
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-datasource-redis</artifactId>
    <version>x.y.z</version>
</dependency>
```

Redis 动态配置源采用 Redis PUB-SUB 机制实现，详细文档参考：https://github.com/alibaba/Sentinel/tree/master/sentinel-extension/sentinel-datasource-redis

### 规则持久化实战(以 Nacos 为例)

参考资料:

+ [Sentinel使用Nacos存储规则](https://developer.aliyun.com/article/698565)

+ [sentinel和Nacos配合实现配置规则的持久化 ](https://www.cnblogs.com/htyj/p/14049733.html)

Nacos 是阿里中间件团队开源的服务发现和动态配置中心。Sentinel 针对 Nacos 作了适配，底层可以采用 Nacos 作为规则配置数据源。

1. 服务调用方 shop-order 添加以下依赖：

```xml
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-datasource-nacos</artifactId>
    <version>1.8.4</version>
</dependency>
```

2. 服务调用方 shop-order application.yml 改为如下, 从 nacos 读取配置文件

``` java
    sentinel:
      transport:
        port: 8061 #跟控制台交流的端口 ,随意指定一个未使用的端口即可
        dashboard: 127.0.0.1:8858 # 指定控制台服务的地址
      web-context-unify: false # 关闭context整合
      datasource: 
        ds: 
          nacos:
            server-addr: 127.0.0.1:8848
            # nacos中存储规则的groupId
            dataId: test-sentinel
            # nacos中存储规则的dataId
            groupId: DEFAULT_GROUP
            # 定义存储的规则类型，取值见：org.springframework.cloud.alibaba.sentinel.datasource.RuleType
            # 限流: FLOW, 熔断降级: DEGRADE, 系统规则: SYSTEM, 授权规则: AUTHORITY, 热点规则: PARAM_FLOW
            ruleType: AUTHORITY
```

3. 登录 nacos -> configurationManagement -> Configuration -> + add new Rule, 参数可参考 [规则 API](https://github.com/alibaba/Sentinel/wiki/%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8#%E8%A7%84%E5%88%99%E7%9A%84%E7%A7%8D%E7%B1%BB)

    + 流量控制规则 (FlowRule)

      | Field           | 说明                                                            | 默认值                                          |
             | --------------- | --------------------------------------------------------------- | ----------------------------------------------- |
      | resource        | 资源名，资源名是限流规则的作用对象                              |                                                 |
      | count           | 限流阈值                                                        |                                                 |
      | grade           | 限流阈值类型，QPS 模式（1）或并发线程数模式（0）                | QPS 模式, RuleConstant.FLOW_GRADE_QPS           |
      | limitApp        | 流控针对的调用来源                                              | `default`，代表不区分调用来源                   |
      | strategy        | 调用关系限流策略：直接、链路、关联                              | 根据资源本身（直接）RuleConstant.STRATEGY_CHAIN |
      | controlBehavior | 流控效果（直接拒绝/WarmUp/匀速+排队等待），不支持按调用关系限流 | 直接拒绝RuleConstant.CONTROL_BEHAVIOR_DEFAULT   |
      | clusterMode     | 是否集群限流                                                    | 否                                              |

    + 熔断降级规则 (DegradeRule)

      | Field              | 说明                                                                                     | 默认值                                  |
             | ------------------ | ---------------------------------------------------------------------------------------- | --------------------------------------- |
      | resource           | 资源名，即规则的作用对象                                                                 |                                         |
      | grade              | 熔断策略，支持慢调用比例/异常比例/异常数策略                                             | 慢调用比例RuleConstant.DEGRADE_GRADE_RT |
      | count              | 慢调用比例模式下为慢调用临界 RT（超出该值计为慢调用）；异常比例/异常数模式下为对应的阈值 |                                         |
      | timeWindow         | 熔断时长，单位为 s                                                                       |                                         |
      | minRequestAmount   | 熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断（1.7.0 引入）       | 5                                       |
      | statIntervalMs     | 统计时长（单位为 ms），如 60*1000 代表分钟级（1.8.0 引入）                               | 1000 ms                                 |
      | slowRatioThreshold | 慢调用比例阈值，仅慢调用比例模式有效（1.8.0 引入）                                       |                                         |

    + 热点参数规则

      | 属性              | 说明                                                                                                          | 默认值   |
           | ----------------- | ------------------------------------------------------------------------------------------------------------- | -------- |
      | resource          | 资源名，必填                                                                                                  |          |
      | count             | 限流阈值，必填                                                                                                |          |
      | grade             | 限流模式                                                                                                      | QPS 模式 |
      | durationInSec     | 统计窗口时间长度（单位为秒），1.6.0 版本开始支持                                                              | 1s       |
      | controlBehavior   | 流控效果（支持快速失败和匀速排队模式），1.6.0 版本开始支持                                                    | 快速失败 |
      | maxQueueingTimeMs | 最大排队等待时长（仅在匀速排队模式生效），1.6.0 版本开始支持                                                  | 0ms      |
      | paramIdx          | 热点参数的索引，必填，对应 `SphU.entry(xxx, args)` 中的参数索引位置                                           |          |
      | paramFlowItemList | 参数例外项，可以针对指定的参数值单独设置限流阈值，不受前面 `count` 阈值的限制。**仅支持基本类型和字符串类型** |          |
      | clusterMode       | 是否是集群参数流控规则                                                                                        | `false`  |
      | clusterConfig     | 集群流控相关配置                                                                                              |          |

    + 系统保护规则 (SystemRule)

      | Field             | 说明                                   | 默认值      |
           | ----------------- | -------------------------------------- | ----------- |
      | highestSystemLoad | `load1` 触发值，用于触发自适应控制阶段 | -1 (不生效) |
      | avgRt             | 所有入口流量的平均响应时间             | -1 (不生效) |
      | maxThread         | 入口流量的最大并发数                   | -1 (不生效) |
      | qps               | 所有入口资源的 QPS                     | -1 (不生效) |
      | highestCpuUsage   | 当前系统的 CPU 使用率（0.0-1.0）       | -1 (不生效) |

    + 访问控制规则 (AuthorityRule)

        - `resource`：资源名，即规则的作用对象

        - `limitApp`：对应的黑名单/白名单，不同 origin 用 `,` 分隔，如 `appA,appB`

        - `strategy`：限制模式，Mode: 0 for whitelist; 1 for blacklist.

示例如下图

<img width="1265" alt="Screen Shot 2022-12-30 at 18 38 11" src="https://user-images.githubusercontent.com/37357447/210061311-686dc2aa-7964-48ea-8cc8-f0fb335df1f9.png">

启动项目, sentinel 从 nacos 中拉取规则如图

<img width="1275" alt="Screen Shot 2022-12-30 at 18 39 54" src="https://user-images.githubusercontent.com/37357447/210061413-fa969fdf-c150-44ee-8af4-525337db586d.png">

当然也可以按照[官方文档](https://github.com/alibaba/Sentinel/tree/master/sentinel-demo/sentinel-demo-nacos-datasource/src/main/java/com/alibaba/csp/sentinel/demo/datasource/nacos)所示, 使用硬编码的形式, 进行 Sentinel 规则持久化

## Sentinel整合Feign

当项目中使用了 `OpenFeign` 后，可以很方便的进行远程服务调用，现在有个问题，假如远程服务出现故障了，调不了远程的接口，这边又着急等着返回结果，怎么办呢？

当然是使用 **服务降级** ，本篇就使用 `OpenFeign` 进行远程调用，并结合 `Sentinel` 对出现的异常、故障等问题进行服务降级。

### 使用 Fallback 实现

1. 调用方 `shop-order`  application.yml 添加配置

```java
feign: 
  sentinel:
    enabled: true
```

2. 创建容错类

``` java
@Component
public class ProductServiceFallBack implements ProductService {

    @Override
    public String product(String pid) {
        throw new IllegalArgumentException("服务容错");
    }

}
```

3. 为被容器的接口指定容错类, 当远程服务都出现异常时, 调用本类

```java
//value用于指定调用nacos下哪个微服务
//fallback用于指定容错类
@FeignClient(value = "shop-product", fallback = ProductServiceFallBack.class) // 声明服务提供者的name
public interface ProductService {

    @GetMapping(value = "/product/{pid}")
    String product(@PathVariable("pid") String pid);

}
```

4. 只启动 `shop-order`, 调用 http://localhost:8091/order/prod/1234, 进入容错

> 异常Handler dispatch failed; nested exception is java.lang.AssertionError: java.lang.IllegalArgumentException: 服务容错

### 使用 FallbackFactory 实现

参考资料: https://arnoldgalovics.com/feign-fallback/

>What’s the difference between this and the regular fallback?
>
>With the regular fallback, you can’t access the underlying exception that triggered the fallback however with the fallback factory, you can.

1. Create a new Feign service as follows

```java
@FeignClient(value = "shop-product")
public interface ProductTestService {

    @GetMapping(value = "/product/{pid}")
    String product(@PathVariable("pid") String pid);

}
```

2. We need to implement the `FallbackFactory<T>` interface where T represents the type of the Feign client, in this case `ProductTestService`.

```java
@Component
public class ProductTestFallbackFactory implements FallbackFactory<ProductTestService> {

    @Override
    public ProductTestService create(Throwable cause) {
        return new ProductTestService() {
            @Override
            public String product(String pid) {
                if (cause instanceof InvalidObjectException) {
                    return "Bad Request!!!";
                }
                if (cause instanceof FileNotFoundException) {
                    return "Not Found!!!";
                }
                 throw new IllegalArgumentException(cause);
            }
        };
    }

}
```

3. Add `ProductTestFallbackFactory` as fallbackFactory to ProductTestService, shown as follows

```java
@FeignClient(value = "shop-producttttt", fallbackFactory = ProductTestFallbackFactory.class)
public interface ProductTestService {

    @GetMapping(value = "/product/{pid}")
    String product(@PathVariable("pid") String pid);

}
```

> Warning: use feign.hystrix.FallbackFactory, otherwise the terminal would throw `"is not assignable to interface feign.hystrix.FallbackFactory"` exception.

3. Create a new Controller as follows

```java
@RestController
@Slf4j
public class OrderFallbackController {

    @Autowired
    private ProductTestService productService;

    @GetMapping("/order/prodTest/{pid}")
    public String order(@PathVariable("pid") String pid) {

        // feign fallback factory
        String product = productService.product(pid);
        log.info("product info:" + JSON.toJSONString(product));
        return product;
        
    }
    
}
```

3. start consumer service `shop-order`, and access http://localhost:8091/order/prodTest/1234. The browser would show the result as below

<img width="823" alt="Screen Shot 2023-01-05 at 11 27 29" src="https://user-images.githubusercontent.com/37357447/210695330-06d9e713-931e-4a26-821c-56d654ab25e2.png">



