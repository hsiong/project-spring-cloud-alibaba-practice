package excample.module.service.imple;

import excample.module.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author JF
 * @version 1.0.0
 * @desc 〈〉
 * @date 2022/1/7 4:52 下午
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Override
    public void save(String msg) {
        log.info("保存订单");
    }

}
