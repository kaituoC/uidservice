package com.ifeng.controller;

import com.ifeng.entity.AccessToken;
import com.ifeng.entity.PostToMeEntity;
import com.ifeng.entity.WxResponseMsg;
import com.ifeng.service.WxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(APP_V1 + "/getServiceId")
    @ResponseBody
    public String getServiceId() {
        return wxService.toString() + "---" + System.currentTimeMillis();
    }

    @RequestMapping(value = APP_V1 + "/sendMessage", method = RequestMethod.POST)
    @ResponseBody
    public WxResponseMsg sendMessage(@RequestBody PostToMeEntity postToMeEntity) {
        logger.info("receive a request:" + postToMeEntity);
        WxResponseMsg responseEntity = wxService.sendMessage(postToMeEntity);
        return responseEntity;
    }
}
