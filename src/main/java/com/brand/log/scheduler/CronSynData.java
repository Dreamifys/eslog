package com.brand.log.scheduler;
import com.brand.log.util.DateFormatV1;
import com.brand.log.util.RedisUtil;
import com.brand.log.util.RedissonManager;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class CronSynData {

    @Autowired
    RedissonManager redissonManager;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    DateFormatV1 dateFormatV1;

    private String lokFlag = ".handleKernel";

    private Redisson redisson = null;

    /*
    * java定时脚本挂靠实例
    * 多实例会有重复调用问题 + 使用Redisson实现分布式锁
    * 业务逻辑必须加锁 + 且需要保证 tryLock 等待时间小于cron的最小间隔执行时间
    * */
    @Scheduled(cron = "*/10 * * * * *")
    public void handleKernel() {
        redisson = redissonManager.getRedisson();
        if (redisson != null) {
            RLock lock = redisson.getLock(this.getClass().getName() + lokFlag);
            Boolean stat = false;
            try {
                // 尝试加锁，立即返回，最多等待5s自动解锁
                stat = lock.tryLock(0, 5, TimeUnit.SECONDS);
                if (stat) {
                    log.info("{} 取锁成功！{}",this.getClass().getName(), Thread.currentThread().getName());
                    redisUtil.checkCount("log:limit_", dateFormatV1.getDate("HH", "GMT+8"), 60*10, 1000);
                } else {
                    log.info("{}没有获取到锁:{}", this.getClass().getName(), Thread.currentThread().getName());
                }
            } catch (InterruptedException e) {
                log.error("Redisson 获取分布式锁异常", e);
                if (!stat){
                    return;
                }
                lock.unlock();
            }
        }

    }

}
