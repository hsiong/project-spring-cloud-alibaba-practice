package excample.module.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import excample.module.service.OrderService;
import excample.module.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author JF
 * @version 1.0.0
 * @desc 〈〉
 * @date 2022/1/13 4:14 下午
 */
@RestController
@RequestMapping("/sentinel")
public class OrderSentinelController {

    @Autowired
    private OrderService orderService;

    /*********************** 流控模式测试 ***************************/

    @GetMapping("/direct")
    public String direct() {
        return "直接";
    }

    /**
     * 关联模式测试
     * @return
     */
    @GetMapping("/relate1")
    public String relate1() {
        return "关联1";
    }

    /**
     * 关联模式测试
     * @return
     */
    @GetMapping("/relate2")
    public String relate2() {
        return "关联2";
    }

    /**
     * 链路模式测试
     * @return
     */
    @GetMapping("/link1")
    public String link1() {
        orderService.sentinelLinkTest();
        return "链路1";
    }

    /**
     * 链路模式测试
     * @return
     */
    @GetMapping("/link2")
    public String link2() {
        orderService.sentinelLinkTest();
        return "链路2";
    }

    /*********************** 降级规则测试 ***************************/

    /**
     * 慢调用比例(RT)测试
     * @return
     * @throws InterruptedException
     */
    @GetMapping("slowRequestRadio")
    public String slowRequestRadio() throws InterruptedException {
        double random = Math.random();
        if (random > 0.5) {
            Thread.sleep(2000);
        }
        return "slowRequestRadio";
    }

    /**
     * 异常比例测试
     * @return
     */
    @GetMapping("errorRadio")
    public String errorRadio() {
        double random = Math.random();
        if (random > 0.5) {
            throw new IllegalArgumentException("error");
        }
        return "errorRadio";
    }

    /**
     * 异常数测试
     * @return
     */
    @GetMapping("errorCount")
    public String errorCount() {
        double random = Math.random();
        if (random > 0.5) {
            throw new IllegalArgumentException("error");
        }
        return "errorCount";
    }

    /*********************** 热点规则测试 ***************************/

    /**
     * 热点规则测试
     * 热点参数限流对默认的SpringMVC资源无效
     *
     * @return
     */
    @GetMapping("paramBlock")
    @SentinelResource(value = "paramBlock", blockHandler = "paramBlockHandler")
    public String paramBlock(@RequestParam(name = "param", required = false) String param,
                             @RequestParam(name = "index", required = false) Integer index) {
        return "paramBlock";
    }

    public String paramBlockHandler(String param, Integer index, BlockException ex) {
        return "error-paramBlockHandler";
    }




}
