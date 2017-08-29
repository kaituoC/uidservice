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
    public String setStringValueIntoRedis(String key, String value) {

    }
    public Set<String> getUidSet() {
        this.jedis = getRedisConnection();
        Set<String> uidSet = jedis.smembers(SPECIAL_UID_KEY);
        logger.info("uidSet = " + uidSet.toString());
        jedis.close();
        return uidSet;
    }

    public Map<String, String> getUserMap() {
        this.jedis = getRedisConnection();
        Map<String, String> userMap = jedis.hgetAll("specialUserMap");
        logger.info("userMap = " + userMap.toString());
        jedis.close();
        return userMap;
    }

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
