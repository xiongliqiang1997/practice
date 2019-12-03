package com.qiang.practice.service;

import com.alibaba.fastjson.JSONObject;
import com.qiang.practice.model.FileInfo;
import com.qiang.practice.model.SysTouristMsg;
import com.qiang.practice.model.SysUserMsg;
import com.qiang.practice.model.vo.MsgInfo;
import com.qiang.practice.utils.response.WSResult;

import java.util.List;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/9/6
 * @Description: TODO
 */
public interface SysTouristMsgService {
    /**
     * 根据客服id分页获取与客服的消息记录
     *
     * @param clientId
     * @param jsonObject
     * @return
     */
    WSResult getMsgPageList(String clientId, JSONObject jsonObject);

    /**
     * 将消息存到数据库
     *
     * @param clientId
     * @param toUserId
     * @param msgInfo
     * @param sign
     * @param uploadFileInfoMap
     * @return
     */
    SysTouristMsg sendMsg(String clientId, String toUserId, MsgInfo msgInfo, String sign, Map<String, Map<String, FileInfo>> uploadFileInfoMap);

    /**
     * 将anotherUserId发给clientId的消息全部置为已读
     *
     * @param clientId
     * @param anotherUserId
     */
    void updateAllMsgToHasReaded(String clientId, String anotherUserId);

    /**
     * 将消息存进数据库
     *
     * @param sysTouristMsg
     */
    void insert(SysTouristMsg sysTouristMsg);

    /**
     * 撤回消息
     *
     * @param id
     */
    void recallMsg(Long id);

    /**
     * 删除聊天记录
     *
     * @param msgList
     * @param sysUserId
     */
    List<SysUserMsg> removeMsgList(List<SysUserMsg> msgList, String sysUserId);
}
