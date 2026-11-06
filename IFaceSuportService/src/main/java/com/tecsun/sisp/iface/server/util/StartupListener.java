package com.tecsun.sisp.iface.server.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Bootstraps the infrastructure the application needs before it can serve a request, and
 * releases it on undeploy.
 *
 * <p>Registered in {@code web.xml} after {@code ContextLoaderListener}, so the Spring root
 * context is already available when {@link #contextInitialized} runs.
 *
 * <p>Two things happen, in order:
 * <ol>
 *   <li>{@code DataBase.init()} builds the Redis connection pool. Redis backs the
 *       interface token, the session cache and the dictionary cache, so it is
 *       load-bearing rather than an optimisation.</li>
 *   <li>{@code ThreadPoolUtil.init()} creates the shared, bounded executor used by
 *       asynchronous work.</li>
 * </ol>
 *
 * <p><strong>Fail-fast by design.</strong> If Redis is unreachable or
 * {@code redis_core_database} is malformed, this method throws and the deployment fails.
 * That is intentional for a service behind a health check, and hostile for local
 * development — see {@code docs/TROUBLESHOOTING.md#startup-hangs-or-fails-on-redis}.
 *
 * <p>{@link #contextDestroyed} drains the executor for up to 10 seconds and closes the Redis
 * pool, so a container reload does not leak threads or connections.
 */
public class StartupListener implements ServletContextListener {

    static final long SHUTDOWN_TIMEOUT_SECONDS = 10L;

    /**
     * Initialises the Redis pool and the shared thread pool.
     *
     * @param servletContextEvent the servlet context being initialised
     * @throws RuntimeException if Redis cannot be reached, which aborts deployment
     */
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        DataBase.init();
        ThreadPoolUtil.init();
    }

    /** Stops the executor first, because queued tasks may still use Redis. */
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ThreadPoolUtil.shutdown(SHUTDOWN_TIMEOUT_SECONDS);
        DataBase.close();
    }

}
