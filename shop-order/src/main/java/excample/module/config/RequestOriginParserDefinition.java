package excample.module.config;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 〈授权规则〉
 *
 * @author Hsiong
 * @version 1.0.0
 * @since 2022/12/29
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
