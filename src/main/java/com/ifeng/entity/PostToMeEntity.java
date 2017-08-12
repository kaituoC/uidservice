package com.ifeng.entity;

import java.util.Map;

/**
 * Created by chang on 2017/8/12.
 */
public class PostToMeEntity {
    /**
     * 发起请求的组ID，每组一个秘钥（secret）
     */
    private int groupId;
    /**
     * 成员ID列表（消息接收者，多个接收者用'|'分隔，最多支持1000个）
     */
    private String toUser;
    /**
     * 部门ID列表，多个接收者用‘|’分隔，最多支持100个
     */
    private String toParty;
    /**
     * 标签ID列表，多个接收者用‘|’分隔，最多支持100个
     */
    private String toTag;
    /**
     * 消息类型，文本类型固定为：text
     */
    private String msgType;
    /**
     * 使用哪个应用发送此条消息
     */
    private String appType;
    /**
     * text 参数的 content 字段为发送文本消息的具体内容
     * 可以支持换行、以及A标签，即可打开自定义的网页（可参考以上示例代码）(注意：换行符请用转义过的\n)
     */
    private Map<String, String> text;
    /**
     * MD5(groupId + msgType + appType + secret)的值，用于验证组ID及相关权限
     */
    private String sign;

    public PostToMeEntity() {
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getToParty() {
        return toParty;
    }

    public void setToParty(String toParty) {
        this.toParty = toParty;
    }

    public String getToTag() {
        return toTag;
    }

    public void setToTag(String toTag) {
        this.toTag = toTag;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public Map<String, String> getText() {
        return text;
    }

    public void setText(Map<String, String> text) {
        this.text = text;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
