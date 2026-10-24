package com.tecsun.sisp.iface.server.util;

import org.springframework.web.context.ContextLoader;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Bootstraps the infrastructure the application needs before it can serve a request.
 *
 * <p>Registered in {@code web.xml} after {@code ContextLoaderListener}, so the Spring root
 * context is already available when {@link #contextInitialized} runs.
 *
 * <p>Two things happen, in order:
 * <ol>
 *   <li>{@code DataBase.init()} builds the Redis connection pool. Redis backs the
 *       interface token, the session cache and the dictionary cache, so it is
 *       load-bearing rather than an optimisation.</li>
 *   <li>{@code ThreadPoolUtil.init()} creates the shared executor used by asynchronous
 *       work.</li>
 * </ol>
 *
 * <p><strong>Fail-fast by design.</strong> If Redis is unreachable, this method throws and
 * the deployment fails. That is intentional for a service behind a health check, and
 * hostile for local development — see
 * {@code docs/TROUBLESHOOTING.md#startup-hangs-or-fails-on-redis}.
 *
 * <p>{@link #contextDestroyed} is empty: pools are not closed on undeploy, so a container
 * reload leaks connections until the JVM exits.
 */
public class StartupListener extends ContextLoader implements ServletContextListener {

    /**
     * Initialises the Redis pool and the shared thread pool.
     *
     * @param servletContextEvent the servlet context being initialised
     * @throws RuntimeException if Redis cannot be reached, which aborts deployment
     */
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        DataBase.init();
        ThreadPoolUtil.init();
//        try {
//            DBConnection.initConn();
//        } catch (SQLException e) {
//            logger.error("初始化程序失败：无法初始化数据库连接:database sisp");
//        }
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

}
