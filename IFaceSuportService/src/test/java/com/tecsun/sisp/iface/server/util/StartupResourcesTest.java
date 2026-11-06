package com.tecsun.sisp.iface.server.util;

import org.junit.After;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/** Startup/shutdown behaviour that does not need Redis. */
public class StartupResourcesTest {

    @After
    public void stopPool() {
        ThreadPoolUtil.shutdown(1);
    }

    @Test
    public void parsesHostPort() {
        assertArrayEquals(new String[] {"127.0.0.1", "6379"}, DataBase.parseHostPort("127.0.0.1_6379"));
        assertArrayEquals(new String[] {"redis_host", "6380"}, DataBase.parseHostPort(" redis_host_6380 "));
    }

    @Test
    public void malformedRedisKeyNamesTheKey() {
        String[] bad = {"127.0.0.1", "127.0.0.1_", "_6379", "host_port", "host_0", "host_70000"};
        for (String value : bad) {
            try {
                DataBase.parseHostPort(value);
                fail("accepted " + value);
            } catch (IllegalStateException e) {
                assertTrue(e.getMessage().contains("redis_core_database"));
            }
        }
    }

    @Test
    public void closeIsSafeWithoutPool() {
        DataBase.close();
        DataBase.close();
    }

    @Test
    public void threadPoolIsBounded() {
        ThreadPoolExecutor pool = ThreadPoolUtil.getThreadPool();

        assertEquals(ThreadPoolUtil.MAX_THREADS, pool.getMaximumPoolSize());
        assertEquals(ThreadPoolUtil.QUEUE_CAPACITY, pool.getQueue().remainingCapacity());
        assertTrue(pool.getRejectedExecutionHandler() instanceof ThreadPoolExecutor.CallerRunsPolicy);
        assertSame(pool, ThreadPoolUtil.getThreadPool());
    }

    @Test
    public void runsTasksOnNamedDaemonThreads() throws Exception {
        final CountDownLatch done = new CountDownLatch(1);
        final AtomicReference<Thread> worker = new AtomicReference<Thread>();

        ThreadPoolUtil.getThreadPool().execute(new Runnable() {
            public void run() {
                worker.set(Thread.currentThread());
                done.countDown();
            }
        });

        assertTrue(done.await(5, TimeUnit.SECONDS));
        assertTrue(worker.get().getName().startsWith("sisp-iface-worker-"));
        assertTrue(worker.get().isDaemon());
    }

    @Test
    public void shutdownStopsPoolAndNextUseCreatesANewOne() {
        ThreadPoolExecutor first = ThreadPoolUtil.getThreadPool();

        new StartupListener().contextDestroyed(null);

        assertTrue(first.isShutdown());
        ThreadPoolExecutor second = ThreadPoolUtil.getThreadPool();
        assertNotSame(first, second);
        assertFalse(second.isShutdown());
    }
}
