package com.tecsun.sisp.iface.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * ClassName: JedisUtil
 * Description:
 * CreateTime： 2015年06月07日 16时:00分
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
