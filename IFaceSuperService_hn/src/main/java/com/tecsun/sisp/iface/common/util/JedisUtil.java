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
        boolean broken = false;
        try {
            publicJedisPool = DataBase.getJedisPublicPool();
            publicJedis = publicJedisPool.getResource();
            publicJedis.setex(key, 60 * outTime, value);
        } catch (Exception e) {
            broken = true;
            throw new Exception("设置redis失败", e);
        } finally {
            if (publicJedis != null) {
                if (broken) {
                    publicJedisPool.returnBrokenResource(publicJedis);
                } else {
                    publicJedisPool.returnResource(publicJedis);
                }
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
            throw new Exception("获取redis失败", e);
        } finally {
            if (publicJedis != null) {
                if (broken) {
                    publicJedisPool.returnBrokenResource(publicJedis);
                } else {
                    publicJedisPool.returnResource(publicJedis);
                }
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
            throw new Exception("删除redis失败", e);
        } finally {
            if (publicJedis != null) {
                if (broken) {
                    publicJedisPool.returnBrokenResource(publicJedis);
                } else {
                    publicJedisPool.returnResource(publicJedis);
                }
            }
        }
    }
}
