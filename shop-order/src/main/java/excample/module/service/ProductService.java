package excample.module.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author JF
 * @version 1.0.0
 * @desc 〈〉
 * @Date 2022/1/12 6:10 下午
 */
@FeignClient("shop-product") // 声明服务提供者的name
public interface ProductService {

    @GetMapping(value = "/product/{pid}")
    String product(@PathVariable("pid") String pid);

}
