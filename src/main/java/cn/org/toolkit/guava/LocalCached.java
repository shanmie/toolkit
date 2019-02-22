package cn.org.toolkit.guava;

import com.google.common.cache.*;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Package cn.org.guava
 * @ClassName CacheBuilderManager
 * @Description
 * @Author Adminstactor
 * @Date 2018/4/25 14:53
 * @Version 1.0.0
 */
@Slf4j
public class LocalCached {


    /**
     * 缓存刷新周期时间格式
     */
    private static TimeUnit SECONDS = TimeUnit.SECONDS;
    private static TimeUnit MINUTES = TimeUnit.MINUTES;

    private static LoadingCache<Object, Object> cache;

    private static ListeningExecutorService backgroundRefreshPools = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(20));

    /**
     * 设置缓存
     *
     * @return
     */
    static public Cache getCache() {
        return CacheBuilder.newBuilder().recordStats().build();
    }

    /**
     * 设置缓存 最大值
     *
     * @param max
     * @return
     */
    static public Cache getCacheMaxSize(int max) {
        return CacheBuilder.newBuilder().maximumSize(max).recordStats().build();
    }

    /**
     * 设置缓存 过期时间
     *
     * @param max
     * @param time
     * @return
     */
    static public Cache getCacheExpire(int max, int time) {
        return CacheBuilder.newBuilder().maximumSize(max).expireAfterWrite(time, SECONDS)
                .removalListener(rn -> log.info("key {} 被移除", rn.getKey())).recordStats().build();
    }

    /**
     * 设置缓存 过期时间 超过指定时间未使用
     *
     * @param max
     * @param time
     * @return
     */
    static public Cache getCacheExpireAccess(int max, int time) {
        return CacheBuilder.newBuilder().maximumSize(max).expireAfterAccess(time, SECONDS)
                .removalListener(rn -> log.info("key {} 被移除", rn.getKey())).recordStats().build();
    }

    /**
     *              + -------------------------- +
     *              +    以下是另一种方式           +
     *              +    LoadingCache            +
     *              +                            +
     *              + -------------------------- +
     * */

    /**
     * LoadingCache
     *
     * @return
     */
    static public LoadingCache loadingCache() {
        int refreshDuration = 120;//缓存自动刷新周期
        int expireDuration = 10;//缓存过期时间
        int cacheMaxSize = 2;//缓存最大容量
        CacheLoader<Object, Object> cacheLoader = new CacheLoader<Object, Object>() {
            @Override
            public Object load(Object key) {
                log.info("guava loading cached is success");
                return key;
            }

            @Override
            public ListenableFuture<Object> reload(final Object key, Object oldValue) {
                return backgroundRefreshPools.submit(() -> key);
            }
        };
        RemovalListener<Object, Object> rl = rn -> log.info("key {} 被移除", rn.getKey());
        return CacheBuilder.newBuilder()
                .maximumSize(cacheMaxSize)
                .refreshAfterWrite(refreshDuration, SECONDS)
                .expireAfterWrite(expireDuration, MINUTES)
                .weakKeys().removalListener(rl)
                .recordStats().build(cacheLoader);
    }

    /**
     * 从cache中拿出数据的操作
     *
     * @param key
     * @return
     * @throws ExecutionException
     */
    static public Object getValue(Object key) {
        try {
            return getLoadingCache().get(key);
        } catch (ExecutionException e) {
            log.error("loadingCached get value is error", e);
        }
        return null;
    }

    /**
     * 获取cache实例
     *
     * @return
     */
    static private LoadingCache getLoadingCache() {
        if (cache == null) {
            return cache = loadingCache();
        }
        //要不要加锁
        return cache;

    }
}
