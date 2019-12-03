package com.qiang.practice.mapper;

import com.qiang.practice.model.SysTouristMsg;
import com.qiang.practice.model.SysUserMsg;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SysTouristMsgMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysTouristMsg record);

    int insertSelective(SysTouristMsg record);

    SysTouristMsg selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysTouristMsg record);

    int updateByPrimaryKeyWithBLOBs(SysTouristMsg record);

    int updateByPrimaryKey(SysTouristMsg record);

    /**
     * 将该游客与该客服的未读消息置为已读
     * @param clientId
     * @param anotherUserId
     */
    void setHasReadedByTwoUserId(@Param("clientId") String clientId,
                                 @Param("anotherUserId") String anotherUserId);

    /**
     * 根据客服id分页获取与该客服的消息记录
     * @param msgDateBottomLine
     * @param sysUserId
     * @param anotherUserId
     * @param maxPageSize
     * @param maxSecondIntervalOfTwoMsg
     * @return
     */
    List<SysTouristMsg> getMsgPageList(@Param("msgDateBottomLine") Date msgDateBottomLine,
                                       @Param("sysUserId") String sysUserId,
                                       @Param("anotherUserId") String anotherUserId,
                                       @Param("maxPageSize") Integer maxPageSize,
                                       @Param("maxSecondIntervalOfTwoMsg") Integer maxSecondIntervalOfTwoMsg);

    /**
     * 撤回消息
     * @param id
     */
    void recallMsg(Long id);

    /**
     * 删除聊天记录
     * @param msgList
     * @param sysUserId
     */
    void removeById(@Param("msgList") List<SysUserMsg> msgList,
                    @Param("sysUserId") String sysUserId);

    /**
     * 查找这些消息是否被双方都删除了
     * @param msgList
     * @return
     */
    List<Boolean> isBothDeleteList(List<SysUserMsg> msgList);
}