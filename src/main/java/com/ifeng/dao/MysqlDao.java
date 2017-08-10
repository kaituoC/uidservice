package com.ifeng.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chang on 2017/8/8.
 */
@Repository
public class MysqlDao {
    private Logger logger = LoggerFactory.getLogger(MysqlDao.class);

    @Value("${mysql.ip}")
    private String mysqlIp;

    @Value("${mysql.port}")
    private int mysqlPort;

    @Value("${mysql.db}")
    private String dbName;

    @Value("${mysql.user}")
    private String userName;

    @Value("${mysql.passwd}")
    private String passwd;
    private Connection connection;
    private Statement statement;

    private boolean getStatement() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            logger.info("成功加载MySQL驱动！");
            String url = "jdbc:mysql://" + mysqlIp + ":" + mysqlPort + "/" + dbName + "?useUnicode=true&characterEncoding=UTF-8&useSSL=true";
            this.connection = DriverManager.getConnection(url, userName, passwd);
            logger.info("创建数据库连接成功！");
            this.statement = connection.createStatement();
            logger.info("成功连接到数据库！");
        } catch (ClassNotFoundException e) {
            logger.error("找不到MySQL驱动！", e);
            return false;
        } catch (SQLException e) {
            logger.error("创建数据库连接失败！", e);
            return false;
        }
        return true;
    }

    public String getValueFromMysql(String sql) {
        boolean state = true;
        String value = "";
        if (this.statement == null) {
            state = getStatement();
            logger.info("首次获取MySQL数据库连接。");
        }
        if (state) {
            try {
                ResultSet resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    value = resultSet.getString(1);
                    logger.info(sql + " => " + value);
                } else {
                    logger.error("resultSet为空！");
                }
                resultSet.close();
            } catch (SQLException e) {
                logger.error("执行SQL失败：" + sql, e);
            }
        }
        return value;
    }

    public void close() {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                logger.error("statement关闭失败！");
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("connection关闭失败！");
            }
        }
    }

    public Map<String, String> getSystemConfMap() {
        Map<String, String> map  = new ConcurrentHashMap<>();
        boolean state = true;
        if (this.statement == null) {
            state = getStatement();
            logger.info("首次获取MySQL数据库连接。");
        }
        if (state) {
            try {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM system_conf");
                while (resultSet.next()) {
                    map.put(resultSet.getString("conf_name"), resultSet.getString("conf_value"));
                }
                resultSet.close();
            } catch (SQLException e) {
                logger.error("执行SQL失败：SELECT * FROM system_conf", e);
            }
        }
        return map;
    }
}
