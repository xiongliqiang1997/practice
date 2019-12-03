package com.qiang.practice.service;

import com.alibaba.fastjson.JSONObject;
import com.qiang.practice.model.FileInfo;
import com.qiang.practice.model.SysUserMsg;
import com.qiang.practice.model.vo.MsgInfo;
import com.qiang.practice.utils.response.WSResult;

import java.util.List;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/9/3
 * @Description: TODO
 */
public interface SysUserMsgService {

    /**
     * 根据好友id分页获取与好友的消息记录
     * @param sysUserId
     * @param jsonObject
     * @return
     */
    WSResult getMsgPageList(Long sysUserId, JSONObject jsonObject);

    /**
     * 将消息存到数据库
     *
     * @param sysUserId
     * @param toUserId
     * @param msgInfo
     * @param sign
     * @param uploadFileInfoMap
     */
    SysUserMsg sendMsg(Long sysUserId, Long toUserId, MsgInfo msgInfo, String sign, Map<String, Map<String, FileInfo>> uploadFileInfoMap);

    /**
     * 将anotherUserId发给clientId的未读消息全部置为已读
     * @param clientId
     * @param anotherUserId
     */
    void updateAllMsgToHasReaded(Long clientId, Long anotherUserId);

    /**
     * 将消息存进数据库
     * @param sysUserMsg
     */
    void insert(SysUserMsg sysUserMsg);

    /**
     * 撤回消息
     * @param id
     */
    void recallMsg(Long id);

    /**
     * 删除聊天记录
     * @param msgList
     */
    List<SysUserMsg> removeMsgList(List<SysUserMsg> msgList, Long sysUserId);

}
