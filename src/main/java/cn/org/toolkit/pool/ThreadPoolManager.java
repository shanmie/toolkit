package cn.org.toolkit.pool;


import java.util.concurrent.*;

/**
 * @Package: cn.org.toolkit.pool
 * @ClassName: ThreadPoolManager
 * @Description:
 * @Author: mac-pro
 * @CreateDate: 2019/2/22 下午3:45
 * @Version: 1.0
 */
public class ThreadPoolManager {
    private ThreadPoolExecutor threadPoolExecutor;

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    private ThreadPoolManager() {
        int corePoolSize = Runtime.getRuntime().availableProcessors() * 20 - 1;
        int maximumPoolSize = Runtime.getRuntime().availableProcessors() * 50 - 1;
        long keepAliveTime = 60L;
        TimeUnit milliseconds = TimeUnit.SECONDS;
        BlockingQueue<Runnable> runnableTaskQueue = new LinkedBlockingQueue<>(); // FIFO, 无界阻塞队列
        ThreadFactory threadFactory = new DefaultThreadFactory();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy(); // 默认, 饱和则抛出异常

        this.threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, milliseconds, runnableTaskQueue, threadFactory, handler);

        this.scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(corePoolSize, threadFactory, handler);
    }

    public static ThreadPoolManager getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private final static ThreadPoolManager instance = new ThreadPoolManager();
    }

    public void execute(Runnable runnable) {
        this.threadPoolExecutor.execute(runnable);
    }

    public Future<?> submit(Runnable runnable) {
        return this.threadPoolExecutor.submit(runnable);
    }

    /**
     * @param runnable
     * @param period   单位: 毫秒
     * @return
     */
    public ScheduledFuture scheduleAtFixedRate(Runnable runnable, long period) {
        return this.scheduledThreadPoolExecutor.scheduleAtFixedRate(runnable, 0, period, TimeUnit.MILLISECONDS);
    }

    /**
     * @param runnable
     * @param initialDelay
     * @param period
     * @param timeUnit
     * @return
     */
    public ScheduledFuture scheduleAtFixedRate(Runnable runnable, long initialDelay, long period, TimeUnit timeUnit) {
        return this.scheduledThreadPoolExecutor.scheduleAtFixedRate(runnable, initialDelay, period, timeUnit);
    }

    /**
     * @param runnable
     * @param period   单位: 毫秒
     * @return
     */
    public ScheduledFuture scheduleWithFixedDelay(Runnable runnable, long period) {
        return this.scheduledThreadPoolExecutor.scheduleWithFixedDelay(runnable, 0, period, TimeUnit.MILLISECONDS);
    }

    /**
     * @param runnable
     * @param initialDelay
     * @param period
     * @param timeUnit
     * @return
     */
    public ScheduledFuture scheduleWithFixedDelay(Runnable runnable, long initialDelay, long period, TimeUnit timeUnit) {
        return this.scheduledThreadPoolExecutor.scheduleWithFixedDelay(runnable, initialDelay, period, timeUnit);
    }
}
