package com.ifeng.service;

import com.ifeng.dao.MysqlDao;
import com.ifeng.entity.AccessToken;
import com.ifeng.entity.SystemConf;
import com.ifeng.utils.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chang on 2017/8/11.
 */
@Service
public class WxService {

    @Autowired
    MysqlDao mysqlDao;
    public AccessToken getAccessToken(String tokenName) {
        String secret = "";
        if ("monitorToken".equals(tokenName)) {
            secret = "monitorSecret";
        } else if ("addrListToken".equals(tokenName)) {
            secret = "addrListSecret";
        } else {
            return null;
        }
        SystemConf systemConf = SystemConf.getSystemConf(mysqlDao);
        String wxUrlPrefix = systemConf.getSystemConfValue("wxUrlPrefix");
        String getToken = systemConf.getSystemConfValue("getToken");
        String corpID = systemConf.getSystemConfValue("CorpID");
        String corpSecret = systemConf.getSystemConfValue(secret);
        String url = wxUrlPrefix + getToken;
        String param = "corpid=" + corpID + "&corpsecret=" + corpSecret;
        String jsonStr = HttpRequest.sendGet(url, param);
        AccessToken accessToken = new AccessToken(jsonStr);
        return accessToken;
    }
}
