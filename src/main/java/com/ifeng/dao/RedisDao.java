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

    public Set<String> getUidSet() {
        Jedis jedis = getRedisConnection();
        Set<String> uidSet = jedis.smembers(SPECIAL_UID_KEY);
        logger.info("uidSet = " + uidSet.toString());
        jedis.close();
        return uidSet;
    }

    public Map<String, String> getUserMap() {
        Jedis jedis = getRedisConnection();
        Map<String, String> userMap = jedis.hgetAll("specialUserMap");
        logger.info("userMap = " + userMap.toString());
        jedis.close();
        return userMap;
    }

    public boolean addUserIntoRedisMap(UserEntity userEntity) {
        try {
            Jedis jedis = getRedisConnection();
            Map<String, String> userMap = new HashMap<>();
            userMap.put(userEntity.getUserId(), userEntity.getUserName());
            jedis.hmset("specialUserMap", userMap);
            logger.info("set <" + userEntity.getUserId() + ":" + userEntity.getUserName() + "> into redis");
        } catch (Exception e) {
            logger.error("set <" + userEntity.getUserId() + ":" + userEntity.getUserName() + "> into redis failed!");
            return false;
        }
        return true;
    }

    private Jedis getRedisConnection() {
        Jedis jedis = null;
        try {
            jedis = new Jedis(ip, port);
        } catch (Exception e) {
            System.out.println("get redis connection failed!");
        }
        return jedis;
    }
}
