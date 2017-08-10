package com.ifeng.entity;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by chang on 2017/8/10.
 */
public class AccessToken {
    private int errcode = 0;
    private String errmsg = "ok";
    private String accessToken = "";
    private int expiresIn = 0;

    public AccessToken(String jsonStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        this.errcode = jsonObject.getInteger("errcode");
        this.errmsg = jsonObject.getString("errmsg");
        this.accessToken = jsonObject.getString("access_token");
        this.expiresIn = jsonObject.getIntValue("expires_in");
    }

    public int getErrcode() {
        return errcode;
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

    @Override
    public String toString() {
        return "AccessToken{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", expiresIn=" + expiresIn +
                '}';
    }
}
