package com.ifeng.service;

import com.ifeng.dao.RedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by chang on 2017/8/3.
 */
@Service
@EnableAutoConfiguration
public class UidService {
    @Autowired
    RedisDao redisDao;

    public String getUidAsString() {
        return getUidSetFromRedis().toString();
    }

    private Set<String> getUidSetFromRedis() {
        Set<String> uidSet = redisDao.getUidSet();
        return uidSet;
    }
}
