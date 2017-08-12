package com.ifeng.service;

import com.ifeng.dao.MysqlDao;
import com.ifeng.entity.*;
import com.ifeng.utils.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by chang on 2017/8/11.
 */
@Service
public class WxService {
    private Logger logger = LoggerFactory.getLogger(WxService.class);

    private String checkResultMsg = "";


    @Autowired
    MysqlDao mysqlDao;

    public AccessToken getAccessToken(String tokenName) {
        String secret = "";
        if ("monitorToken".equals(tokenName)) {
            secret = "monitorSecret";
        } else if ("addrListToken".equals(tokenName)) {
            secret = "addrListSecret";
        } else {
            return null;
        }
        SystemConf systemConf = SystemConf.getSystemConf(mysqlDao);
        String wxUrlPrefix = systemConf.getSystemConfValue("wxUrlPrefix");
        String getToken = systemConf.getSystemConfValue("getToken");
        String corpID = systemConf.getSystemConfValue("CorpID");
        String corpSecret = systemConf.getSystemConfValue(secret);
        String url = wxUrlPrefix + getToken;
        String param = "corpid=" + corpID + "&corpsecret=" + corpSecret;
        String jsonStr = HttpRequest.sendGet(url, param);
        AccessToken accessToken = new AccessToken(jsonStr);
        return accessToken;
    }

    /**
     * 发送消息到微信服务
     * 如果部分接收人无权限或不存在，发送仍然执行，但会返回无效的部分
     * @param pe
     * @return
     */
    public WxResponseMsg sendMessage(PostToMeEntity pe) {
        WxResponseMsg rm = null;
        boolean state = checkPermission(pe.getGroupId(), pe.getMsgType(), pe.getAppType(), pe.getSign());
        if (state) {
            logger.info("prepare send message...");
            rm = messageSend(pe);
        }
        return rm;
    }

    /**
     * 调用微信服务发送消息
     * @param pe
     * @return
     */
    private WxResponseMsg messageSend(PostToMeEntity pe) {
        WxResponseMsg responseMsg = new WxResponseMsg();

        responseMsg.setErrcode(0);
        responseMsg.setErrmsg("send null");
        responseMsg.setInvaliduser(pe.getToUser());
        responseMsg.setInvalidparty(pe.getToParty());
        responseMsg.setInvalidtag(pe.getToTag());
        return responseMsg;
    }

    private boolean checkPermission(int groupId, String msgType, String appType, String sign) {
        if (sign == null || sign.equals("")) {
            this.checkResultMsg = "sign is null";
            logger.error("check permission failed: sign is null");
            return false;
        }
        String secret = mysqlDao.getValueFromMysql("SELECT conf_value FROM system_conf WHERE conf_name = \"group1secret\"");
        try {
            // 1 创建一个提供信息摘要算法的对象，初始化为md5算法对象
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            String inputStr = groupId + msgType + appType;

            // 2 将消息变成byte数组
            byte[] input = inputStr.getBytes();

            // 3 计算后获得字节数组,这就是那128位了
            byte[] buff = messageDigest.digest(input);

            // 4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
            String md5str = bytesToHex(buff);
            if (sign.equals(md5str)) {
                this.checkResultMsg = "check permission success!";
                logger.info("check permission success: groupId=" + groupId + ",msgType=" + msgType + ",appType=" + appType);
                return true;
            }
            logger.error("check permission failed: groupId=" + groupId + ",msgType=" + msgType + ",appType=" + appType
                    + ",input sign=" + sign + ",expect sign=" + md5str);
        } catch (NoSuchAlgorithmException e) {
            this.checkResultMsg = "check permission failed!";
            logger.error("check permission failed!", e);
            return false;
        }
        return false;
    }

    /**
     * 二进制转十六进制
     *
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer md5str = new StringBuffer();
        // 把数组每一字节换成16进制连成md5字符串
        int digital;
        for (int i = 0; i < bytes.length; i++) {
            digital = bytes[i];

            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        return md5str.toString().toUpperCase();
    }
}
