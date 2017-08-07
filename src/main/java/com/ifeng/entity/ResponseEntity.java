package com.ifeng.entity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by chang on 2017/8/3.
 */
public class ResponseEntity {
    private String msg = "ok";
    private String state = "over";
    private List<String> userList = new LinkedList<>();
    private long timeStamp = 0l;
    public ResponseEntity() {
        this.userList.add("a");
        this.userList.add("b");
        this.userList.add("c");
        this.timeStamp = System.currentTimeMillis();
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }

    /**
     * 没有get方法，则返回该类的实例的时候，实例会转化成json但是不会包含此参数
     * @return
     */
    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

}
