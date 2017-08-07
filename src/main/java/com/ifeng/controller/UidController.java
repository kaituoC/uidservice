package com.ifeng.controller;

import com.ifeng.entity.ResponseEntity;
import com.ifeng.service.UidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by chang on 2017/8/3.
 */
@RestController
@EnableAutoConfiguration
public class UidController {
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
        System.out.println("enter /getResponseEntity");
        ResponseEntity responseEntity = new ResponseEntity();
        return responseEntity;
    }

    @RequestMapping(APP_V1 + "/checkHelth")
    public String checkHelth() {
        return "ok";
    }
}
