package com.qiang.practice.utils;

import com.qiang.practice.base.Constants;
import com.qiang.practice.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: CLQ
 * @Date: 2019/9/26
 * @Description: TODO
 */
@Component
public class MsgUtils {

    @Autowired
    private RedisService redisService;

    /**
     * 根据消息类型判断是否是文字消息
     * @param msgType
     * @return
     */
    public static boolean isTextMsg(String msgType) {
        return StringUtils.equals("text", msgType);
    }

    /**
     * 根据消息类型判断是否是图片消息
     * @param msgType
     * @return
     */
    public static boolean isImageMsg(String msgType) {
        return StringUtils.contains(msgType, "image/");
    }

    public static boolean isSysUser(String clientId, String anotherUserId) {
        return NumUtil.isInteger(clientId) && NumUtil.isInteger(anotherUserId);
    }

    /**
     * 获取消息可读状态
     * @param sysUserId
     * @param toUserId
     * @return
     */
    public byte getMsgIsRead(String sysUserId, String toUserId) {
        return isChatWithClient(sysUserId, toUserId) ? (byte)1 : (byte)0;
    }

    /**
     * 判断接收消息的一方是否正在呆在本客户端的聊天页面上
     * @param sysUserId
     * @param toUserId
     * @return
     */
    public boolean isChatWithClient(String sysUserId, String toUserId) {
        Object heChatWithWho = redisService.hget(Constants.USER_CHAT_WITH_WHO, toUserId);
        return (heChatWithWho != null && StringUtils.equals(sysUserId, heChatWithWho.toString()));
    }
}
