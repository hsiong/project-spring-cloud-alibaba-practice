package excample.config;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author JF
 * @version 1.0.0
 * @desc 〈〉
 * @date 2022/1/18 2:17 下午
 */
@Component
public class RequestOriginParserDefinition implements RequestOriginParser {

    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest) {
        // 指定授权应用名为参数servicename
        String serviceName = httpServletRequest.getParameter("servicename");
        return serviceName;
    }
}
