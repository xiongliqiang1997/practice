package com.qiang.practice.mapper;

import com.qiang.practice.model.SysUserStatus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserStatusMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysUserStatus record);

    int insertSelective(SysUserStatus record);

    SysUserStatus selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUserStatus record);

    int updateByPrimaryKey(SysUserStatus record);

    /**
     * 设置用户的登陆状态(0离线 1在线)
     * @param sysUserId
     * @param status
     */
    void setUserStatus(@Param("sysUserId") Long sysUserId,
                       @Param("status") Byte status);

    /**
     * 批量删除
     * @param idList
     */
    void removeBySysUserList(List<Long> idList);
}