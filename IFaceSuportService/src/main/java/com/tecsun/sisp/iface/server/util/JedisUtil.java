package com.tecsun.sisp.iface.server.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Thin wrapper over the shared {@link redis.clients.jedis.JedisPool} for cached values.
 *
 * <p>All TTL-bearing methods read {@code redis_iface_expx} and multiply it by 60, so that
 * key is in <strong>minutes</strong> despite its name. A missing or non-numeric value
 * throws {@code NumberFormatException} on the first cache write, not at startup.
 *
 * <p>Values are stored as {@code String}. There is no serialisation layer; callers convert
 * to and from JSON themselves.
 *
 * <p><strong>Known defect — double return.</strong> Every method calls
 * {@code returnBrokenResource(jedis)} in its {@code catch} block and then
 * {@code returnResource(jedis)} on the same instance in {@code finally}. Jedis 2.x throws
 * {@code IllegalStateException("Returned object not currently part of this pool")} on the
 * second return, and the pool can end up holding a connection it considers broken. If the
 * pool reference itself is {@code null} — because {@code DataBase.init()} never ran — the
 * {@code catch} block throws {@code NullPointerException} and hides the original failure.
 *
 * <p>Until this is fixed, treat Redis errors as noisy rather than precise: read the first
 * exception in the chain, not the last. See
 * {@code docs/TROUBLESHOOTING.md#redis-errors-mask-themselves}.
 */
public class JedisUtil {

    protected static final Log logger = LogFactory.getLog(JedisUtil.class);

    public static void setValue(String key, String value) throws Exception {
        setValue(key, value, Integer.parseInt(Config.getInstance().get("redis_iface_expx")));
    }

    public static void setValue(String key, String value, Integer outTime) throws Exception {
        JedisPool publicJedisPool = null;
        Jedis publicJedis = null;
        try {
            publicJedisPool = DataBase.getJedisPublicPool();
            publicJedis = publicJedisPool.getResource();
            publicJedis.setex(key, 60 * outTime, value);
        } catch (Exception e) {
            publicJedisPool.returnBrokenResource(publicJedis);
            throw new Exception("设置redis失败: key:" + key + ",value:" + value, e);
        } finally {
            if (publicJedis != null) {
                publicJedisPool.returnResource(publicJedis);
            }
        }
    }

    public static String getValue(String key) throws Exception {
        JedisPool publicJedisPool = null;
        Jedis publicJedis = null;
        String value="";
        try {
            publicJedisPool = DataBase.getJedisPublicPool();
            publicJedis = publicJedisPool.getResource();
            value = publicJedis.get(key);
        } catch (Exception e) {
            publicJedisPool.returnBrokenResource(publicJedis);
            throw new Exception("获取reids失败，key:" + key, e);
        } finally {
            if (publicJedis != null) {
                publicJedisPool.returnResource(publicJedis);
            }
        }
        return value;
    }

    /**
     * 退出时将token删除
     *
     * @param key
     * @throws Exception
     */
    public static void delValue(String key) throws Exception {
        JedisPool publicJedisPool = null;
        Jedis publicJedis = null;
        try {
            publicJedisPool = DataBase.getJedisPublicPool();
            publicJedis = publicJedisPool.getResource();
            publicJedis.del(key);
        } catch (Exception e) {
            publicJedisPool.returnBrokenResource(publicJedis);
            throw new Exception("删除reids失败，key:" + key, e);
        } finally {
            if (publicJedis != null) {
                publicJedisPool.returnResource(publicJedis);
            }
        }
    }
}
