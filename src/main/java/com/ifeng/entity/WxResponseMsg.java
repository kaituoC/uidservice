package com.ifeng.entity;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by chang on 2017/8/12.
 */
public class WxResponseMsg {
    private int errcode;
    private String errmsg;
    private String invaliduser;     // 不区分大小写，返回的列表都统一转为小写
    private String invalidparty;
    private String invalidtag;

    public WxResponseMsg() {
    }

    public WxResponseMsg(String wxResponseMsg) {
        JSONObject jsonObject =JSONObject.parseObject(wxResponseMsg);
        this.errcode = jsonObject.getInteger("errcode");
        this.errmsg = jsonObject.getString("errmsg");
        this.invaliduser = jsonObject.getString("invaliduser");
        this.invalidparty = jsonObject.getString("invalidparty");
        this.invalidtag = jsonObject.getString("invalidtag");
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getInvaliduser() {
        return invaliduser;
    }

    public void setInvaliduser(String invaliduser) {
        this.invaliduser = invaliduser;
    }

    public String getInvalidparty() {
        return invalidparty;
    }

    public void setInvalidparty(String invalidparty) {
        this.invalidparty = invalidparty;
    }

    public String getInvalidtag() {
        return invalidtag;
    }

    public void setInvalidtag(String invalidtag) {
        this.invalidtag = invalidtag;
    }
}
