package com.qiang.practice.mapper;

import com.qiang.practice.model.PrimaryMember;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PrimaryMemberMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PrimaryMember record);

    int insertSelective(PrimaryMember record);

    PrimaryMember selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PrimaryMember record);

    int updateByPrimaryKeyWithBLOBs(PrimaryMember record);

    int updateByPrimaryKey(PrimaryMember record);

    /**
     * 查询，若各查询条件不为null，则为动态条件查询
     * @param paramMap
     * @return
     */
    List<Map<String, Object>> getByCondition(Map<String, Object> paramMap);

    /**
     * 查目前的最大排序号
     * @return
     */
    Integer getMaxOrderby();

    /**
     * 批量删除主要成员,将有效字段置为0
     * @param idList
     * @return
     */
    int updateIsValidToZeroByIdList(@Param(value = "idList")List<Long> idList);

    /**
     * 获取需要排序的主要成员数据
     * @return
     */
    List<Map<String, Object>> getSortData();

    /**
     * 根据id修改orderby字段
     * @param paramMap
     * @return
     */
    int sort(Map<String, Object> paramMap);
}