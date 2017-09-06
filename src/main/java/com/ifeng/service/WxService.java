package com.ifeng.service;

import com.ifeng.dao.MysqlDao;
import com.ifeng.dao.RedisDao;
import com.ifeng.entity.*;
import com.ifeng.utils.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Created by chang on 2017/8/11.
 */
@Service
public class WxService {
    private Logger logger = LoggerFactory.getLogger(WxService.class);

    private int checkResultCode = 0;
    private String checkResultMsg = "";


    @Autowired
    MysqlDao mysqlDao;

    @Autowired
    RedisDao redisDao;

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
     *
     * @param pe
     * @return
     */
    public WxResponseMsg sendMessage(PostToMeEntity pe) {
        WxResponseMsg rm = new WxResponseMsg();
        boolean state = checkPermission(pe.getGroupId(), pe.getMsgType(), pe.getAppType(), pe.getSign());
        if (state) {
            logger.info("prepare send message...");
            rm = messageSend(pe);
        } else {
            logger.error("checkPermission failed!");
            rm.setErrcode(this.checkResultCode);
            rm.setErrmsg(this.checkResultMsg);
        }
        return rm;
    }

    /**
     * 调用微信服务发送消息
     *
     * @param pe
     * @return
     */
    private WxResponseMsg messageSend(PostToMeEntity pe) {
        PostToWxEntity postToWxEntity = new PostToWxEntity(pe);
        postToWxEntity.setAgentid(Integer.parseInt(mysqlDao.getSystemConfMap().get(pe.getAppType() + "AgentId")));
        logger.info("PostToWxEntity is:" + postToWxEntity);
        //默认设置安全模式为 0 ：明文发送
        postToWxEntity.setSafe(0);
        //1.从 Redis 获取发送消息的 URL 地址
        String sendMessageURLPrefix = getSendMessageURLPrefixFormRedis("sendMessageURLPrefix");

        //2.从 Redis 获取 accessToken
        String accessToken = getAccessTokenFromRedis("monitor");
        logger.info("accessToken=" + accessToken);
        //3.发送消息体到微信服务
        String wxResponseMsg = HttpRequest.sendPost("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + accessToken, postToWxEntity.toString());
        logger.info("POST send over: " + wxResponseMsg);
        WxResponseMsg responseMsg = new WxResponseMsg(wxResponseMsg);

//        responseMsg.setErrcode(0);
//        responseMsg.setErrmsg("send null");
//        responseMsg.setInvaliduser(pe.getToUser());
//        responseMsg.setInvalidparty(pe.getToParty());
//        responseMsg.setInvalidtag(pe.getToTag());
        return responseMsg;
    }

    private String getAccessTokenFromRedis(String accessType) {
        String accessToken = redisDao.getStrValueByKey(accessType + "AccessToken");
        if ("".equals(accessToken) || accessToken == null) {
            accessToken = getAccessTokenFromWxServer(accessType);
            redisDao.setStringValueIntoRedis(accessType + "AccessToken", accessToken);
            redisDao.setKeyExpireSecond(accessType + "AccessToken", 7200);
        }
        return accessToken;
    }

    private String getAccessTokenFromWxServer(String accessType) {
        Map<String, String> confMap = mysqlDao.getSystemConfMap();
        String getTokenUrl = confMap.get("getTokenUrl");
        String corpId = confMap.get("CorpID");
        String corpSecret = confMap.get(accessType + "Secret");
        String param = "corpid=" + corpId + "&corpsecret=" + corpSecret;
        String jsonStr = HttpRequest.sendGet(getTokenUrl, param);
        AccessTokenJson accessTokenJson = new AccessTokenJson(jsonStr);
        return accessTokenJson.getAccessToken();
    }

    private String getSendMessageURLPrefixFormRedis(String sendMessageURLPrefix) {
        String prefix = redisDao.getStrValueByKey(sendMessageURLPrefix);
        if ("".equals(prefix) || prefix == null) {
            redisDao.setStringValueIntoRedis("sendMessageURLPrefix", getSendMessageURLPrefixFormMysql());
        }
        return prefix;
    }

    /**
     * 检查发送消息者是否有权限发送消息，权限检测失败则返回false
     *
     * @param groupId 发送消息的组id
     * @param msgType 发送消息的类型，默认是text
     * @param appType 发送消息的渠道（微信应用）
     * @param sign    =MD5(groupId + msgType + appType + secret)
     * @return
     */
    private boolean checkPermission(int groupId, String msgType, String appType, String sign) {
        if (sign == null || sign.equals("")) {
            this.checkResultCode = 1;
            this.checkResultMsg = "sign is null";
            logger.error("check permission failed: sign is null");
            return false;
        } else {
            sign = sign.toUpperCase();
            String secret = mysqlDao.getValueFromMysql("SELECT conf_value FROM system_conf WHERE conf_name = \"group1secret\"");
            try {
                // 1 创建一个提供信息摘要算法的对象，初始化为md5算法对象
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                String inputStr = groupId + msgType + appType + secret;

                // 2 将消息变成byte数组
                byte[] input = inputStr.getBytes();

                // 3 计算后获得字节数组,这就是那128位了
                byte[] buff = messageDigest.digest(input);

                // 4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
                String md5str = bytesToHex(buff);
                logger.info("sign=[" + sign + "]; md5str=[" + md5str + "]");
                if (sign.equalsIgnoreCase(md5str)) {
                    this.checkResultCode = 0;
                    this.checkResultMsg = "check permission success!";
                    logger.info("check permission success: groupId=" + groupId + ",msgType=" + msgType + ",appType=" + appType);
                    return true;
                } else {
                    this.checkResultCode = 3;
                    this.checkResultMsg = "sign is not right!";
                    logger.error("check permission failed: groupId=" + groupId + ",msgType=" + msgType + ",appType=" + appType
                            + ",input sign=" + sign + ",expect sign=" + md5str);
                    return false;
                }
            } catch (NoSuchAlgorithmException e) {
                this.checkResultCode = 2;
                this.checkResultMsg = "check permission failed!";
                logger.error("check permission failed!", e);
                return false;
            }
        }
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

    public String getSendMessageURLPrefixFormMysql() {
        String sendMessageURLPrefix = mysqlDao.getValueFromMysql("SELECT conf_value FROM system_conf WHERE conf_name = \"sendMessageURLPrefix\"");
        return sendMessageURLPrefix;
    }
}
