package cn.org.toolkit.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * @Package com.example.demo.redisson
 * @ClassName RedissonManager
 * @Description redisson 配置管理类
 * @Author Adminstactor
 * @Date 2018/4/25 13:18
 * @Version 1.0.0
 */
public class RedissonManager {
    private static RedissonClient redissonClient;
    private static Config config = new Config();
    private RedissonManager(){}
    /**
     * 初始化Redisson
     * setMasterName
     * addSentinelAddress
     * setConnectTimeout 同任何节点建立连接时的等待超时。时间单位是毫秒。默认：10000
     * setReconnectionTimeout 当与某个节点的连接断开时，等待与其重新建立连接的时间间隔。时间单位是毫秒。默认:3000
     * setTimeout 等待节点回复命令的时间。该时间从命令发送成功时开始计时。默认:3000
     * setRetryAttempts 如果尝试达到 retryAttempts（命令失败重试次数） 仍然不能将命令发送至某个指定的节点时，
     * 将抛出错误。如果尝试在此限制之内发送成功，则开始启用 timeout（命令等待超时） 计时。默认值：3
     * setRetryInterval 在一条命令发送失败以后，等待重试发送的时间间隔。时间单位是毫秒。     默认值：1500
     */
    public static void init() {
        try {
            config.useSingleServer()
                    .setClientName("")
                    .setConnectTimeout(30000)
                    .setTimeout(10000)
                    .setRetryAttempts(5)
                    .setRetryInterval(3000)
                    .setAddress("redis://127.0.0.1:6379");
                    //.setPassword("linlin");
            redissonClient = Redisson.create(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取Redisson的实例对象
     * @return
     */
    public static Redisson getRedisson(){
        init();
        return (Redisson) redissonClient;
    }

    /**
     * 关闭Redisson
     */
    public static void closed(){
        redissonClient.shutdown();
    }
}
