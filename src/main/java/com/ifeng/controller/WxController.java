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

    /**
     * 发送微信的命令
     * curl -l -H "Content-type: application/json" -X POST -d '{"groupId":1,"toUser":"ChangKaiTuo","toParty":"","toTag":"","msgType":"text","appType":"monitor","text":{"content":"this is a test message by changkaituo"},"sign":"a50ea7c63c146ec40a838ab81f8193b2"}' http://192.168.193.1:8080/baseSys/1.0/sendMessage
     * @param postToMeEntity
     * @return
     */
    @RequestMapping(value = APP_V1 + "/sendMessage", method = RequestMethod.POST)
    @ResponseBody
    public WxResponseMsg sendMessage(@RequestBody PostToMeEntity postToMeEntity) {
        logger.info("receive a request:" + postToMeEntity);
        WxResponseMsg responseEntity = wxService.sendMessage(postToMeEntity);
        return responseEntity;
    }
}
