package excample.module.service;

//import org.springframework.cloud.openfeign.FeignClient;
import excample.module.fallback.ProductTestFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author JF
 * @version 1.0.0
 * @desc 〈〉
 * @Date 2022/1/12 6:10 下午
 */
// value 用于指定调用 nacos 下哪个微服务
// fallback 用于指定容错类, 当远程服务都出现异常时, 调用本类
@FeignClient(value = "shop-product", fallbackFactory = ProductTestFallbackFactory.class) // 声明服务提供者的name
public interface ProductTestService {

    @GetMapping(value = "/product/{pid}")
    String product(@PathVariable("pid") String pid);

}
