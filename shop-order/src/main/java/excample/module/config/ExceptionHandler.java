package excample.module.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 〈全局异常捕获器〉
 *
 * @author Hsiong
 * @version 1.0.0
 * @since 2022/12/29
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public String handleException(Exception e){
        log.error(e.getMessage(), e);
        return "异常" + e.getMessage();
    }

}
