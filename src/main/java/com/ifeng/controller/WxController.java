package com.ifeng.controller;

import com.ifeng.entity.AccessToken;
import com.ifeng.service.WxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by chang on 2017/8/11.
 */
@RestController
@EnableAutoConfiguration
public class WxController {
    private Logger logger = LoggerFactory.getLogger(WxController.class);

    private final String APP_V1 = "/baseSys/1.0";

    @Autowired
    WxService wxService;

    @RequestMapping(APP_V1 + "/getMonitorToken")
    @ResponseBody
    public String getMonitorToken() {
        AccessToken monitorAccessToken = wxService.getAccessToken("monitorToken");
        return monitorAccessToken.toString();
    }
}
