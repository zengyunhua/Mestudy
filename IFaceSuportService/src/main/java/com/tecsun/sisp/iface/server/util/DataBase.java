package com.tecsun.sisp.iface.server.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Owns the process-wide Redis connection pool.
 *
 * <p>{@link #init()} is called once from {@code StartupListener.contextInitialized()}.
 * It parses {@code redis_core_database} (format {@code host_port}) and builds a
 * {@link JedisPool} with 500 max connections, 20 idle, a 5s borrow wait and a 10s
 * connection timeout. If the key is blank the pool stays {@code null}, an error is logged,
 * and every later {@link #getJedisPublicPool()} call returns {@code null}. If the key is
 * present but not {@code host_port}, {@link #init()} throws {@link IllegalStateException}
 * naming the key, so the deployment fails with a readable message.
 *
 * <p><strong>Fail-fast.</strong> Redis backs the interface token, the session cache and
 * the dictionary cache. An unreachable Redis aborts deployment rather than degrading —
 * intentional for a service behind a health check.
 *
 * <p>No password or TLS is configured: the Jedis 2.6 constructor used here does not take them.
 * {@link #close()} is called from {@code StartupListener.contextDestroyed()}.
 */
public class DataBase {

    protected static final Log logger = LogFactory.getLog(DataBase.class);

    private static volatile JedisPool jedisPublicPool;

    public static synchronized void init() {
        if (jedisPublicPool == null) {
            initJedisCenterPool();
        }
    }

    private static void initJedisCenterPool() {
        String jedisInfoStr = Config.getInstance().get("redis_core_database");
        if (StringUtils.isNotBlank(jedisInfoStr)) {
            String[] hostPort = parseHostPort(jedisInfoStr);
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(500);
            config.setMaxIdle(20);
            config.setMaxWaitMillis(5000L);
            config.setTestOnBorrow(true);
            jedisPublicPool = new JedisPool(config, hostPort[0], Integer.parseInt(hostPort[1]), 10000);
        } else {
            logger.error("iface init redis is error:can not read config.properties");
        }
    }

    /**
     * Splits {@code host_port} at the last underscore and checks the port.
     *
     * @throws IllegalStateException if the value is not {@code host_port} with a port in 1-65535
     */
    static String[] parseHostPort(String value) {
        String trimmed = value.trim();
        int split = trimmed.lastIndexOf('_');
        if (split > 0 && split < trimmed.length() - 1) {
            String host = trimmed.substring(0, split);
            String port = trimmed.substring(split + 1);
            if (port.matches("\\d{1,5}")) {
                int number = Integer.parseInt(port);
                if (number >= 1 && number <= 65535) {
                    return new String[] {host, port};
                }
            }
        }
        throw new IllegalStateException(
                "Malformed redis_core_database: expected host_port, for example 127.0.0.1_6379");
    }

    public static JedisPool getJedisPublicPool() {
        JedisPool pool = jedisPublicPool;
        if (pool != null) {
            return pool;
        }
        synchronized (DataBase.class) {
            if (jedisPublicPool == null) {
                initJedisCenterPool();
            }
            return jedisPublicPool;
        }
    }

    /** Closes the pool. Safe to call when it was never created. */
    public static synchronized void close() {
        if (jedisPublicPool != null) {
            try {
                jedisPublicPool.destroy();
            } catch (RuntimeException e) {
                logger.warn("closing the Redis pool failed", e);
            }
            jedisPublicPool = null;
        }
    }
}
