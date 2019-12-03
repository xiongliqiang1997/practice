package com.qiang.practice.service;

import com.qiang.practice.utils.response.WSResult;

import java.util.Date;
import java.util.List;

/**
 * @Author: CLQ
 * @Date: 2019/9/5
 * @Description: TODO
 */
public interface SysTouristMsgListService {
    /**
     * 为游客在聊天列表中添加客服
     * @param clientId
     */
    void createChatListWithCustomerService(String clientId);

    /**
     * 游客或客服加载最近聊天列表
     * @param clientId
     * @return
     */
    WSResult friendList(String clientId);

    /**
     * 将游客的聊天信息清空
     * @param clientId
     */
    void clearMsgInfo(String clientId);

    /**
     * 获取跟该用户聊过天的人的id列表
     * @param clientId
     * @return
     */
    List<String> getIdListWhoChatWithHim(String clientId);

    /**
     * 删除最近聊天列表记录
     * @param id
     */
    void remove(Long id);

    void insertAfterSendMsg(String clientId, String toUserId, Date lastMsgDate);
}
