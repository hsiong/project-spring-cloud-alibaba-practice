package excample.module.service.imple;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import excample.module.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author JF
 * @version 1.0.0
 * @desc 〈〉
 * @date 2022/1/7 4:52 下午
 */
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
