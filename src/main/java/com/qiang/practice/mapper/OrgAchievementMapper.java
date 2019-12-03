package com.qiang.practice.mapper;

import com.qiang.practice.model.OrgAchievement;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrgAchievementMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrgAchievement record);

    OrgAchievement selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrgAchievement record);

    int updateByPrimaryKeyWithBLOBs(OrgAchievement record);

    int updateByPrimaryKey(OrgAchievement record);

    /**
     * 根据条件查询
     * @param paramMap
     * @return
     */
    List<OrgAchievement> getByCondition(Map<String, Object> paramMap);

    /**
     * 批量删除,将有效字段置为0
     * @param idList
     * @return
     */
    int updateIsValidToZeroByIdList(@Param(value = "idList") List<Long> idList);

    /**
     * 批量发布企业成果,将发布字段置为1
     * @param idList
     * @return
     */
    int updateIsPublishToOneByIdList(@Param(value = "idList") List<Long> idList);

    /**
     * 批量撤销企业成果,将发布字段置为0
     * @param idList
     * @return
     */
    int updateIsPublishToZeroByIdList(@Param(value = "idList") List<Long> idList);

    /**
     * 获取所有已发布的企业成果
     * @return
     */
    List<OrgAchievement> getAll();
}