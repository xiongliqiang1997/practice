package com.qiang.practice.mapper;

import com.qiang.practice.model.SysUserMsgList;
import com.qiang.practice.model.vo.SysUserMsgListUserNameMsgVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SysUserMsgListMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysUserMsgList record);

    int insertSelective(SysUserMsgList record);

    SysUserMsgList selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUserMsgList record);

    int updateByPrimaryKey(SysUserMsgList record);

    /**
     * 获取有过消息交流的好友列表
     * @return
     */
    List<SysUserMsgListUserNameMsgVO> frendList(Long sysUserId);

    /**
     * 根据sysUserId和anotherUserId获取记录数
     * @param sysUserId
     * @param anotherUserId
     * @return
     */
    Long getByTwoUserId(@Param("sysUserId") Long sysUserId,
                       @Param("anotherUserId") Long anotherUserId);

    /**
     * 将两人的聊天列表的最后消息时间置为最新的
     * @param sysUserId
     * @param toUserId
     */
    void updateLastMsgDateToNewByTwoUserId(@Param("sysUserId") Long sysUserId,
                                           @Param("toUserId") Long toUserId,
                                           @Param("lastMsgDate") Date lastMsgDate);

    /**
     * 没有则添加，存在则不添加
     * @param sysUserMsgList
     */
    void insertIfAbsent(SysUserMsgList sysUserMsgList);

    /**
     * 发完消息后,更新双方的左侧最近聊天列表
     * @param sysUserMsgList
     */
    void insertAfterSendMsg(SysUserMsgList sysUserMsgList);
}