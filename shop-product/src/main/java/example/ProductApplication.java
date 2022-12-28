package example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author JF
 * @version 1.0.0
 * @desc 〈〉
 * @date 2022/1/7 4:46 下午
 */
@SpringBootApplication
@Slf4j
@EnableDiscoveryClient // 将该服务注册到nacos
public class ProductApplication {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(ProductApplication.class, args);

        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostName();
        String port = env.getProperty("server.port");
        String path = "/" + env.getProperty("spring.application.name");
        log.warn("\n----------------------------------------------------------\n\t" +
                 "Application Product-Boot is running! Access URLs:\n\t" +
                 "Local: \t\thttp://localhost:" + port + "/\n\t" +
                 "----------------------------------------------------------");

    }

}
