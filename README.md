
# 第三章 Nacos Discovery--服务治理
## 3.1 服务治理介绍
通过上一章的操作，我们已经可以实现微服务之间的调用。但是我们把服务提供者的网络地址(ip，端口)等硬编码到了代码中，这种做法存在许多问题：
+ 一旦服务提供者地址变化，就需要手工修改代码
+ 一旦是多个服务提供者，无法实现负载均衡功能
+ 一旦服务变得越来越多，人工维护调用关系困难

那么应该怎么解决呢，这时候就需要通过注册中心动态的实现<b style='color:red'>服务治理</b>。
服务治理是微服务架构中最核心最基本的模块。用于实现各个微服务的自动化注册与发现。
+ 服务注册：
在服务治理框架中，都会构建一个注册中心，每个服务单元向注册中心登记自己提供服务的详细信息。并在注册中心形成一张服务的清单，服务注册中心需要以心跳的方式去监测清单中的服务是否可用，如果不可用，需要在服务清单中剔除不可用的服务。
+ 服务发现：  服务调用方向服务注册中心咨询服务，并获取所有服务的实例清单，实现对具体服务实例的访问。

我们在[2.1.3 微服务调用](#213-微服务调用)的基础上， 增加了<b style='color:red'>注册中心</b>， 用以说明服务治理体系。

![image](https://user-images.githubusercontent.com/37357447/149058031-ee424fcd-7849-4b8d-b141-91bddf9fced6.png)

通过上面的调用图会发现，除了微服务，还有一个组件是服务注册中心，它是微服务架构非常重要 的一个组件，在微服务架构里主要起到了协调者的一个作用。注册中心一般包含如下几个功能：
1. 服务发现：
   + 服务注册：保存服务提供者和服务调用者的信息
   + 服务订阅：服务调用者订阅服务提供者的信息，注册中心向订阅者推送提供者的信息

2. 服务配置：
    + 配置订阅：服务提供者和服务调用者订阅微服务相关的配置
    + 配置下发：主动将配置推送给服务提供者和服务调用者
3. 服务健康检测
    + 检测服务提供者的健康情况，如果发现异常，执行服务剔除

### 3.2 常见的注册中心
+ Zookeeper
  zookeeper是一个分布式服务框架，是Apache Hadoop 的一个子项目，它主要是用来解决分布式应用中经常遇到的一些数据管理问题，如：统一命名服务、状态同步服务、集群管理、分布式应用 配置项的管理等。
+ Eureka
  Eureka是Springcloud Netﬂix中的重要组件，主要作用就是做服务注册和发现。但是现在已经闭源。
+ Consul
  Consul是基于GO语言开发的开源工具，主要面向分布式/服务化的系统提供服务注册、服务发现和配置管理的功能。
  Consul的功能都很实用，其中包括：服务注册/发现、健康检查、Key/Value存储、多数据中心和分布式一致性保证等特性。
  Consul本身只是一个二进制的可执行文件，所以安装和部署都非常简单，只需要从官网下载后，在执行对应的启动脚本即可。
+ Nacos
  Nacos是一个更易于构建云原生应用的动态服务发现、配置管理和服务管理平台。它是Spring Cloud Alibaba组件之一，负责服务注册发现和服务配置，可以这样认为nacos=eureka+conﬁg。

## 3.3 nacos实战
### 3.3.1 本地搭建nacos环境
1. 安装nacos
    + [github-nacos-release](https://github.com/alibaba/nacos/releases)
2. 启动nacos
    + 切换到目录 ```cd /xxx/nacos```
    + 启动nacos ```sh startup.sh -m standalone ```
3. 访问nacos
    + 打开浏览器输入[http://localhost:8848/nacos](http://localhost:8848/nacos)， 即可访问服务，默认密码是nacos/nacos

### 3.3.2 服务注册与发现
我们在第二章的基础上， 将nacos融入我们的微服务中
#### 3.3.2.1 添加nacos的依赖
在父模块spring-cloud-alibaba-base的pom.xml中，
1. 在\<properties\>标签中添加属性
```
        <spring-alibaba.version>2.2.7.RELEASE</spring-alibaba.version>
```
1. 在\<dependencies\>标签中添加依赖
```
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
            <version>${spring-alibaba.version}</version>
        </dependency>
```
#### 3.3.2.2 将各微服务注册到nacos
在各微服务的启动类xxApplication上添加注解
```
@EnableDiscoveryClient // 将该服务注册到nacos
```
#### 3.3.2.3 在spring-boot中配置nacos的地址
在各微服务的application.yml中添加nacos服务的地址
```
spring:
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
```
### 3.3.2.4 修改微服务调用
将OrderController改为如下所示
```
@RestController
@Slf4j
public class OrderController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OrderService orderService;

    // DiscoveryClient是专门负责服务注册和发现的，我们可以通过它获取到注册到注册中心的所有服务
    @Autowired
    private DiscoveryClient discoveryClient;

    //准备买1件商品
    @GetMapping("/order/prod/{pid}")
    public String order(@PathVariable("pid") Integer pid) {

        log.info(">>客户下单，这时候要调用商品微服务查询商品信息");

        // 从nacos中获取服务地址(其中为实例名)
        ServiceInstance serviceInstance = discoveryClient.getInstances("shop-product").get(0);
        String url = serviceInstance.getHost() + ":" + serviceInstance.getPort();
        log.info(">>从nacos中获取到的微服务地址为:" + url);


        // 通过restTemplate调用商品微服务
        String product = restTemplate.getForObject("http://" + url + "/product/" + pid， String.class);
        log.info(">>商品信息，查询结果:" + JSON.toJSONString(product));
        orderService.save(product);
        return product;
    }
```

### 3.3.2.5 启动
启动各微服务， 进入[nacos控制台](http://localhost:8848/nacos/#/serviceManagement?dataId=&group=&appName=&namespace=) 服务管理->服务列表， 观察对应的微服务是否注册到了nacos中

![image](https://user-images.githubusercontent.com/37357447/149061577-60d6fa74-033b-4398-8cfe-389deae0ba6c.png)

## 3.4 实现服务调用的负载均衡
### 3.4.1 什么是负载均衡
负载均衡是将负载(工作任务，访问请求)进行分摊到多个操作单元(服务器，组件)上进行执行。
根据负载均衡发生位置的不同，一般分为<b>服务端负载均衡</b>和<b>客户端负载均衡</b>。
+ 服务端负载均衡指的是发生在<u>服务提供者</u>一方，比如常见的nginx负载均衡.
+ 客户端负载均衡指的是发生在<u>服务请求</u>的一方，也就是在发送请求之前已经选好了由哪个实例处理请求。

![image](https://user-images.githubusercontent.com/37357447/149073780-5b2b9477-23c8-420f-85a9-90b99cafe209.png)

我们在微服务调用关系中一般会选择客户端负载均衡，也就是在服务消费者来决定服务由哪个提供者执行。

### 3.4.2 自定义实现负载均衡

1. 通过idea再启动一个shop-product微服务，设置其端口为8082(新增spring-boot启动配置, 再配置启动端口) [spring-boot如何指定启动端口](https://www.jb51.net/article/175636.htm)

    ![image](https://user-images.githubusercontent.com/37357447/149077803-89becd07-c5ac-47fa-b123-99f37b591bdb.png)
2. 通过nacos查看微服务的启动情况, 可以看到微服务shop-product的实例数变为了2

    ![image](https://user-images.githubusercontent.com/37357447/149077903-d87324c4-9c45-4b78-bafe-c565d6a2367c.png)
3. 修改shop-order的代码，实现负载均衡
    将
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
