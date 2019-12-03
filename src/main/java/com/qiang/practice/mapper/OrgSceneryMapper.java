package com.qiang.practice.mapper;

import com.qiang.practice.model.OrgScenery;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrgSceneryMapper {
    int deleteByPrimaryKey(Long id);

    int add(OrgScenery record);

    int insertSelective(OrgScenery record);

    OrgScenery getById(Long id);

    int updateByPrimaryKeySelective(OrgScenery record);

    int updateByPrimaryKey(OrgScenery record);

    /**
     * 将当前最大的排序号查出来
     * @return
     */
    Integer getMaxOrderby();

    /**
     * 根据list里的id，将这些企业风光的有效字段设置为0，即无效
     * @param idList
     * @return
     */
    int updateValidToZeroByIdList(@Param(value = "idList") List<Long> idList);

    /**
     * 根据条件分页查询
     * @param paramMap
     * @return
     */
    List<Map<String, Object>> getListByCondition(Map<String, Object> paramMap);

    /**
     * 获取需要排序的企业风光数据
     * @return
     */
    List<Map<String, Object>> getSortData();

    /**
     * 根据id修改orderby字段
     * @param stringObjectMap
     * @return
     */
    int sort(Map<String, Object> stringObjectMap);
}