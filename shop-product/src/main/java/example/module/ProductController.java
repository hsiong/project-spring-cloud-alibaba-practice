package example.module;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JF
 * @version 1.0.0
 * @desc 〈〉
 * @date 2022/1/7 4:07 下午
 */
@RestController
@Slf4j
public class ProductController {

    @GetMapping("/product/{pid}")
    public String product(@PathVariable("pid") String pid) {
        String msg = "商品" + pid;
        log.info(msg);
        return msg;
    }

}
