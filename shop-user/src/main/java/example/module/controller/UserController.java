package example.module.controller;

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
public class UserController {

    @GetMapping("/user/{pid}")
    public String product(@PathVariable("pid") String pid) {
        // 向服务消费者返回用户信息
        String msg = "用户" + pid;
        return msg;
    }

}
