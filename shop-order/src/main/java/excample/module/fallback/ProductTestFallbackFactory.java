package excample.module.fallback;

import excample.module.service.ProductTestService;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.InvalidObjectException;

/**
 * 〈〉
 *
 * @author Hsiong
 * @version 1.0.0
 * @since 2023/1/5
 */
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
