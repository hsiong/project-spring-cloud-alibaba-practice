# spring-cloud-alibaba-base
spring cloud alibaba learning

- [spring-cloud-alibaba-base](#spring-cloud-alibaba-base)
- [序言](#序言)
- [第一章 微服务介绍](#第一章-微服务介绍)
  - [1.1 系统架构演变](#11-系统架构演变)
    - [1.1.1 单体应用架构](#111-单体应用架构)
    - [1.1.2 垂直应用架构](#112-垂直应用架构)
    - [1.1.3 分布式架构](#113-分布式架构)
    - [1.1.5 微服务架构](#115-微服务架构)
  - [1.2 微服务架构介绍](#12-微服务架构介绍)
    - [1.2.1 引入](#121-引入)
    - [1.2.2 常用概念](#122-常用概念)
      - [1.2.2.1 服务治理](#1221-服务治理)
      - [1.2.2.2 服务调用](#1222-服务调用)
      - [1.2.2.3 服务网关](#1223-服务网关)
      - [1.2.2.4 服务容错](#1224-服务容错)
      - [1.2.2.5 链路追踪](#1225-链路追踪)
    - [1.2.3 微服务架构的常见解决方案](#123-微服务架构的常见解决方案)
  - [1.3 SpringCloud Alibaba介绍](#13-springcloud-alibaba介绍)
    - [1.3.1 主要功能](#131-主要功能)
    - [1.3.2 组件](#132-组件)
- [第二章 微服务环境搭建](#第二章-微服务环境搭建)
  - [2.1 案例准备](#21-案例准备)
    - [2.1.1 技术选型](#211-技术选型)
    - [2.1.2 模块设计](#212-模块设计)
    - [2.1.3 微服务调用](#213-微服务调用)
  - [2.2 创建父工程](#22-创建父工程)
  - [2.3 创建公共模块shop-common](#23-创建公共模块shop-common)
  - [2.4 创建微服务](#24-创建微服务)
    - [2.4.1 创建微服务步骤](#241-创建微服务步骤)
    - [2.4.2 创建用户微服务shop-user](#242-创建用户微服务shop-user)
      - [2.4.2.1 创建模块](#2421-创建模块)
      - [2.4.2.2 创建用户接口](#2422-创建用户接口)
    - [2.4.3 创建商品微服务shop-product](#243-创建商品微服务shop-product)
      - [2.4.3.1 创建模块](#2431-创建模块)
      - [2.4.3.2 创建商品接口](#2432-创建商品接口)
    - [2.4.4 创建订单微服务shop-order](#244-创建订单微服务shop-order)
      - [2.4.4.1 创建模块](#2441-创建模块)
      - [2.4.4.2 创建保存订单服务](#2442-创建保存订单服务)
        - [2.4.4.2.1 新建接口OrderService](#24421-新建接口orderservice)
        - [2.4.4.2.2 新建实现类OrderServiceImple](#24422-新建实现类orderserviceimple)
      - [2.4.4.3 创建订单接口](#2443-创建订单接口)
    - [2.4.5 总结](#245-总结)
- [第三章 Nacos Discovery--服务治理](#第三章-nacos-discovery--服务治理)
  - [3.1 服务治理介绍](#31-服务治理介绍)
    - [3.2 常见的注册中心](#32-常见的注册中心)
  - [3.3 Nacos实战](#33-nacos实战)
    - [3.3.1 本地搭建Nacos环境](#331-本地搭建nacos环境)
    - [3.3.2 服务注册与发现](#332-服务注册与发现)
      - [3.3.2.1 添加Nacos的依赖](#3321-添加nacos的依赖)
      - [3.3.2.2 将各微服务注册到Nacos](#3322-将各微服务注册到nacos)
      - [3.3.2.3 在spring-boot中配置Nacos的地址](#3323-在spring-boot中配置nacos的地址)
    - [3.3.2.4 修改微服务调用](#3324-修改微服务调用)
    - [3.3.2.5 启动](#3325-启动)
  - [3.4 实现服务调用的负载均衡](#34-实现服务调用的负载均衡)
    - [3.4.1 什么是负载均衡](#341-什么是负载均衡)
    - [3.4.2 自定义实现负载均衡](#342-自定义实现负载均衡)
    - [3.4.3 基于Ribbon实现负载均衡](#343-基于ribbon实现负载均衡)
      - [3.4.3.1 集成Ribbon](#3431-集成ribbon)
      - [3.4.3.2 Ribbon支持的负载均衡策略](#3432-ribbon支持的负载均衡策略)
  - [3.5 基于Feign实现服务调用](#35-基于feign实现服务调用)
    - [3.5.1 什么是Feign](#351-什么是feign)
    - [3.5.2 Feign的使用](#352-feign的使用)
- [第四章 Sentinel--服务容错](#第四章-sentinel--服务容错)
  - [4.1 高并发带来的问题](#41-高并发带来的问题)
  - [4.2 服务雪崩效应](#42-服务雪崩效应)
  - [4.3 常见服务容错方案](#43-常见服务容错方案)
    - [4.3.1 常见的容错思路](#431-常见的容错思路)
    - [4.3.2 常见的容错组件](#432-常见的容错组件)
  - [4.4 Sentinel入门](#44-sentinel入门)
    - [4.4.1 什么是Sentinel](#441-什么是sentinel)
    - [4.4.2 微服务集成Sentinel](#442-微服务集成sentinel)
  - [4.5 Sentinel的概念和功能](#45-sentinel的概念和功能)
    - [4.5.1 基本概念](#451-基本概念)
    - [4.5.2 重要功能](#452-重要功能)
  - [4.6 Sentinel规则](#46-sentinel规则)
    - [4.6.1 流控规则](#461-流控规则)
      - [4.6.1.1 流控模式实战](#4611-流控模式实战)
      - [4.6.1.2 流控模式-直接](#4612-流控模式-直接)
      - [4.6.1.3 @SentinelResource注解实战](#4613-sentinelresource注解实战)
      - [4.6.1.4 流控模式-关联](#4614-流控模式-关联)
      - [4.6.1.5 流控模式-链路](#4615-流控模式-链路)
    - [4.6.2 降级规则](#462-降级规则)
      - [4.6.2.1 降级规则实战](#4621-降级规则实战)
      - [4.6.2.2 慢调用比例](#4622-慢调用比例)
      - [4.6.2.3 异常比例](#4623-异常比例)
      - [4.6.2.4 异常数](#4624-异常数)
    - [4.6.3 热点规则](#463-热点规则)
    - [4.6.3.1 热点规则基础篇](#4631-热点规则基础篇)
    - [4.6.3.2 热点规则高级规则](#4632-热点规则高级规则)
    - [4.6.4 授权规则](#464-授权规则)
      - [4.6.4.1 授权规则实战](#4641-授权规则实战)
    - [4.6.5 系统规则](#465-系统规则)
      - [4.6.5.1 背景](#4651-背景)
      - [4.6.5.2 系统规则](#4652-系统规则)
      - [4.6.5.3 原理](#4653-原理)
      - [4.6.5.4 系统规则实战](#4654-系统规则实战)
  - [4.7 Sentinel自定义异常](#47-sentinel自定义异常)
    - [4.7.1 引入](#471-引入)
    - [4.7.2 Sentinel自定义异常实战](#472-sentinel自定义异常实战)
  - [4.8 Sentinal规则持久化](#48-sentinal规则持久化)

# 序言
在文章开头, 我们总结一下spring cloud alibaba各组件的功能, 给您一个大致的思路和便于您日后的复习。  
也许这些概念和组件您目前还不能理解, 不过没关系, 随着我们学习的深入, 您会慢慢理解这些概念的意义和这些组件的功能。
|组件|功能|
|:-:|:-:|
|Spring Cloud Alibaba|阿里提供的微服务开发的一站式解决方案|
|Nacos|服务注册中心|
|Ribbon|客户端负载均衡|
|Feign|REST API 客户端|
|Sentinel|服务容错|
|||
|||

# 第一章 微服务介绍
## 1.1 系统架构演变
单体应用架构--->垂直应用架构--->分布式架构--->SOA(资源调度和治理中心)架构--->微服务架构--->Service Mesh(服务网格化)  
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
    + 服务间会有依赖关系，一旦某个环节出错会影响较大(服务雪崩)(因为所有服务都部署在同一个服务层上， 并且具有上下游关系)
    + 服务关心复杂，运维、测试部署困难

### 1.1.5 微服务架构
微服务架构在某种程度上是面向服务的架构SOA继续发展的下一步，它更加强调服务的"彻底拆分"，拆分成更小的服务，每个服务都是一个可以独立运行的项目。  

![image](https://user-images.githubusercontent.com/37357447/148908164-33024b85-cf76-405e-93a7-0cc3f17e385c.png)

+ 优点：
    + 服务原子化拆分，独立打包、部署和升级，保证每个微服务清晰的任务划分，利于扩展微服务之间采用Restful等轻量级http协议相互调用
+ 缺点：
    + 分布式系统开发的技术成本高(容错、分布式事务等)

## 1.2 微服务架构介绍
### 1.2.1 引入
在微服务化后， 伴随<b style='color:red'>服务划分粒度</b>越来越细， 我们也由原先的单体应用， 逐步转变成了多个微服务构成的服务集群， 那么， 势必会出现以下问题， 以及目前主流的解决思路。

|问题|解决思路|
|:-:|:-:|
|如何管理他们?|服务治理 注册中心[服务注册 发现 剔除]|
|他们之间如何通讯?|restful rpc|
|客户端怎么访问他们?|网关|
|一旦出现问题，应该如何自处理?|服务容错|
|一旦出现问题，应该如何排错?|链路追踪|
|伴随新的架构体系，应如何架构设计?|事务驱动 领域驱动|

![image](https://user-images.githubusercontent.com/37357447/148911880-67edd711-a1cb-4a5d-b8e9-546038b48fa0.png)

### 1.2.2 常用概念
#### 1.2.2.1 服务治理
服务治理就是进行服务的自动化管理，其核心是服务的自动注册与发现。  
+ <b style='color:red'>服务注册</b>：服务实例将自身服务信息注册到注册中心。  
+ <b style='color:red'>服务发现</b>：服务实例通过注册中心，获取到注册到其中的服务实例的信息，通过这些信息去请求它们提供的服务。  
+ <b style='color:red'>服务剔除</b>：服务注册中心将出问题的服务自动剔除到可用列表之外，使其不会被调用到。  

![image](https://user-images.githubusercontent.com/37357447/148913750-13ef5a3a-bfaa-4dd3-b824-e39b49f16276.png)

#### 1.2.2.2 服务调用
在微服务架构中，通常存在多个服务之间的远程调用的需求。目前主流的远程调用技术有基于HTTP的<b style='color:red'>RESTFUL接口</b>以及基于TCP的<b style='color:red'>RPC协议</b>。一般来说， RESTFUL接口即可满足我们大多数开发需求， 配置和适用上也更方便、灵活。
+ REST(Representational State Transfer)  
这是一种HTTP调用的格式，更标准，更通用，无论哪种语言都支持http协议
+ RPC(Remote Promote Call)  
一种进程间通信方式。允许像调用本地服务一样调用远程服务。RPC框架的主要目标就是让远程服务调用更简单、透明。  
RPC框架负责屏蔽底层的传输方式、序列化方式和通信细节。开发人员在使用的时候只需要了解谁在什么位置提供了什么样的远程服务接口即可，并不需要关心底层通信细节和调用过程。  

|比较项|RESTFUL|RPC|
|:-:|:-:|:-:|
|通讯协议|HTTP|常用TCP， 亦支持UDP HTTP|
|性能|较低|较高|
|灵活度|高|低|
|应用|微服务|SOA|

#### 1.2.2.3 服务网关
随着微服务的不断增多，不同的微服务一般会有不同的网络地址，而外部客户端可能需要调用多个服务的接口才能完成一个业务需求， 如果让客户端直接与各个微服务通信可能出现：
+ 客户端需要调用不同的url地址，增加难度
+ 在一定的场景下，存在跨域请求的问题                                    
+ 每个微服务都需要进行单独的身份认证  

为了解决这些问题， 我们引入了<b style='color:red'>API网关</b>。  
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
+ <b style='color:red'>分布式任务调度</b>：提供秒级、精准、高可靠、高可用的定时(基于 Cron 表达式)任务调度服务。 同时提供分布式的任务执行模型，如网格任务。网格任务支持海量子任务均匀分配到所有 Worker(schedulerx-client)上执行。  
+ 阿里云对象存储：阿里云提供的海量、安全、低成本、高可靠的云存储服务。支持在任何应用、任 何时间、任何地点存储和访问任意类型的数据。  
+ 阿里云短信服务：覆盖全球的短信服务，友好、高效、智能的互联化通讯能力，帮助企业迅速搭建客户触达通道。

### 1.3.2 组件
+ <b style='color:red'>Nacos</b>：一个更易于构建云原生应用的动态服务发现、配置管理和服务管理平台。
+ <b style='color:red'>Sentinel</b>：把流量作为切入点，从流量控制、熔断降级、系统负载保护等多个维度保护服务的稳定性。
+ <b style='color:red'>Dubbo</b>：Apache Dubbo™ 是一款高性能 Java RPC 框架。
+ <b style='color:red'>Seata</b>：阿里巴巴开源产品，一个易于使用的高性能微服务分布式事务解决方案。
+ <b style='color:red'>RocketMQ</b>：一款开源的分布式消息系统，基于高可用分布式集群技术，提供低延时的、高可靠的消息发布与订阅服务。
+ <b style='color:red'>Alibaba Cloud ACM</b>：一款在分布式架构环境中对应用配置进行集中管理和推送的应用配置中心产品。
+ <b style='color:red'>Alibaba Cloud SchedulerX</b>: 阿里中间件团队开发的一款分布式任务调度产品，提供秒级、精准、高可靠、高可用的定时(基于 Cron 表达式)任务调度服务。
+ Alibaba Cloud OSS: 阿里云对象存储服务(Object Storage Service，简称 OSS)，是阿里云提供的海量、安全、低成本、高可靠的云存储服务。您可以在任何应用、任何时间、任何地点存储和 访问任意类型的数据。
+ Alibaba Cloud SMS: 覆盖全球的短信服务，友好、高效、智能的互联化通讯能力，帮助企业迅速搭建客户触达通道。

# 第二章 微服务环境搭建
以电商项目中的商品、订单、用户为例， 进行学习。
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
|feign|2.2.7.RELEASE|
|nacos|2.0.3|
|sentinel|1.8.1|
|fastjson|任意版本|
|lombok|任意版本|
|MySQL|5.7(任意版本， 8.0以上需要修改配置)|

 <b style='color:red'>注意: </b>若未满足版本对应关系， 将会出现各种问题 [spring cloud alibaba 版本对应](https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E)  
        
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
创建一个maven工程， 名字为spring-cloud-alibaba-base，pom.xml如下

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

<!--        &lt;!&ndash; 本地maven配置问题， parent不会下载， 故引入后下载包后注释 &ndash;&gt;
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
 <b style='color:red'>注意: </b>我们认为您已经掌握了创建和部署spring-boot单体应用的能力， 故此只做简要配置说明 

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
        ConfigurableApplicationContext application = SpringApplication.run(xxApplication.class， args);

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
1. 加入配置文件 pom.xml  
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

### 2.4.2 创建用户微服务shop-user
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
### 2.4.3 创建商品微服务shop-product
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
### 2.4.4 创建订单微服务shop-order
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
        String product = restTemplate.getForObject( "http://localhost:8081/product/" + pid， String.class);
        log.info(">>商品信息，查询结果:" + JSON.toJSONString(product));
        orderService.save(product);
        return product;
    }
}
```

### 2.4.5 总结
至此， 我们就完成了项目的基础搭建， 具体源码可以参考base分支。  
访问[http://localhost:8091/order/prod/12345](http://localhost:8091/order/prod/12345)， 可以看到如下界面。  

![image](https://user-images.githubusercontent.com/37357447/149081493-54bc3c49-8e94-4992-9e97-8dbb719b71f5.png)

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

我们在[2.1.3 微服务调用](#213-微服务调用)的基础上，增加了<b style='color:red'>注册中心</b>，用以说明服务治理体系。  

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

## 3.3 Nacos实战
### 3.3.1 本地搭建Nacos环境
1. 安装nacos
    +[github-nacos-release](https://github.com/alibaba/nacos/releases)
2. 启动nacos
    + 切换到目录```cd /xxx/nacos```
    + 启动nacos```sh startup.sh -m standalone```
3. 访问nacos
    + 打开浏览器输入[http://localhost:8848/nacos](http://localhost:8848/nacos)，即可访问服务，默认密码是nacos/nacos
4. 我们在这里编写了一个简单的脚本，方便以后的nacos启动  
打开终端，输入以下命令  
<b>注意: </b>将'Desktop'换成您的实际位置，'您的目录'换成您的nacos的实际目录， 以后使用`sh nacos-start.sh`命令即可快速启动nacos
```
cd ~/Desktop/
touch nacos-start.sh;
chmod -R 755 nacos-start.sh;  
echo '#!/bin/bash' >> nacos-start.sh;
echo 'cd 您的目录/nacos/bin' >> nacos-start.sh;
echo 'startup.sh -m standalone' >> nacos-start.sh;
echo 'echo 'nacos http://127.0.0.1:8848/nacos'' >> nacos-start.sh;
```

  
### 3.3.2 服务注册与发现
我们在第二章的基础上，将Nacos融入我们的微服务中
#### 3.3.2.1 添加Nacos的依赖
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
#### 3.3.2.2 将各微服务注册到Nacos
在各微服务的启动类xxApplication上添加注解
```
@EnableDiscoveryClient // 将该服务注册到nacos
```
#### 3.3.2.3 在spring-boot中配置Nacos的地址
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
        String product = restTemplate.getForObject("http://" + url + "/product/" + pid，String.class);
        log.info(">>商品信息，查询结果:" + JSON.toJSONString(product));
        orderService.save(product);
        return product;
    }
```

### 3.3.2.5 启动
启动各微服务，进入[nacos控制台](http://localhost:8848/nacos/#/serviceManagement?dataId=&group=&appName=&namespace=) 服务管理->服务列表，观察对应的微服务是否注册到了nacos中

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

1. 通过idea再启动一个shop-product微服务，设置其端口为8082(新增spring-boot启动配置，再配置启动端口)[spring-boot如何指定启动端口](https://www.jb51.net/article/175636.htm)

    ![image](https://user-images.githubusercontent.com/37357447/149077803-89becd07-c5ac-47fa-b123-99f37b591bdb.png)
2. 通过nacos查看微服务的启动情况，可以看到微服务shop-product的实例数变为了2  

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
### 3.4.3 基于Ribbon实现负载均衡
#### 3.4.3.1 集成Ribbon
Ribbon是Spring Cloud的一个组件，它可以让我们使用一个注解就能轻松的搞定负载均衡。  
1. 在服务调用(shop-order)的RestTemplate的生成方法上添加@LoadBalanced注解
```
    @Bean
    @LoadBalanced // 如果RestTemplate上面有这个注解，那么这个RestTemplate调用的远程地址，会走负载均衡器。
    public RestTemplate restTemplate() {
    return new RestTemplate();
    }
```
2. 修改服务调用的方法OrderController
```
    @RestController
    @Slf4j
    public class OrderController {

        @Autowired
        private RestTemplate restTemplate;

        @Autowired
        private OrderService orderService;

        //准备买1件商品
        @GetMapping("/order/prod/{pid}")
        public String order(@PathVariable("pid") Integer pid) {

            log.info(">>客户下单，这时候要调用商品微服务查询商品信息");

            // 通过负载随机从nacos中获取服务地址
            String url = "shop-product";

            // 通过restTemplate调用商品微服务
            // 由于restTemplate已经集成@LoadBalanced，那么会自动从注册中心拿到对应的地址
            String product = restTemplate.getForObject("http://" + url + "/product/" + pid，String.class);
            log.info(">>商品信息，查询结果:" + JSON.toJSONString(product));
            orderService.save(product);
            return product;
        }
    }
``` 

#### 3.4.3.2 Ribbon支持的负载均衡策略
Ribbon内置了多种负载均衡策略，内部负载均衡的顶级接口为com.netflix.Loadbalancer.IRule，具体的负载策略如下图所示:

![image](https://user-images.githubusercontent.com/37357447/149099393-949b8855-c90b-43dd-aa77-6e1545f7bcb4.png)

|策略名|策略描述|实现说明|
|:-:|:-:|:-:|
|RetryRule|对选定的负载均衡策略加上重试机制|[负载均衡策略之ROUNDROBINRULE和RETRYRULE源码解读](https://www.freesion.com/article/9980145836/)|
|RoundRobinRule|轮询规则: 每次都取下一个服务器|轮询index，选择index对应位置的server|
|WeightedResponseTimeRule|加权规则: 根据相应时间分配一个weight，server的响应时间越长，weight越小，被选中的可能性越低|开始的时候还没有权重列表，采用父类的轮询方式，有一个默认每30秒更新一次权重列表的定时任务，该定时任务会根据实例的响应时间来更新权重列表，choose方法做的事情就是，用一个(0，1)的随机double数乘以最大的权重得到randomWeight，然后遍历权重列表，找出第一个比randomWeight大的实例下标，然后返回该实例[负载均衡策略之WEIGHTEDRESPONSETIMERULE源码解读](https://www.freesion.com/article/3613148138/)|
|AvailabilityFilteringRule|先过滤出故障的或并发请求大于阈值的服务实例，再以线性轮询的方式从过滤后的实例清单中选出一个|[RIBBON过滤器AVAILABILITYFILTERINGRULE源码解读](https://www.freesion.com/article/1707151963/)|
|BestAvailableRule|选择一个最小的并发请求的server|[RIBBON源码之BESTAVAILABLERULE解读](https://www.freesion.com/article/4064150697/)|
|RandomRule|随机规则: 随机选择一个server|随机选择一个数作为index，选择index对应位置的server|
|ZoneAvoidanceRule|Ribbon默认规则: 先使用主过滤条件(区域负载器，选择最优区域)对所有实例过滤并返回过滤后的实例清单，依次使用次过滤条件列表中的过滤条件对主过滤条件的结果进行过滤，判断最小过滤数(默认1)和最小过滤百分比(默认0)，最后对满足条件的服务器则使用RoundRobinRule(轮询方式)选择一个服务器实例|[RIBBON过滤器ZONEAVOIDANCERULE源码解读](https://www.freesion.com/article/5359151961/)|

使用方式: 除了ZoneAvoidanceRule外， 用以下方式将负载均衡策略注册到spring容器即可。  
```
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

## 3.5 基于Feign实现服务调用
### 3.5.1 什么是Feign
Feign是Spring Cloud提供的一个声明式的伪Http客户端，它使得调用远程服务就像调用本地服务一样简单，只需要创建一个接口并添加一个注解即可。  
Nacos很好的兼容了Feign，Feign默认集成了Ribbon，所以在Nacos下使用Fegin默认就实现了负载均衡的效果。
### 3.5.2 Feign的使用
1. 在父模块xx-alibaba-base加入feign的依赖
```
        <!-- feign 版本与spring-alibaba.version一致 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
            <version>${spring-alibaba.version}</version>
        </dependency>
```  
<b>注意：</b>若将feign的依赖单独放在微服务模块中，项目启动时会报 `NoSuchMethodError: com.google.common.collect.Sets$SetView.iterator()Lcom/google/common/collect/UnmodifiableIterator` 异常，猜测是因为feign依赖了ribbon导致

2. 在shop-order启动类上加入@EnableFeignClients注解  
```
@SpringBootApplication
@EnableDiscoveryClient // 将该服务注册到nacos
@EnableFeignClients //开启Fegin
@Slf4j
public class OrderApplication {
```  
3. 创建ProductService， 通过feign调用商品微服务  
```
@FeignClient("shop-product") // 声明服务提供者的name
public interface ProductService {

    @GetMapping(value = "/product/{pid}")
    String product(@PathVariable("pid") String pid);

}
```  
4. 修改OrderController如下  
```
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
我们可以看到， 通过feign我们实现了两个微服务之间类似接口的调用， 而不用再借助restTemplate通过固定的http地址进行访问。  


5. 重启服务消费方order-shop， 访问[http://localhost:8091/order/prod/4444](http://localhost:8091/order/prod/4444)查看效果  

<b>注意: </b>  
1. 若重启过程中， 提示"No Feign Client for LoadBalancing defined.Did you forget to include spring-cloud-starter-Loadbalance"， 请优先修改feign的版本， 使feign的版本与您的spring-cloud-alibaba版本一致。  
请不要用LoadBalancing替换ribbon。在一段时间内， 仅支持轮询策略的Loadbalance还不能替代ribbon。
2. 

# 第四章 Sentinel--服务容错
## 4.1 高并发带来的问题
在微服务架构中，我们将业务拆分成一个个的服务，服务与服务之间可以相互调用，但是由于网络原因或者自身的原因，服务并不能保证服务的100%可用，如果单个服务出现问题，调用这个服务就会出现网络延迟，此时若有大量的网络涌入，会形成任务堆积，最终导致服务瘫痪。  
接下来，我们来模拟一个高并发的场景: 
1. 改造OrderController
```
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
```
server:
  port: 8091
  tomcat:
    threads:
      max: 50 #tomcat的最大并发值修改为50，默认是200
```
3. 使用jmeter进行压力测试  
[Jmeter下载地址](https://jmeter.apache.org/downLoad_jmeter.cgi)  
[Jmeter性能测试的基本操作](https://www.cnblogs.com/color-cc/p/13585144.html)  
    1. 设置线程并发数
![image](https://user-images.githubusercontent.com/37357447/149298447-0a20d5b6-8d81-455f-afa2-ec3fa13f171d.png)
    2. 运行，打开结果树，观察结果
![image](https://user-images.githubusercontent.com/37357447/149298759-98c3c119-2cf1-4ae6-ab8a-38dd8d84dceb.png)

此时会发现， 由于order方法囤积了大量请求， 导致ｍessage方法的访问出现了问题，这就是服务雪崩的雏形。

## 4.2 服务雪崩效应
在分布式系统中，由于网络原因或自身的原因，服务一般无法保证100%可用。如果一个服务出现了问题，调用这个服务就会出现线程阻塞的情况，此时若有大量的请求涌入，就会出现多条线程阻塞等待，进而导致服务瘫痪。  
由于服务与服务之间的依赖性，故障会传播，会对整个微服务系统造成灾难性的严重后果，这就是服务故障的 “雪崩效应”。  
雪崩发生的原因多种多样，有不合理的容量设计，或者是高并发下某一个方法响应变慢，亦或是某台机器的资源耗尽。我们无法完全杜绝雪崩源头的发生，故此，必须做好足够的<b>服务容错</b>，保证在一个服务发生问题，不会影响到其它服务的正常运行。  

![image](https://user-images.githubusercontent.com/37357447/149300154-a1a80124-593f-4cf7-9d58-b2d44130cf18.png)

## 4.3 常见服务容错方案
### 4.3.1 常见的容错思路
常见的容错思路有隔离、超时、限流、熔断、降级这几种，下面分别介绍一下
+ <b>隔离</b>  
它是指将系统按照一定的原则划分为若干个服务模块，各个模块之间相对独立，无强依赖。当有故障发生时，能将问题和影响隔离在某个模块内部，而不扩散风险，不波及其它模块，不影响整体的系统服务。常见的隔离方式有：<u>线程池隔离</u>和<u>信号量隔离</u>。  
![image](https://user-images.githubusercontent.com/37357447/149301353-b7032ee5-2e35-40c5-9741-c6927efed0ba.png)    
+ <b>超时</b>  
在上游服务调用下游服务的时候，设置一个最大响应时间，<u>如果超过这个时间</u>，下游未作出反应，就<u>断开请求</u>，释放掉线程。
+ <b>限流</b>  
限流就是限制系统的输入和输出流量已达到保护系统的目的。为了保证系统的稳固运行,一旦达到的需要限制的阈值,就需要限制流量并采取少量措施以完成<u>限制流量</u>的目的。
+ <b>熔断</b>  
在互联网系统中，当下游服务响应变慢或失败，上游服务为了保护系统整体的可用性，可以暂时切断对下游服务的调用。这种牺牲局部，保全整体的措施就叫做熔断。  
![image](https://user-images.githubusercontent.com/37357447/149302314-85a245e4-47ac-4375-909e-11c83f494e23.png)  
服务熔断一般有三种状态：
    + 熔断关闭状态(Closed)  
服务没有故障时，熔断器所处的状态，对调用方的调用不做任何限制
    + 熔断开启状态(Open)  
后续对该服务接口的调用不再经过网络，直接执行本地的fallback方法
    + 半熔断状态(Half-Open)  
尝试恢复服务调用，允许有限的流量调用该服务，并监控调用成功率。如果成功率达到预期，则说明服务已恢复，进入熔断关闭状态；如果成功率仍旧很低，则重新进入熔断关闭状态。
+ <b>降级</b>  
降级其实就是为服务提供一个备用方案，一旦服务无法正常调用，就使用备用方案。  
![image](https://user-images.githubusercontent.com/37357447/149303212-9dcd0961-cffd-4445-9d5d-365c99bf269b.png)
### 4.3.2 常见的容错组件
|比较项|Sentinel|Hystrix|resilience4j|
|:-:|:-:|:-:|:-:|
|隔离策略|信号量隔离(并发线程数限流)|线程池隔离/信号量隔离|信号量隔离|
|熔断降级策略|基于响应时间、异常比率、异常数|基于异常比率|基于异常比率、响应时间|
|实时统计实现|滑动窗口(LeapArray)|滑动窗口(基于RxJava)|Ring Bit Buffer|
|动态规则配置|支持多种数据源|支持多种数据源|支持少|
|扩展性|丰富|丰富|仅支持接口形式扩展|
|限流|基于QPS，支持基于调用关系的限流|支持少|Rate Limiter|
|流量整形|支持预热模式、匀速器模式、预热排队模式|X|简单的Rate Limiter模式|
|系统自适应保护|√|X|X|
|控制台|开箱即用的控制台，可配置规则、查看秒级监控、机器发现等|简单的监控查看|不提供控制台，可对接其它监控系统|

## 4.4 Sentinel入门
### 4.4.1 什么是Sentinel 
1. Sentinel(分布式系统的流量防卫兵)是阿里开源的一套用于服务容错的综合性解决方案。它以流量为切入点, 从流量控制、熔断降级、系统负载保护等多个维度来保护服务的稳定性。  
2. Sentinel 具有以下特征:
    + 丰富的应用场景：承接了阿里巴巴近10年的双十一大促流量的核心场景, 例如秒杀(即突发流量控制在系统容量可以承受的范围)、消息削峰填谷、集群流量控制、实时熔断下游不可用应用等
    + 完备的实时监控：提供了实时的监控功能。通过控制台可以看到接入应用的单台机器秒级数据, 甚至500台以下规模的集群的汇总运行情况
    + 广泛的开源生态：提供开箱即用的与其它开源框架/库的整合模块, 例如与Spring Cloud、Dubbo、gRPC的整合。只需要引入相应的依赖并进行简单的配置即可快速地接入Sentinel
    + 完善的SPI扩展点：提供简单易用、完善的SPI扩展接口。您可以通过实现扩展接口来快速地定制逻辑。例如定制规则管理、适配动态数据源等
3. Sentinel 分为两个部分:  
    + 核心库(Java 客户端)：不依赖任何框架/库,能够运行于所有Java运行时环境，同时对Dubbo/Spring Cloud等框架也有较好的支持
    + 控制台(Dashboard)：基于Spring Boot开发，打包后可以直接运行，不需要额外的Tomcat等应用容器

### 4.4.2 微服务集成Sentinel

1. 在父模块alibaba-base的pom.xml中加入依赖
```
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
            <version>${spring-alibaba.version}</version>
        </dependency>
```

2. 新增OrderSentinalController
```
@RestController
public class OrderSentinalController {

    @GetMapping("/sentinal/message")
    public String message() {
        return "1";
    }

    @GetMapping("/sentinal/message2")
    public String message2() {
        return "2";
    }
}
```

3. 安装Sentinel控制台
[下载jar包, 版本使用v1.8.1](https://github.com/alibaba/Sentinel/releases)

4. 类似nacos，这里我们依然选择用脚本来启动Sentinel
打开终端，输入以下命令  
<b>注意: </b>将'Desktop'换成您的实际位置，'您的目录'换成您的Sentinel的实际目录， 以后使用`sh sentinel-start.sh`命令即可快速启动Sentinel, 为了避免端口冲突，我们将sentinel的端口设为8060
```
cd ~/Desktop/
touch sentinel-start.sh;
chmod -R 755 sentinel-start.sh;  
echo '#!/bin/bash' >> sentinel-start.sh;
echo 'cd 您的目录' >> sentinel-start.sh;
echo 'java -Dserver.port=8060 -Dcsp.sentinel.dashboard.server=localhost:8060 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.8.1.jar' >> sentinel-start.sh;
echo 'echo 'sentinel http://127.0.0.1:8060'' >> Sentinel-start.sh;
```

5. 修改shop-order的application.yml
```
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
        dashboard: localhost:8060 # 指定控制台服务的地址
```

6. 启动shop-order, 访问localhost:8060进入Sentinel控制台(默认用户名密码是sentinel/sentinel)  
<b>补充：</b>Sentinel的控制台其实就是一个SpringBoot编写的程序。我们需要将我们的微服务程序注册到控制台上,即在微服务中指定控制台的地址, 并且还要开启一个跟控制台传递数据的端口(这里我们使用8061), 控制台也可以通过此端口调用微服务中的监控程序获取微服务的各种信息。
 
 ## 4.5 Sentinel的概念和功能
 ### 4.5.1 基本概念
+ 资源  
资源就是Sentinel要保护的东西。它可以是Java应用程序中的任何内容，可以是一个服务(接口)，也可以是一个方法，甚至可以是一段代码。
+ 规则  
规则就是用来定义如何进行保护资源的，作用在资源之上, 定义以什么样的方式保护资源，主要包括流量控制规则、熔断降级规则以及系统保护规则。

### 4.5.2 重要功能
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

总之，我们需要做的事情，就是在Sentinel的资源上配置各种各样的规则，来实现各种容错的功能。

## 4.6 Sentinel规则
### 4.6.1 流控规则
流量控制，其原理是监控应用流量的QPS(每秒查询率)或并发线程数等指标，当达到指定的阈值时对流量进行控制，以避免被瞬时的流量高峰冲垮，从而保障应用的高可用性。  
<b>注意：</b>  
+ <u>流控规则在Sentinel重启或者微服务重启有可能会被删除，以后我们会有专门的章节([4.8 Sentinal规则持久化](#48-sentinal规则持久化))来介绍流控规则的持久化配置。</u>   
+ <u>Sentinel本身是一个流量监控服务，需要有对应微服务的接口访问，才会在控制台中显示。</u>   
  
点击簇点链路，我们就可以看到访问过的接口地址，然后点击对应的流控按钮，进入流控规则配置页面。新增流控规则界面如下  
![image](https://user-images.githubusercontent.com/37357447/149312642-cb3fb7ee-f212-44dd-87bf-615b9fa5b0a2.png)
+ 资源名：需要限制的请求路径、方法等等
+ 针对来源：指定对哪个微服务进行限流，指default，意思是不区分来源，全部限制  
阈值类型/单机阈值：
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
  
下面，我们来演示各个流控模式的区别，资料参考  
[sentinel流控设置--关联限流](https://blog.csdn.net/qq_41813208/article/details/107003787)  
[Sentinel限流规则-流控模式之链路模式](https://www.cnblogs.com/linjiqin/p/15369091.html)  

#### 4.6.1.1 流控模式实战


1. 将OrderService改为如下
```
public interface OrderService {

    void save(String msg);

    /**
     * sentinel-链路测试方法
     */
    void sentinelLinkTest();
}
```

2. 将OrderServiceImpl改为如下
```
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Override
    public void save(String msg) {
        log.info("保存订单");
    }

    @SentinelResource(value = "sentinelLinkTest", blockHandler = "tipHandler")
    @Override
    public void sentinelLinkTest() {
        log.info("sentinel-link-test");
    }

    /**blockHandler 函数会在原方法被限流/降级/系统保护的时候调用*****/
    public static void tipHandler(BlockException be) {
        log.info("您访问的太频繁了，请稍后再试！");
    }

}
```

3. 将OrderSentinelController修改成如下代码
```
@RestController
public class OrderSentinelController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/sentinel/direct")
    public String direct() {
        return "直接";
    }

    @GetMapping("/sentinel/relate1")
    public String relate1() {
        return "关联1";
    }

    @GetMapping("/sentinel/relate2")
    public String relate2() {
        return "关联2";
    }

    @GetMapping("/sentinel/link1")
    public String link1() {
        orderService.sentinelLinkTest();
        return "链路1";
    }

    @GetMapping("/sentinel/link2")
    public String link2() {
        orderService.sentinelLinkTest();
        return "链路2";
    }
}
```

<b>备注：</b>
[Jmeter性能测试NoHttpResponseException (the target server failed to respond)](https://blog.csdn.net/just_lion/article/details/46923775)

#### 4.6.1.2 流控模式-直接
直接模式的作用是资源达到限流条件时，直接对该资源开启限流
1. 在[Sentinel控制台](http://127.0.0.1:8060/)-簇点链路中，对`/sentinel/direct`资源添加流控规则，为了效果更为明显，将阈值设定为1
![image](https://user-images.githubusercontent.com/37357447/149469910-3c61c96e-4c6c-4dfa-b10c-856be391910b.png)
2. 直接访问 http://你的ip:8091/sentinel/direct ，结果如下  
`Blocked by Sentinel (flow limiting)`

#### 4.6.1.3 @SentinelResource注解实战
> @SentinelResource 注解详解  
> + <b>注意: </b>该注解非常重要, 在实际使用时, 常用该注解而不是直接使用接口路径作为路径, 以避免限流降级接口返回`Whitelable Error Page`, 在本文档中, 出于方便学习的目的, 多用接口路径直接作讲解
> + @SentinelResource用于标记特定的资源，在Sentinel对其进行流控
> + 格式为 @SentinelResource(value = "资源名",blockHandler = "兜底方法名")
>   + 资源名作为资源的名字，在Sentinel中与接口路径一样，可作为资源进行流控
>   + 兜底方法用于降级后的服务调用
> + 一旦使用@SentinelResource注解，则兜底方法不可或缺，否则降级后会抛出`com.alibaba.csp.sentinel.slots.block.flow.FlowException: null`异常
> + 兜底方法要求：
>   1. `blockHandler = "兜底方法名"`与`实际方法名`一致
>   2. 兜底方法必须用`public static`修饰
>   3. 兜底方法的入参为原方法的入参加上`BlockException be`
>   4. 兜底方法的返回值必须与原方法一致  
> 
> 例子如下:
> ```
>  
>    /**
>     * 原方法
>     */
>    @SentinelResource(value = "sentinelLinkTest", blockHandler = "tipHandler")
>    @Override
>    public void sentinelLinkTest() {
>        log.info("sentinel-link-test");
>    }
>
>    /**
>     * 兜底方法
>     */
>    /**blockHandler 函数会在原方法被限流/降级/系统保护的时候调用*****/
>    public static void tipHandler(BlockException be) {
>        log.info("您访问的太频繁了，请稍后再试！");
>    }
>
> ```

1. 对资源`sentinelLinkTest`新增流控规则
![image](https://user-images.githubusercontent.com/37357447/149492164-c27dc29d-3fed-48b3-8a81-2701d9256522.png)

2. 访问 http://192.168.1.你的ip:8091/sentinel/link1, 观察控制台如下
![image](https://user-images.githubusercontent.com/37357447/149492494-c79629bd-bac0-4528-8e46-b456389e208c.png)


#### 4.6.1.4 流控模式-关联
关联模式的作用是，流控资源2需要监控资源1
1. 添加规则如下，流控资源`/sentinel/relate1`，关联资源`/sentinel/relate2`
![image](https://user-images.githubusercontent.com/37357447/149471677-d6bbda43-4b35-4cb5-baf3-89d2c639950d.png)

2. 设置jmter如下, `relateTest1`对应资源`/sentinel/relate1`, `relateTest1`对应资源`/sentinel/relate2`
![image](https://user-images.githubusercontent.com/37357447/149473484-1bbd351f-d8d0-4b7c-ad9e-ea583f0c5938.png)
![image](https://user-images.githubusercontent.com/37357447/149474204-9a312bf4-b31e-4ae0-99e3-a7e9c1b91578.png)

3. 将`relateTest2`置为Disable，点击start单独测试`relateTest1`, 可以发现`relateTest1`不受流控规则限制

4. 将`relateTest2`置为Enable，点击start同时测试relateTest1和relateTest2, `relateTest2`不受流控规则限制  
而<u>`relateTest1`由于`relateTest2`超过了阈值</u>，结果变为  
`Blocked by Sentinel (flow limiting)`
![image](https://user-images.githubusercontent.com/37357447/149474667-4c61e2dc-d196-4d67-a9c2-9c3ac06282dc.png)

#### 4.6.1.5 流控模式-链路
链路监控一般用于对`@SentinelResource("xxx")`标记的资源进行流控
1. Sentinel默认会将Controller方法做context整合，导致链路模式的流控失效，需要修改shop-order的application.yml，添加配置：
```
spring:
  cloud:
    sentinel:
      transport:
        port: 8061 #跟控制台交流的端口 ,随意指定一个未使用的端口即可
        dashboard: localhost:8060 # 指定控制台服务的地址
      web-context-unify: false # 关闭context整合
```

2. 将兜底方法改为如下
```
    /**
     * 兜底方法
     */
    /**blockHandler 函数会在原方法被限流/降级/系统保护的时候调用*****/
    public static void tipHandler(BlockException be) {
        log.info("您访问的太频繁了，请稍后再试！");
        throw new IllegalArgumentException("您访问的太频繁了，请稍后再试！");
    }
```

3. 配置链路规则如下
![image](https://user-images.githubusercontent.com/37357447/149493320-3bac6d54-9744-4630-ab4e-278cfe983fa5.png)

4. 类似关联模式的测试, 我们配置两个jmeter测试类
![image](https://user-images.githubusercontent.com/37357447/149493001-c76de154-80d9-4620-afe3-9842cded69b2.png)

5. 启动测试, 可以看到`linkTest2`正常, 而受限的链路`linkTest1`进入了兜底函数
   ![image](https://user-images.githubusercontent.com/37357447/149493813-927de642-3066-473c-a56b-9de329ae3ecf.png)

### 4.6.2 降级规则
+ 对调用链路中不稳定的资源进行熔断降级也是保障高可用的重要措施之一。  
+ 对不稳定的弱依赖服务调用进行熔断降级，暂时切断不稳定调用，避免局部不稳定因素导致整体的雪崩。  
+ 熔断降级作为保护自身的手段，通常在客户端（调用端）进行配置。  
#### 4.6.2.1 降级规则实战
将OrderSentinelController改为如下
```
@RestController
@RequestMapping("/sentinel")
public class OrderSentinelController {

    @Autowired
    private OrderService orderService;

    /*********************** 流控模式测试 ***************************/

    @GetMapping("/direct")
    public String direct() {
        return "直接";
    }

    /**
     * 关联模式测试
     * @return
     */
    @GetMapping("/relate1")
    public String relate1() {
        return "关联1";
    }

    /**
     * 关联模式测试
     * @return
     */
    @GetMapping("/relate2")
    public String relate2() {
        return "关联2";
    }

    /**
     * 链路模式测试
     * @return
     */
    @GetMapping("/link1")
    public String link1() {
        orderService.sentinelLinkTest();
        return "链路1";
    }

    /**
     * 链路模式测试
     * @return
     */
    @GetMapping("/link2")
    public String link2() {
        orderService.sentinelLinkTest();
        return "链路2";
    }

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

}
```
#### 4.6.2.2 慢调用比例
慢调用比例 (SLOW_REQUEST_RATIO)：选择以慢调用比例作为阈值，需要设置允许的慢调用 RT（即最大的响应时间），请求的响应时间大于该值则统计为慢调用。当统计时长（statIntervalMs）内请求数目大于设置的最小请求数目，并且慢调用的比例大于阈值，则接下来的熔断时长内请求会自动被熔断。经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求响应时间小于设置的慢调用 RT 则结束熔断，若大于设置的慢调用 RT 则会再次被熔断。  
+ 最大RT: 最大请求响应时间(单位:ms)
+ 比例阈值: 慢调用统计数对于最小请求数的占有比例
+ 熔断时长: 超过时间后会尝试恢复
+ 最小请求数: 触发熔断的最小请求数目，若当前统计窗口内的请求数小于此值，即使达到了熔断条件也不会触发
+ 统计时长: 监控的一个时间段
1. 配置降级规则如下, 这里我们为了效果更加明显, 将各值都调整到较明显的值
   ![image](https://user-images.githubusercontent.com/37357447/149705530-1d255220-8862-4174-a39b-9989785ad6df.png)
2. 配置jmeter如下
   ![image](https://user-images.githubusercontent.com/37357447/149705562-ed8e2d00-e951-42b3-8ee0-b0284c42fd0b.png)
3. 点击测试, 结果如下
   ![image](https://user-images.githubusercontent.com/37357447/149705594-0ce5cce6-51ec-495f-8f2d-52db16b2d77d.png)

#### 4.6.2.3 异常比例
异常比例 (ERROR_RATIO)：当单位统计时长（statIntervalMs）内请求数目大于设置的最小请求数目，并且异常的比例大于阈值，则接下来的熔断时长内请求会自动被熔断。经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求成功完成（没有错误）则结束熔断，否则会再次被熔断。异常比率的阈值范围是 [0.0, 1.0]，代表 0% - 100%。
1. 配置降级规如下, 操作和结果与[4.6.2.2 慢调用比例](#4622-慢调用比例)类似, 不再累述
![image](https://user-images.githubusercontent.com/37357447/149705730-417036f4-4032-435d-8f17-805844c66202.png)

#### 4.6.2.4 异常数
异常数 (ERROR_COUNT)：当单位统计时长内的异常数目超过阈值之后会自动进行熔断。经过熔断时长后熔断器会进入探测恢复状态（HALF-OPEN 状态），若接下来的一个请求成功完成（没有错误）则结束熔断，否则会再次被熔断。
![image](https://user-images.githubusercontent.com/37357447/149705962-6ce84fc4-c99a-4232-bff3-fe7e759f91ca.png)

### 4.6.3 热点规则
何为热点？热点即经常访问的数据。很多时候我们希望统计某个热点数据中访问频次最高的Top K数据，并对其访问进行限制。比如：
+ 商品(shopId)为参数，统计一段时间内最常购买的商品ID并进行限制
+ 用户(userId)为参数，针对一段时间内频繁访问的用户ID进行限制
热点参数限流会统计传入参数中的热点参数，并根据配置的限流阈值与模式，对包含热点参数的资源调用进行限流。热点参数限流可以看做是一种特殊的流量控制，仅对包含热点参数的资源调用生效。

### 4.6.3.1 热点规则基础篇
1. 在`OrderSentinelController`中添加代码
```
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
2. 配置如下图
    + 参数索引: 对第几个参数做限流控制, 类似数组, 从0开始, 参数索引0代表接口中的第1个参数
    + 单机阈值: QPS
    + <b>注意: </b>热点参数限流对默认的SpringMVC资源无效, 仅对`@SentinelResource`标记的资源生效

    ![image](https://user-images.githubusercontent.com/37357447/149720798-4c178d3c-b3d9-459d-9239-f38369a5223a.png)

3. jmeter配置如下
   ![image](https://user-images.githubusercontent.com/37357447/149721041-13d56780-b76d-4a9b-a225-5d3d72e244d9.png)

4. 运行结果如下, 可以发现代码进入了限流兜底方法`paramBlockHandler`
   ![image](https://user-images.githubusercontent.com/37357447/149721328-1e9db4b6-67de-4a7e-a92b-56d8f415e08f.png)

### 4.6.3.2 热点规则高级规则
上面讲了热点key的参数限流，第一个参数当QPS超过1秒1次点击后马上被限流，这是普通的案例超过1秒钟一个后，达到阈值1后马上被限流，但是也有参数例外的情况。  
当我们期望第一个参数当它是某个特殊值时，它的限流值和平时不一样，假如当p1的值等于5时，它的阈值可以达到200，这种参数例外的情况，我们就使用到了热点配置的高级属性。

1. 配置如图  
   + 参数值: 当参数的值为该值时, 进行限流
   + <b>注意: </b>此处的参数类型只能为基本类型和String类型。
  
    ![image](https://user-images.githubusercontent.com/37357447/149736479-e28b2f4e-0d07-4d82-9218-75decc707db3.png)


2. jmeter测试, 修改param的值, 可以发现, 当param值为123时, 系统未限流, 而其他值的结果与[4.6.3.1 热点规则基础篇](#4631-热点规则基础篇)一致

### 4.6.4 授权规则
很多时候，我们需要根据调用来源来判断该次请求是否允许放行，这时候可以使用Sentinel的来源访问控制的功能。来源访问控制根据资源的请求来源（origin）限制资源是否通过。
简单地说, 授权某个接口给上游某些微服务调用。
+ 资源名: 以`@SentinelResource`定义的资源
+ 授权类型: 
  + 若配置白名单，则只有请求来源位于白名单内时才可通过
  + 若配置黑名单，则请求来源位于黑名单时不通过，其余的请求通过
+ 授权应用:   
  授权应用的名称由`RequestOriginParser`确定

#### 4.6.4.1 授权规则实战
1. 新增`RequestOriginParserDefinition`用于定义授权应用名
```
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

3. 访问`http://localhost:8091/sentinel/paramBlock?servicename=1234`, 观察效果, 发现代码进入了流控兜底函数`paramBlockHandler`

### 4.6.5 系统规则
#### 4.6.5.1 背景
在开始之前，我们先了解一下系统保护的目的：
+ 保证系统不被拖垮
+ 在系统稳定的前提下，保持系统的吞吐量

长期以来，系统保护的思路是根据硬指标，即系统的负载(Load)来做系统过载保护。当系统负载高于某个阈值，就禁止或者减少流量的进入；当 Load 开始好转，则恢复流量的进入。

这个思路给我们带来了不可避免的两个问题：
1. Load 是一个“结果”，如果根据 Load 的情况来调节流量的通过率，那么就始终有延迟性。也就意味着通过率的任何调整，都会过一段时间才能看到效果。当前通过率是使 Load 恶化的一个动作，那么也至少要过1秒之后才能观测到；同理，如果当前通过率调整是让 Load 好转的一个动作，也需要1秒之后才能继续调整，这样就浪费了系统的处理能力。所以我们看到的曲线，总是会有抖动。
2. 恢复慢。想象一下这样的一个真实场景: 出现了这样一个问题，下游应用不可靠，导致应用 RT 很高，从而 Load 到了一个很高的点。过了一段时间之后下游应用恢复了，应用 RT 也相应减少。这个时候，其实应该大幅度增大流量的通过率；但是由于这个时候 Load 仍然很高，通过率的恢复仍然不高。  

TCP BBR 的思想给了我们一个很大的启发。我们应该根据系统能够处理的请求，和允许进来的请求，来做平衡，而不是根据一个间接的指标（系统 Load）来做限流。  
最终我们追求的目标是: 在系统不被拖垮的情况下，提高系统的吞吐率，而不是 Load 一定要到低于某个阈值。如果我们还是按照固有的思维，超过特定的 Load 就禁止流量进入，系统 Load 恢复就放开流量，这样做的结果是无论我们怎么调参数，调比例，都是按照果来调节因，都无法取得良好的效果。

Sentinel 在系统自适应保护的做法是，用 Load 作为启动自适应保护的因子，而允许通过的流量由处理请求的能力，即请求的响应时间以及当前系统正在处理的请求速率来决定。

#### 4.6.5.2 系统规则
系统保护规则是从应用级别的入口流量进行控制，从单台机器的总体Load、RT、入口QPS、CPU使用率和线程数五个维度监控应用数据，让系统尽可能跑在最大吞吐量的同时保证系统整体的稳定性。  
系统保护规则是应用整体维度的，而不是资源维度的，并且仅对入口流量(进入应用的流量)生效。  
+ Load(仅对 Linux/Unix-like 机器生效)：当系统Load1超过阈值，且系统当前的并发线程数超过系统容量时才会触发系统保护。
  系统容量由系统的 maxQps * minRt 计算得出。设定参考值一般是CPU cores * 2.5。
+ 平均RT：当单台机器上所有入口流量的平均RT达到阈值即触发系统保护，单位是毫秒。
+ 线程数：当单台机器上所有入口流量的并发线程数达到阈值即触发系统保护。
+ 入口 QPS：当单台机器上所有入口流量的QPS达到阈值即触发系统保护。
+ CPU使用率：当单台机器上所有入口流量的CPU使用率达到阈值即触发系统保护。

#### 4.6.5.3 原理

![image](https://user-images.githubusercontent.com/9434884/50813887-bff10300-1352-11e9-9201-437afea60a5a.png)

我们把系统处理请求的过程想象为一个水管，到来的请求是往这个水管灌水，当系统处理顺畅的时候，请求不需要排队，直接从水管中穿过，这个请求的RT是最短的；反之，当请求堆积的时候，那么处理请求的时间则会变为：排队时间 + 最短处理时间。

1. 推论一: 
   + 如果我们能够保证水管里的水量，能够让水顺畅的流动，则不会增加排队的请求；也就是说，这个时候的系统负载不会进一步恶化。我们用 T 来表示(水管内部的水量)，用 RT 来表示请求的处理时间，用 P 来表示进来的请求数，那么一个请求从进入水管道到从水管出来，这个水管会存在 P * RT 个请求。换一句话来说，当 T ≈ QPS * Avg(RT) 的时候，我们可以认为系统的处理能力和允许进入的请求个数达到了平衡，系统的负载不会进一步恶化。
   + 接下来的问题是，水管的水位是可以达到了一个平衡点，但是这个平衡点只能保证水管的水位不再继续增高，但是还面临一个问题，就是在达到平衡点之前，这个水管里已经堆积了多少水。如果之前水管的水已经在一个量级了，那么这个时候系统允许通过的水量可能只能缓慢通过，RT会大，之前堆积在水管里的水会滞留；反之，如果之前的水管水位偏低，那么又会浪费了系统的处理能力。

1. 推论二:
     + 当保持入口的流量是水管出来的流量的最大的值的时候，可以最大利用水管的处理能力。然而，和 TCP BBR 的不一样的地方在于，还需要用一个系统负载的值（load）来激发这套机制启动。

注：这种系统自适应算法对于低 load 的请求，它的效果是一个“兜底”的角色。对于不是应用本身造成的 load 高的情况（如其它进程导致的不稳定的情况），效果不明显。

#### 4.6.5.4 系统规则实战
1. 配置如图
![image](https://user-images.githubusercontent.com/37357447/149897700-7c044ebe-7e96-4e2f-b837-b80fe5661c2f.png)

2. 使用jmeter测试任意接口, 当`QPS>10`, 接口提示`Block by Sentinel (flow limiting)`

## 4.7 Sentinel自定义异常
### 4.7.1 引入
在之前的测试中, 我们发现了以下问题
+ Sentinel一旦发生流控, 默认都会返回`Block by Sentinel (flow limiting)`, 这对我们的实际开发是非常不利的
+ 虽然可以用`blockHandler=xxx`指定兜底函数, 但是有以下问题
    + 仅对`@SentinelResource`标记的资源有用
    + 需要每个方法分别指定, 十分麻烦
    + 不支持系统规则流控

这样, 我们就在想, 有没有一种办法, 能够像java的`全局异常处理`一样, 在某个地方统一处理 Sentinel 流控后的结果, 返回我们自定义的结果?
于是, 我们引入了Sentinel自定义异常`BlockExceptionHandler`

> BlockException的实现类
> + FlowException  限流异常
> + DegradeException  降级异常
> + ParamFlowException  参数限流异常
> + AuthorityException  授权异常
> + SystemBlockException  系统负载异常

### 4.7.2 Sentinel自定义异常实战
1. 新增`SentinelExceptionHandler`如下
```
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
        // spring mvc自带的json操作工具，叫jackson
        new ObjectMapper().writeValue(httpServletResponse.getWriter(), result);
    }

}
```

2. 结合之前所学, 定义任意规则, 使用jmeter测试

## 4.8 Sentinal规则持久化
我们在[4.6 Sentinel规则](#46-sentinel规则)中提过, 虽然我们可以直接通过Sentinel-Dashboard来为每个Sentinel客户端设置各种各样的规则, 但是这些规则默认是存放在内存中，极不稳定, 而且当微服务重启后, 对应的规则就丢失了, 那么有没有一种办法, 让这些规则持久化呢?   
参考资料: 
+ [生产环境使用 Sentinel](https://github.com/alibaba/Sentinel/wiki/%E5%9C%A8%E7%94%9F%E4%BA%A7%E7%8E%AF%E5%A2%83%E4%B8%AD%E4%BD%BF%E7%94%A8-Sentinel)  
+ [动态数据拓展](https://github.com/alibaba/Sentinel/wiki/%E5%8A%A8%E6%80%81%E8%A7%84%E5%88%99%E6%89%A9%E5%B1%95)

### 4.8.1 规则
Sentinel 的理念是开发者只需要关注资源的定义，当资源定义成功后可以动态增加各种流控降级规则。  
Sentinel 提供两种方式修改规则：
1. 通过 API 直接修改(`loadRules`)  
  手动通过 API 修改比较直观，可以通过以下几个 API 修改不同的规则：
```
FlowRuleManager.loadRules(List<FlowRule> rules); // 修改流控规则
DegradeRuleManager.loadRules(List<DegradeRule> rules); // 修改降级规则
```
2. 通过 `DataSource` 适配不同数据源修改
  
手动修改规则（硬编码方式）一般仅用于测试和演示，生产上一般通过动态规则源的方式来动态管理规则。

### 4.8.2 DataSource扩展
上述 `loadRules()` 方法只接受内存态的规则对象，但更多时候规则存储在文件、数据库或者配置中心当中。DataSource 接口给我们提供了对接任意配置源的能力。相比直接通过 API 修改规则，实现 DataSource 接口是更加可靠的做法。

DataSource 扩展常见的实现方式有:
+ 拉模式：客户端主动向某个规则管理中心定期轮询拉取规则，这个规则中心可以是 RDBMS、文件，甚至是 VCS 等。这样做的方式是简单，缺点是无法及时获取变更；
+ 推模式：规则中心统一推送，客户端通过注册监听器的方式时刻监听变化，比如使用 Nacos、Zookeeper 等配置中心。这种方式有更好的实时性和一致性保证。

Sentinel 目前支持以下数据源扩展：
+ 拉模式: 动态文件数据源、Consul, Eureka
+ 推模式: ZooKeeper, Redis, Nacos, Apollo, etcd

> 拉模式拓展  
实现拉模式的数据源最简单的方式是继承 [AutoRefreshDataSource](https://github.com/alibaba/Sentinel/blob/master/sentinel-extension/sentinel-datasource-extension/src/main/java/com/alibaba/csp/sentinel/datasource/AutoRefreshDataSource.java) 抽象类，然后实现readSource()方法，在该方法里从指定数据源读取字符串格式的配置数据。比如[基于文件的数据源](https://github.com/alibaba/Sentinel/blob/master/sentinel-demo/sentinel-demo-dynamic-file-rule/src/main/java/com/alibaba/csp/sentinel/demo/file/rule/FileDataSourceDemo.java)。

![image](https://user-images.githubusercontent.com/37357447/151140012-f8b9d34a-446f-47ab-9be1-440afdfaad62.png)


```
public class FileDataSourceInit implements InitFunc {

    @Override
    public void init() throws Exception {
        String flowRulePath = "xxx";

        ReadableDataSource<String, List<FlowRule>> ds = new FileRefreshableDataSource<>(
            flowRulePath, source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {})
        );
        // 将可读数据源注册至 FlowRuleManager.
        FlowRuleManager.register2Property(ds.getProperty());

        WritableDataSource<List<FlowRule>> wds = new FileWritableDataSource<>(flowRulePath, this::encodeJson);
        // 将可写数据源注册至 transport 模块的 WritableDataSourceRegistry 中.
        // 这样收到控制台推送的规则时，Sentinel 会先更新到内存，然后将规则写入到文件中.
        WritableDataSourceRegistry.registerFlowDataSource(wds);
    }

    private <T> String encodeJson(T t) {
        return JSON.toJSONString(t);
    }
}
```

> 推模式拓展  
生产环境下一般更常用的是 push 模式的数据源。对于 push 模式的数据源,如远程配置中心（ZooKeeper, Nacos, Apollo等等），推送的操作不应由 Sentinel 客户端进行，而应该经控制台统一进行管理，直接进行推送，数据源仅负责获取配置中心推送的配置并更新到本地。因此推送规则正确做法应该是 配置中心控制台/Sentinel 控制台 → 配置中心 → Sentinel 数据源 → Sentinel，而不是经 Sentinel 数据源推送至配置中心。

![image](https://user-images.githubusercontent.com/37357447/151139744-f5178463-43ba-43c4-9e6c-1ed4488973cf.png)


实现推模式的数据源最简单的方式是继承 AbstractDataSource 抽象类，在其构造方法中添加监听器，并实现 readSource() 从指定数据源读取字符串格式的配置数据。比如 基于 Nacos 的数据源。控制台通常需要做一些改造来直接推送应用维度的规则到配置中心。

实际上, 我们有以下方式来实现推模式:
1. 使用 Nacos 配置规则
2. 使用 ZooKeeper 配置规则
3. 使用 Apollo 配置规则
4. 使用 Redis 配置规则

### 4.8.3 推模式拓展实战
