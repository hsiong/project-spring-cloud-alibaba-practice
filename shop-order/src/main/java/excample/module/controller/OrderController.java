package excample.module.controller;

import com.alibaba.fastjson.JSON;
import excample.module.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author JF
 * @version 1.0.0
 * @desc 〈〉
 * @date 2022/1/7 4:56 下午
 */
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

        // 从nacos中获取服务地址
        ServiceInstance serviceInstance = discoveryClient.getInstances("service-product").get(0);
        String url = serviceInstance.getHost() + ":" + serviceInstance.getPort();
        log.info(">>从nacos中获取到的微服务地址为:" + url);


        // 通过restTemplate调用商品微服务
        String product = restTemplate.getForObject("http://" + url + "/product/" + pid, String.class);
        log.info(">>商品信息,查询结果:" + JSON.toJSONString(product));
        orderService.save(product);
        return product;
    }


}
