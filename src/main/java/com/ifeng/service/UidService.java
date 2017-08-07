package com.ifeng.service;

import com.ifeng.dao.RedisDao;
import com.ifeng.entity.ResponseEntity;
import com.ifeng.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.Map;
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

    public ResponseEntity getResponseEntity() {
        ResponseEntity responseEntity = new ResponseEntity();
        Map<String, String> userMap = redisDao.getUserMap();
        if (!userMap.isEmpty()) {
            responseEntity.setMsg("ok");
            responseEntity.setCode(0);
            responseEntity.setUserMap(userMap);
            responseEntity.setUserSet(userMap.keySet());
        }
        responseEntity.setTimeStamp(System.currentTimeMillis());
        return responseEntity;
    }

    public String addUser(UserEntity userEntity) {
        if (redisDao.addUserIntoRedisMap(userEntity)) {
            return "OK\r\n";
        }
        return "ERROR\r\n";
    }
}
