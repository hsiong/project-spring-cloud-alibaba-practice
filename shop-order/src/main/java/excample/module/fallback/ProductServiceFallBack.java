package excample.module.fallback;

import excample.module.service.ProductService;
import org.springframework.stereotype.Component;

/**
 * 〈 ProductService 服务容错 〉
 *
 * @author Hsiong
 * @version 1.0.0
 * @since 2023/1/4
 */
@Component
public class ProductServiceFallBack implements ProductService {

    @Override
    public String product(String pid) {
        throw new IllegalArgumentException("服务容错");
    }

}
