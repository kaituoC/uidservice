package com.ifeng.controller;

import com.ifeng.entity.ResponseEntity;
import com.ifeng.entity.UserEntity;
import com.ifeng.service.UidService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

/**
 * Created by chang on 2017/8/3.
 */
@RestController
@EnableAutoConfiguration
public class UidController {
    private Logger logger = LoggerFactory.getLogger(UidController.class);

    private final String APP_V1 = "/baseSys/1.0";

    @Autowired
    UidService uidService;
    @RequestMapping(APP_V1 + "/getSpecialUid")
    public String getUid() {
        String resultSetStr = uidService.getUidAsString();
        return resultSetStr;
    }

    @RequestMapping(APP_V1 + "/getResponseEntity")
    public ResponseEntity getResponseEntity() {
        logger.info("enter /getResponseEntity");
        ResponseEntity responseEntity = uidService.getResponseEntity();
        return responseEntity;
    }

    @RequestMapping(APP_V1 + "/checkHelth")
    public String checkHelth() {
        return "ok";
    }

    @RequestMapping(value = APP_V1 + "/addUser", method = RequestMethod.POST)
    @ResponseBody
    public String addUser(@RequestBody UserEntity userEntity) {
        UserEntity ue = new UserEntity();
        ue.setUserId(userEntity.getUserId());
        ue.setUserName(userEntity.getUserName());
        return uidService.addUser(ue);
    }

}
