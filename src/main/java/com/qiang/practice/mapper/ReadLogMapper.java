package com.qiang.practice.mapper;

import com.qiang.practice.model.ReadLog;
import org.apache.ibatis.annotations.Param;

public interface ReadLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ReadLog record);

    int insertSelective(ReadLog record);

    ReadLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReadLog record);

    int updateByPrimaryKey(ReadLog record);

    /**
     * 根据年份、月份和板块id查询该板块在这一年这一月的发布数量
     * @param plateId
     * @param month
     * @param year
     * @return
     */
    Integer getReadCountByMonth(@Param(value = "plate") Long plateId,
                                @Param(value = "month") String month,
                                @Param(value = "year") String year);
}