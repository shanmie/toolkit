package cn.org.toolkit.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.Objects;
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
public abstract class CacheBuilderManager<K, V> {

    /**
     * 缓存自动刷新周期
     */
    protected int refreshDuration;

    /**
     * 缓存刷新周期时间格式
     */
    protected TimeUnit refreshTimeUnitType;

    /**
     * 缓存最大容量
     */
    protected int cacheMaximumSize;

    private LoadingCache<K, V> cache;

    private ListeningExecutorService backgroundRefreshPools = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(20));

    /**
     * 设置缓存
     *
     * @return
     */
    public Cache<K, V> loadCache() {
        return (Cache<K, V>) CacheBuilder.newBuilder().recordStats().build();
    }

    /**
     * 设置缓存 最大值
     *
     * @param max
     * @return
     */
    public Cache<K, V> loadCacheMaxmumSize(int max) {
        return (Cache<K, V>) CacheBuilder.newBuilder().maximumSize(max).recordStats().build();
    }

    /**
     * 设置缓存 过期时间
     *
     * @param max
     * @param time
     * @return
     */
    public Cache<K, V> loadCacheExpire(int max, int time) {
        return (Cache<K, V>) CacheBuilder.newBuilder().maximumSize(max).expireAfterWrite(time, TimeUnit.SECONDS).recordStats().build();
    }

    /**
     * 设置缓存 过期时间 超过指定时间未使用
     *
     * @param max
     * @param time
     * @return
     */
    public Cache<K, V> loadCacheExpireAccess(int max, int time) {
        return (Cache<K, V>) CacheBuilder.newBuilder().maximumSize(max).expireAfterAccess(time, TimeUnit.SECONDS).recordStats().build();
    }

    /**
     * 定义缓存值的计算方法 新值计算失败时抛出异常，get操作时将继续返回旧的缓存
     *
     * @param key
     * @return
     * @throws Exception
     */
    protected abstract V getValueWhenExpire(K key) throws Exception;

    /**
     * 局部刷新加载
     *
     * @return
     */
    public LoadingCache loadRefreshAfterWrite() {
        return CacheBuilder.newBuilder().maximumSize(cacheMaximumSize).refreshAfterWrite(refreshDuration, refreshTimeUnitType)
                .recordStats().build(new CacheLoader<K, V>() {
                    @Override
                    public V load(K key) throws Exception {
                        System.out.println(key + " is loaded from a cacheLoader!");
                        return getValueWhenExpire(key);
                    }

                    @Override
                    public ListenableFuture<V> reload(final K key, V oldValue) {
                        return backgroundRefreshPools.submit(() -> getValueWhenExpire(key));
                    }
                });
    }

    /**
     * 从cache中拿出数据的操作
     *
     * @param key
     * @return
     * @throws ExecutionException
     */
    protected V fetchDataFromCache(K key) throws Exception {
        return getCache().get(key);
    }


    /**
     * 获取cache实例
     *
     * @return
     */
    private LoadingCache<K, V> getCache() throws Exception {
        if (Objects.nonNull(cache)) {
            return cache;
        }
        synchronized (this) {
            if (Objects.nonNull(cache)) {
                loadRefreshAfterWrite();
            }
        }
        return cache;
    }
}
