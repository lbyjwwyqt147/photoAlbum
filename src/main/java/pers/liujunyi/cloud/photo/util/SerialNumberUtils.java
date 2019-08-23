package pers.liujunyi.cloud.photo.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pers.liujunyi.cloud.common.redis.RedisTemplateUtils;
import pers.liujunyi.cloud.common.util.DateTimeUtils;

/***
 *
 * 编号 工具类
 * @author Administrator
 */
@Log4j2
@Component
public class SerialNumberUtils {

    @Autowired
    private  RedisTemplateUtils redisTemplateUtils;

    /**
     * 清理 redis 中的 历史顾客编号
     * 1月1日早上1点
     */
    @Scheduled(cron = "0 1 1 1 *")
    public void clearCustomerNumber(){
        log.info("开始 清理 redis 中的 历史顾客编号 start .......................... ");
        redisTemplateUtils.del(RedisKeys.CUSTOMER_NUMBER + DateTimeUtils.getCurrentBeforeYear(1));
    }

    /**
     * 获取当前顾客编号
     * 规则：当前年份后2位 加上 6位数字 数字依次递加
     * @return 19000001
     */
    public  String getCurrentCustomerNumber() {
        //获取当前年份
        int currentYear = DateTimeUtils.getCurrentYear();
        String currentKey = RedisKeys.CUSTOMER_NUMBER + currentYear;
        Object result = redisTemplateUtils.get(currentKey);
        if (result != null) {
            long currentNumber = redisTemplateUtils.incr(currentKey);
            return String.valueOf(currentNumber);
        } else {
            String prefixion = String.valueOf(currentYear - 2000);
            String currentNumber = prefixion + "000001";
            redisTemplateUtils.set(currentKey, Long.valueOf(currentNumber));
            return currentNumber;
        }
    }
}
