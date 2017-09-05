package com.ifeng.entity;

import java.util.Map;

/**
 * Created by chang on 2017/8/12.
 */
public class PostToWxEntity {
    /**
     * 成员ID列表（消息接收者，多个接收者用'|'分隔，最多支持1000个）
     */
    private String touser;
    /**
     * 部门ID列表，多个接收者用‘|’分隔，最多支持100个
     */
    private String toparty;
    /**
     * 标签ID列表，多个接收者用‘|’分隔，最多支持100个
     */
    private String totag;
    /**
     * 消息类型，文本类型固定为：text
     */
    private String msgtype;
    /**
     * 企业应用的id，整型。可在应用的设置页面查看
     */
    private int agentid;
    /**
     * text参数的content字段为发送文本消息的具体内容
     * 可以支持换行、以及A标签，即可打开自定义的网页（可参考以上示例代码）(注意：换行符请用转义过的\n)
     * 消息内容，最长不超过2048个字节
     */
    private Map<String, String> text;
    /**
     * 表示是否是保密消息，0表示否，1表示是，默认0
     */
    private int safe = 0;

    public PostToWxEntity() {
    }

    public PostToWxEntity(PostToMeEntity pe) {
        this.touser = pe.getToUser();
        this.toparty = pe.getToParty();
        this.totag = pe.getToTag();
        this.msgtype = pe.getMsgType();
        this.text = pe.getText();
//        postToWxEntity.setTouser(pe.getToUser());
//        postToWxEntity.setToparty(pe.getToParty());
//        postToWxEntity.setTotag(pe.getToTag());
//        postToWxEntity.setMsgtype(pe.getMsgType());
//        postToWxEntity.setText(pe.getText());
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getToparty() {
        return toparty;
    }

    public void setToparty(String toparty) {
        this.toparty = toparty;
    }

    public String getTotag() {
        return totag;
    }

    public void setTotag(String totag) {
        this.totag = totag;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public int getAgentid() {
        return agentid;
    }

    public void setAgentid(int agentid) {
        this.agentid = agentid;
    }

    public Map<String, String> getText() {
        return text;
    }

    public void setText(Map<String, String> text) {
        this.text = text;
    }

    public int getSafe() {
        return safe;
    }

    public void setSafe(int safe) {
        this.safe = safe;
    }

    @Override
    public String toString() {
        return "{" +
                "\"touser\":\"" + touser + "\"" +
                ", \"toparty\":\"" + toparty + "\"" +
                ", \"totag\":\"" + totag + "\"" +
                ", \"msgtype\":\"" + msgtype + "\"" +
                ", \"agentid\":" + agentid +
                ", \"text\":{\"content\":\"" + text.get("content") + "\"}" +
                ", \"safe\":" + safe +
                "}";
    }
}
