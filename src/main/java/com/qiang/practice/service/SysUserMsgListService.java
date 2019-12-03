package com.qiang.practice.service;

import com.alibaba.fastjson.JSONObject;
import com.qiang.practice.utils.response.WSResult;

import java.util.Date;

/**
 * @Author: CLQ
 * @Date: 2019/9/5
 * @Description: TODO
 */
public interface SysUserMsgListService {

    /**
     * 获取好友列表
     * @return
     */
    WSResult frendList(Long sysUserId);

    /**
     * 用户点击发消息按钮
     * @param sysUserId
     * @param jsonObject
     */
    WSResult clickSendBtn(Long sysUserId, JSONObject jsonObject);

    /**
     * 删除最近聊天列表记录
     * @param id
     */
    void remove(Long id);

    void insertAfterSendMsg(Long sysUserId, Long toUserId, Date lastMsgDate);
}
