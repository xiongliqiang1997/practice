package com.qiang.practice.mapper;

import com.qiang.practice.model.SysLog;

import java.util.List;
import java.util.Map;

public interface SysLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysLog record);

    int insertSelective(SysLog record);

    SysLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysLog record);

    int updateByPrimaryKeyWithBLOBs(SysLog record);

    int updateByPrimaryKey(SysLog record);

    /**
     * 根据条件查询对应的日志列表
     * @param paramMap
     * @return
     */
    List<Map<String, Object>> getListByCondition(Map<String, Object> paramMap);

    /**
     * 获取所有日志列表
     * @return
     */
    List<Map<String, Object>> getList();
}