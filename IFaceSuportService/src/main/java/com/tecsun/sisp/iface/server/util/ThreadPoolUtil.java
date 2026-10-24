package com.tecsun.sisp.iface.server.util;

import java.util.concurrent.*;

/**
 * ClassName: ThreadPoolUtil
 * Description:线程池用来处理业务分析子系统数据
 * CreateTime： 2015年07月31日 15时:20分
 */
public class ThreadPoolUtil {

    private static ThreadPoolExecutor threadPool = null;

    private static boolean isInit = false;

    public static final void init() {
        if (!isInit) {
            initThreadPool();
            isInit = true;
        }
    }

    /**
     *  CachedThreadPool 执行线程不固定，
     *  好处：可以把新增任务全部缓存在一起，
     *  坏处：只能用在短时间完成的任务（占用时间较长的操作可以导致线程数无限增大，系统资源耗尽）
     */
    private static void initThreadPool() {
        threadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
    }

    public static ThreadPoolExecutor getThreadPool() {
        if (threadPool == null) {
            initThreadPool();
        }
        return threadPool;
    }

}
