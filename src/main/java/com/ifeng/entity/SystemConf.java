package com.ifeng.entity;

import com.ifeng.dao.MysqlDao;

import java.util.Map;

/**
 * Created by chang on 2017/8/11.
 */
public class SystemConf {
    Map<String, String> systemConfMap;
    private static SystemConf systemConf;
    private MysqlDao mysqlDao;
    private SystemConf(MysqlDao mysqlDao) {
        this.mysqlDao = mysqlDao;
        this.systemConfMap = getCongFromMysql();
    }

    private Map<String, String> getCongFromMysql() {
        Map<String, String> map = mysqlDao.getSystemConfMap();
        return map;
    }

    public static synchronized SystemConf getSystemConf(MysqlDao mysqlDao) {
        if (systemConf == null) {
            systemConf = new SystemConf(mysqlDao);
        }
        return systemConf;
    }

    private Map<String, String> getSystemConfMap() {
        return systemConfMap;
    }

    public String getSystemConfValue(String key) {
        return this.systemConfMap.get(key);
    }
}
