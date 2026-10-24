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
 * connection timeout. If the key is blank or unparseable the pool stays {@code null} and
 * every later {@link #getJedisPublicPool()} call fails.
 *
 * <p><strong>Fail-fast.</strong> Redis backs the interface token, the session cache and
 * the dictionary cache. An unreachable Redis aborts deployment rather than degrading —
 * intentional for a service behind a health check.
 *
 * <p>{@link #init()} is guarded by a plain boolean, not a lock, so it is not safe to call
 * concurrently. In practice it runs once during servlet initialisation.
 */
public class DataBase {

    protected static final Log logger = LogFactory.getLog(DataBase.class);

    private static JedisPool jedisPublicPool;

    private static boolean isInit = false;

    public static final void init() {
        if (!isInit) {
            initJedisCenterPool();
            isInit=true;
        }
    }

    private static final void initJedisCenterPool() {
        String jedisInfoStr = Config.getInstance().get("redis_core_database");
        if (StringUtils.isNotBlank(jedisInfoStr)) {
            String[] jedisInfo = jedisInfoStr.split("_");
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(500);
            config.setMaxIdle(20);
            config.setMaxWaitMillis(5000L);
            config.setTestOnBorrow(true);
            jedisPublicPool = new JedisPool(config, jedisInfo[0], Integer.valueOf(jedisInfo[1]).intValue(), 10000);
        } else {
            logger.error("iface init redis is error:can not read config.properties");
        }
    }

    public static JedisPool getJedisPublicPool() {
        if (jedisPublicPool == null) {
            initJedisCenterPool();
        }
        return jedisPublicPool;
    }
}
