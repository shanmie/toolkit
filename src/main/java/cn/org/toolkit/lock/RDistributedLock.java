package cn.org.toolkit.lock;

import cn.org.toolkit.redisson.RedissonManager;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * @Package com.example.demo.lock
 * @ClassName RDistributedLock
 * @Description redisson分布式锁类
 * @Author Adminstactor
 * @Date 2018/4/25 13:31
 * @Version 1.0.0
 */
@Slf4j
public class RDistributedLock {
    private static Redisson redisson = RedissonManager.getRedisson();
    private static final String LOCK_FLAG = "redisson_lock_";
    /**
     * 根据name对进行上锁操作，redissonLock 阻塞式的，采用的机制发布/订阅
     * @param lockName
     */
    public static void lock(String lockName,long time){
        String key = LOCK_FLAG + lockName;
        log.info("key={}",key);
        RLock lock = redisson.getLock(key);
        //lock提供带timeout参数，timeout结束强制解锁，防止死锁 ：1分钟
        lock.lock(time, TimeUnit.MINUTES);
    }

    /**
     * 根据name对进行解锁操作
     * @param lockName
     */
    public static void unlock(String lockName){
        String key = LOCK_FLAG + lockName;
        log.info("key={}",key);
        RLock lock = redisson.getLock(key);
        lock.unlock();
    }

}
