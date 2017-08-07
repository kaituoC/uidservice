package com.ifeng.dao;

import redis.clients.jedis.Jedis;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by chang on 2017/8/4.
 */
@Repository
public class RedisDao {
    private final String SPECIAL_UID_KEY = "specialUid";
    private String ip = "192.168.193.130";
    private int port = 6379;

    public Set<String> getUidSet() {
        Jedis jedis = getRedisConnection();
        Set<String> uidSet = jedis.smembers(SPECIAL_UID_KEY);
        System.out.println("uidSet = " + uidSet.toString());
        return uidSet;
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
