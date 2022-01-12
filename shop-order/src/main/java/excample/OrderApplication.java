package excample;

import com.netflix.loadbalancer.BestAvailableRule;
import com.netflix.loadbalancer.IRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author JF
 * @version 1.0.0
 * @desc 〈〉
 * @date 2022/1/7 4:46 下午
 */
@SpringBootApplication
@EnableDiscoveryClient // 将该服务注册到nacos
@EnableFeignClients //开启Fegin
@Slf4j
public class OrderApplication {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(OrderApplication.class, args);

        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostName();
        String port = env.getProperty("server.port");
        String path = "/" + env.getProperty("spring.application.name");
        log.warn("\n----------------------------------------------------------\n\t" +
                 "Application Order-Boot is running! Access URLs:\n\t" +
                 "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
                 "----------------------------------------------------------");

    }

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
