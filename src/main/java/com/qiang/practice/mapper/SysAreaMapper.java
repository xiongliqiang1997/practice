package com.qiang.practice.mapper;

import com.qiang.practice.model.SysArea;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAreaMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysArea record);

    int insertSelective(SysArea record);

    SysArea selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysArea record);

    int updateByPrimaryKey(SysArea record);

    /**
     * 根据上级地区id获取地区列表
     * @param pId
     * @return
     */
    List<SysArea> getByPId(@Param("pId") Integer pId);

    /**
     * 根据上级地区id获取地区列表(为获取收货地址详情使用的)
     * @param pId
     * @return
     */
    List<SysArea> getByPIdForUserAddressDetail(Integer pId);
}