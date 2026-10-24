package com.tecsun.sisp.iface.common.util;

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
    	System.out.println("ThreadPoolUtil init...");
        if (!isInit) {
            initThreadPool();
            isInit = true;
        }
    }

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
