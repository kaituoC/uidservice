package com.ifeng.entity;

import java.util.*;

/**
 * Created by chang on 2017/8/3.
 */
public class ResponseEntity {
    private String msg = "error";
    private int code = 1;
    /**
     * Map(userId, userName)
     */
    private Map<String, String> userMap = new HashMap<>();
    private Set<String> userSet = new HashSet<>();
    private long timeStamp = 0l;

    public ResponseEntity() {
//        this.userSet.add("a");
//        this.userSet.add("b");
//        this.userSet.add("c");
//        this.timeStamp = System.currentTimeMillis();
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Map<String, String> getUserMap() {
        return userMap;
    }

    public void setUserMap(Map<String, String> userMap) {
        this.userMap = userMap;
    }

    public Set<String> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<String> userSet) {
        this.userSet = userSet;
    }

    /**
     * 没有get方法，则返回该类的实例的时候，实例会转化成json但是不会包含此参数
     *
     * @return
     */
    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

}
