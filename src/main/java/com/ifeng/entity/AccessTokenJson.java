package com.ifeng.entity;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by chang on 2017/9/4.
 */
public class AccessTokenJson {
    private int errorcode = 0;
    private String errmsg = "ok";
    private String accessToken = "";
    private int expiresIn = 0;

    public AccessTokenJson(String jsonStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        this.errorcode = jsonObject.getInteger("errcode");
        this.errmsg = jsonObject.getString("errmsg");
        this.accessToken = jsonObject.getString("access_token");
        this.expiresIn = jsonObject.getIntValue("expires_in");
    }

    public int getErrorcode() {
        return errorcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }
}
