package com.qiang.practice.mapper;

import com.qiang.practice.model.SysTouristMsgList;
import com.qiang.practice.model.vo.SysTouristMsgListUserNameMsgVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysTouristMsgListMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysTouristMsgList record);

    int insertSelective(SysTouristMsgList record);

    SysTouristMsgList selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysTouristMsgList record);

    int updateByPrimaryKey(SysTouristMsgList record);

    /**
     * 批量插入，不存在则插入，存在则不插入
     * @param sysTouristMsgLists
     */
    void batchInsertIfAbsent(@Param("sysTouristMsgLists") List<SysTouristMsgList> sysTouristMsgLists);

    /**
     * 获取游客与客服的聊天列表
     * @param sysUser
     * @return
     */
    List<SysTouristMsgListUserNameMsgVO> friendList(@Param("sysUser") String sysUser,
                                                    @Param("isSysUser") boolean isSysUser);

    /**
     * 如果没有则增加
     * @param sysTouristMsgList
     */
    void insertIfAbsent(SysTouristMsgList sysTouristMsgList);

    /**
     * 将游客的最近聊天列表和聊天记录清空
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
     * 发完消息后,更新双方的左侧最近聊天列表
     * @param sysTouristMsgList
     */
    void insertAfterSendMsg(SysTouristMsgList sysTouristMsgList);
}