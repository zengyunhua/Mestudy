package com.tecsun.sisp.iface.server.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ClassName: ThreadPoolUtil
 * Description:线程池用来处理业务分析子系统数据（交易日志等异步任务）
 * CreateTime： 2015年07月31日 15时:20分
 *
 * <p>The pool is bounded: at most {@link #MAX_THREADS} threads and {@link #QUEUE_CAPACITY}
 * queued tasks. When both are full the submitting request thread runs the task itself
 * ({@link ThreadPoolExecutor.CallerRunsPolicy}), which slows callers down instead of
 * creating threads without limit or dropping work. Idle threads exit after 60 seconds.
 */
public class ThreadPoolUtil {

    static final int MAX_THREADS = 32;
    static final int QUEUE_CAPACITY = 1000;

    private static volatile ThreadPoolExecutor threadPool = null;

    public static synchronized void init() {
        if (threadPool == null || threadPool.isShutdown()) {
            threadPool = newThreadPool();
        }
    }

    static ThreadPoolExecutor newThreadPool() {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(MAX_THREADS, MAX_THREADS,
                60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(QUEUE_CAPACITY),
                new NamedDaemonThreadFactory("sisp-iface-worker-"),
                new ThreadPoolExecutor.CallerRunsPolicy());
        pool.allowCoreThreadTimeOut(true);
        return pool;
    }

    public static ThreadPoolExecutor getThreadPool() {
        ThreadPoolExecutor pool = threadPool;
        if (pool != null && !pool.isShutdown()) {
            return pool;
        }
        synchronized (ThreadPoolUtil.class) {
            init();
            return threadPool;
        }
    }

    /**
     * Stops accepting tasks and waits up to {@code timeoutSeconds} for queued ones.
     * Called from {@code StartupListener.contextDestroyed()} so a redeploy does not leak threads.
     */
    public static synchronized void shutdown(long timeoutSeconds) {
        if (threadPool == null) {
            return;
        }
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(timeoutSeconds, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        threadPool = null;
    }

    private static final class NamedDaemonThreadFactory implements ThreadFactory {
        private final String prefix;
        private final AtomicInteger next = new AtomicInteger(1);

        NamedDaemonThreadFactory(String prefix) {
            this.prefix = prefix;
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, prefix + next.getAndIncrement());
            t.setDaemon(true);
            return t;
        }
    }
}
