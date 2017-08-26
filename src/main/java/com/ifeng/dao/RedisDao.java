package com.ifeng.dao;

import com.ifeng.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by chang on 2017/8/4.
 */
@Repository
public class RedisDao {
    private Logger logger = LoggerFactory.getLogger(RedisDao.class);

    private final String SPECIAL_UID_KEY = "specialUid";
    private String ip = "192.168.193.130";
    private int port = 6379;
    private Jedis jedis = null;

    /**
     * 从redis中获取某个key的值
     * @param key 想要获取的key
     * @return String
     */
    public String getStrValueByKey(String key) {
        this.jedis = getRedisConnection();
        return jedis.get(key);
    }

    /**
     * 将 key,value 写入redis，成功返回true
     * @param key   需要写入的key
     * @param value 需要写入的key对应的value
     * @return      成功返回true，失败返回false
     */
    public boolean setStringValueIntoRedis(String key, String value) {
        this.jedis = getRedisConnection();
        String resultStr = jedis.set(key, value);
        if ("OK".equals(resultStr)) {
            logger.info("set into redis succee: " + resultStr);
            return true;
        }
        logger.error("set into redis failed: " + resultStr);
        return false;
    }

    /**
     * 从 redsi 中获取 uidSet 集合
     * @return  Set<String>
     */
    public Set<String> getUidSet() {
        this.jedis = getRedisConnection();
        Set<String> uidSet = jedis.smembers(SPECIAL_UID_KEY);
        logger.info("uidSet = " + uidSet.toString());
        jedis.close();
        return uidSet;
    }

    /**
     * 从 redis 中获取 userMap
     * @return
     */
    public Map<String, String> getUserMap() {
        this.jedis = getRedisConnection();
        Map<String, String> userMap = jedis.hgetAll("specialUserMap");
        logger.info("userMap = " + userMap.toString());
        jedis.close();
        return userMap;
    }

    /**
     * 添加用户到redis中的userMap中
     * @param userEntity
     * @return
     */
    public boolean addUserIntoRedisMap(UserEntity userEntity) {
        try {
            this.jedis = getRedisConnection();
            Map<String, String> userMap = new HashMap<>();
            userMap.put(userEntity.getUserId(), userEntity.getUserName());
            jedis.hmset("specialUserMap", userMap);
            logger.info("set <" + userEntity.getUserId() + ":" + userEntity.getUserName() + "> into redis");
        } catch (Exception e) {
            logger.error("set <" + userEntity.getUserId() + ":" + userEntity.getUserName() + "> into redis failed!", e);
            return false;
        }
        return true;
    }

    private Jedis getRedisConnection() {
        if (this.jedis == null) {
            try {
                this.jedis = new Jedis(ip, port);
            } catch (Exception e) {
                System.out.println("get redis connection failed!");
            }
        }
        return jedis;
    }
}
