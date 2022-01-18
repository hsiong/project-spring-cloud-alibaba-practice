package excample.exception;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author JF
 * @version 1.0.0
 * @desc 〈〉
 * @date 2022/1/18 4:07 下午
 */
@Component
public class SentinelExceptionHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                       BlockException e) throws Exception {
        // 在这里定义您流控的返回值
        String result = "";
        /**
         * //  FlowException  限流异常
         * //  DegradeException  降级异常
         * //  ParamFlowException  参数限流异常
         * //  AuthorityException  授权异常
         * //  SystemBlockException  系统负载异常
         */
        if (e instanceof FlowException) {
            result = "接口限流了";
        } else if (e instanceof DegradeException) {
            result = "服务降级了";
        } else if (e instanceof ParamFlowException) {
            result = "热点参数限流了";
        } else if (e instanceof AuthorityException) {
            result = "授权规则不通过";
        } else if (e instanceof SystemBlockException) {
            result = "系统规则（负载/...不满足要求）";
        }
        // http状态码
        httpServletResponse.setStatus(500);
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setHeader("Content-Type", "application/json;charset=utf-8");
        httpServletResponse.setContentType("application/json;charset=utf-8");
        // spring mvc自带的json操作工具，叫jackson
        new ObjectMapper().writeValue(httpServletResponse.getWriter(), result);
    }

}
