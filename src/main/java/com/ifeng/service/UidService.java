package com.ifeng.service;

import com.ifeng.dao.RedisDao;
import com.ifeng.entity.UidServiceResponseEntity;
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

    public UidServiceResponseEntity getResponseEntity() {
        UidServiceResponseEntity uidServiceResponseEntity = new UidServiceResponseEntity();
        Map<String, String> userMap = redisDao.getUserMap();
        if (!userMap.isEmpty()) {
            uidServiceResponseEntity.setMsg("ok");
            uidServiceResponseEntity.setCode(0);
            uidServiceResponseEntity.setUserMap(userMap);
            uidServiceResponseEntity.setUserSet(userMap.keySet());
        }
        uidServiceResponseEntity.setTimeStamp(System.currentTimeMillis());
        return uidServiceResponseEntity;
    }

    public String addUser(UserEntity userEntity) {
        if (redisDao.addUserIntoRedisMap(userEntity)) {
            return "OK\r\n";
        }
        return "ERROR\r\n";
    }
}
