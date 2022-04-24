package excample.config;

import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.netflix.loadbalancer.BestAvailableRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author JF
 * @version 1.0.0
 * @desc 〈〉
 * @date 2022/1/18 2:49 下午
 */
@Component
public class InitConfigure {

    @Bean
    @LoadBalanced // 如果RestTemplate上面有这个注解，那么这个RestTemplate调用的远程地址，会走负载均衡器。
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    /**
     * 设置Ribbon的策略
     * @return
     */
    @Bean
    public IRule myRule(){
        // 选择一个最小的并发请求的server
        return new BestAvailableRule();
    }

}
