package excample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

}
