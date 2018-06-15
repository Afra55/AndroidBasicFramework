package com.afra55.commontutils.sys;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Afra55
 * @date 2018/3/19
 * A smile is the best business card.
 * 多线程帮助类
 */

public class ThreadUtils {
    private ThreadUtils() {

    }

    private static final int KEEP_ALIVE_TIME = 1;
    private static TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    public static void excute(String threadName, Runnable runnable) {
        int numberOfCores = Runtime.getRuntime().availableProcessors();

        BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>(1024);

        ExecutorService executorService = new ThreadPoolExecutor(
                numberOfCores,
                numberOfCores * 2,
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                taskQueue,
                new ThreadFactoryBuilder(threadName),
                new ThreadPoolExecutor.CallerRunsPolicy());
        executorService.execute(runnable);
    }

    private static class ThreadFactoryBuilder implements ThreadFactory {
        private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;
        private final String threadName;

        ThreadFactoryBuilder(String threadName) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" +
                    POOL_NUMBER.getAndIncrement() +
                    "-thread-";
            this.threadName = threadName;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            t.setName(threadName);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
}
