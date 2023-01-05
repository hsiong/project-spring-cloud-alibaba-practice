package excample.module.controller;

import com.alibaba.fastjson.JSON;
import excample.module.service.ProductTestService;
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
