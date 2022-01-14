package excample.module.controller;

import excample.module.service.OrderService;
import excample.module.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JF
 * @version 1.0.0
 * @desc 〈〉
 * @date 2022/1/13 4:14 下午
 */
@RestController
public class OrderSentinelController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/sentinel/direct")
    public String direct() {
        return "直接";
    }

    @GetMapping("/sentinel/relate1")
    public String relate1() {
        return "关联1";
    }

    @GetMapping("/sentinel/relate2")
    public String relate2() {
        return "关联2";
    }

    @GetMapping("/sentinel/link1")
    public String link1() {
        orderService.sentinelLinkTest();
        return "链路1";
    }

    @GetMapping("/sentinel/link2")
    public String link2() {
        orderService.sentinelLinkTest();
        return "链路2";
    }

}
