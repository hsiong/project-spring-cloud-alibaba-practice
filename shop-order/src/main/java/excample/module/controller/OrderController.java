package excample.module.controller;

import com.alibaba.fastjson.JSON;
import excample.module.service.OrderService;
import excample.module.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    //准备买1件商品
    @GetMapping("/order/prod/{pid}")
    public String order(@PathVariable("pid") String pid) {

        log.info(">>客户下单，这时候要调用商品微服务查询商品信息");

        // 通过feign调用
        String product = productService.product(pid);
        log.info(">>商品信息,查询结果:" + JSON.toJSONString(product));
        orderService.save(product);
        return product;
    }


}
