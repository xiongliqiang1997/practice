package com.qiang.practice.mapper;

import com.qiang.practice.model.SysUserMsg;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SysUserMsgMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysUserMsg record);

    int insertSelective(SysUserMsg record);

    SysUserMsg selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUserMsg record);

    int updateByPrimaryKeyWithBLOBs(SysUserMsg record);

    int updateByPrimaryKey(SysUserMsg record);

    /**
     * 根据好友id分页获取与好友的消息记录
     *
     * @param msgDateBottomLine
     * @param sysUserId
     * @param maxPageSize
     * @param anotherUserId
     * @return
     */
    List<SysUserMsg> getMsgPageList(@Param("msgDateBottomLine") Date msgDateBottomLine,
                                    @Param("sysUserId") Long sysUserId,
                                    @Param("anotherUserId") Long anotherUserId,
                                    @Param("maxPageSize") Integer maxPageSize,
                                    @Param("maxSecondIntervalOfTwoMsg") Integer maxSecondIntervalOfTwoMsg);

    /**
     * 将sysUserId的好友中anotherUserId的未读消息全部置为已读
     *
     * @param sysUserId
     * @param anotherUserId
     */
    void setHasReadedByTwoUserId(@Param("sysUserId") Long sysUserId,
                                 @Param("anotherUserId") Long anotherUserId);

    /**
     * 获取两人最后一次聊天的时间
     *
     * @param sysUserId
     * @param anotherUserId
     * @return
     */
    Date getLastMsgDate(@Param("sysUserId") Long sysUserId,
                        @Param("anotherUserId") Long anotherUserId);

    /**
     * 撤回消息
     *
     * @param id
     */
    void recallMsg(Long id);

    /**
     * 删除聊天记录
     * @param msgList
     * @param sysUserId
     */
    void removeMsgList(@Param("msgList") List<SysUserMsg> msgList,
                       @Param("sysUserId") Long sysUserId);

    /**
     * 查找这些消息是否被双方都删除了
     * @param msgList
     * @return
     */
    List<Boolean> isBothDeleteList(List<SysUserMsg> msgList);
}