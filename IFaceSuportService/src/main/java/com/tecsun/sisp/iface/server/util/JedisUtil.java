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
 * <p>Broken connections are returned once; error messages do not expose keys or values.
 */
public class JedisUtil {

    protected static final Log logger = LogFactory.getLog(JedisUtil.class);

    public static void setValue(String key, String value) throws Exception {
        setValue(key, value, Integer.parseInt(Config.getInstance().get("redis_iface_expx")));
    }

    public static void setValue(String key, String value, Integer outTime) throws Exception {
        JedisPool publicJedisPool = null;
        Jedis publicJedis = null;
        boolean broken = false;
        try {
            publicJedisPool = DataBase.getJedisPublicPool();
            publicJedis = publicJedisPool.getResource();
            publicJedis.setex(key, 60 * outTime, value);
        } catch (Exception e) {
            broken = true;
            if (publicJedisPool != null && publicJedis != null) {
                try { publicJedisPool.returnBrokenResource(publicJedis); }
                catch (Exception releaseError) { logger.warn("Redis broken resource release failed", releaseError); }
            }
            throw new Exception("设置redis失败", e);
        } finally {
            if (publicJedis != null && !broken) {
                publicJedisPool.returnResource(publicJedis);
            }
        }
    }

    public static String getValue(String key) throws Exception {
        JedisPool publicJedisPool = null;
        Jedis publicJedis = null;
        boolean broken = false;
        String value="";
        try {
            publicJedisPool = DataBase.getJedisPublicPool();
            publicJedis = publicJedisPool.getResource();
            value = publicJedis.get(key);
        } catch (Exception e) {
            broken = true;
            if (publicJedisPool != null && publicJedis != null) {
                try { publicJedisPool.returnBrokenResource(publicJedis); }
                catch (Exception releaseError) { logger.warn("Redis broken resource release failed", releaseError); }
            }
            throw new Exception("获取redis失败", e);
        } finally {
            if (publicJedis != null && !broken) {
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
        boolean broken = false;
        try {
            publicJedisPool = DataBase.getJedisPublicPool();
            publicJedis = publicJedisPool.getResource();
            publicJedis.del(key);
        } catch (Exception e) {
            broken = true;
            if (publicJedisPool != null && publicJedis != null) {
                try { publicJedisPool.returnBrokenResource(publicJedis); }
                catch (Exception releaseError) { logger.warn("Redis broken resource release failed", releaseError); }
            }
            throw new Exception("删除redis失败", e);
        } finally {
            if (publicJedis != null && !broken) {
                publicJedisPool.returnResource(publicJedis);
            }
        }
    }
}
