package com.qiang.practice.timer;

import com.qiang.practice.service.UserOrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: CLQ
 * @Date: 2019/8/26
 * @Description: TODO
 */
@Component
public class TimerManager {
    private Logger logger = LogManager.getLogger(TimerManager.class);
    @Autowired
    private UserOrderService userOrderService;

    /**
     * 由于没有做物流，每隔10秒执行一次，修改订单状态
     */
    //@Scheduled(cron = "0/10 * * * * ?") //已测试通过，暂时关闭定时
    private void changeOrderStatus() {
        logger.info("-----------修改订单状态开始-----------");
        //发货
        userOrderService.changeOrderStatus();
        logger.info("-----------修改订单状态结束-----------");
    }
}
