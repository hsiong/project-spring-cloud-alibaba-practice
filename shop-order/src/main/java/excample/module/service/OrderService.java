package excample.module.service;

/**
 * @author JF
 * @version 1.0.0
 * @desc 〈〉
 * @Date 2022/1/7 4:52 下午
 */
public interface OrderService {

    void save(String msg);

    /**
     * sentinel-资源测试方法
     */
    String sentinelResourceTest();

    /**
     * sentinel-链路测试方法
     * @return
     */
    String sentinelLinkTest();

}
