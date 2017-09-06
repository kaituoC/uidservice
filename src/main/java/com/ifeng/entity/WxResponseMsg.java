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
        init(wxResponseMsg);
    }

    private void init(String wxResponseMsg) {
        JSONObject jsonObject = JSONObject.parseObject(wxResponseMsg);
        String tmp;
        this.errcode = jsonObject.getInteger("errcode");
        this.errmsg = jsonObject.getString("errmsg");
        tmp = jsonObject.getString("invaliduser");
        if (tmp == null) {
            this.invaliduser = "";
        } else {
            this.invaliduser = tmp;
        }
        tmp = jsonObject.getString("invalidparty");
        if (tmp == null) {
            this.invalidparty = "";
        } else {
            this.invalidparty = tmp;
        }
        tmp = jsonObject.getString("invalidtag");
        if (tmp == null) {
            this.invalidtag = "";
        } else {
            this.invalidtag = tmp;
        }

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
