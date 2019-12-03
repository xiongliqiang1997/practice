package com.qiang.practice.mapper;

import com.qiang.practice.model.OrgImpression;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrgImpressionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrgImpression record);

    int insertSelective(OrgImpression record);

    OrgImpression selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrgImpression record);

    int updateByPrimaryKeyWithBLOBs(OrgImpression record);

    int updateByPrimaryKey(OrgImpression record);

    /**
     * 根据条件查询对应的企业印象
     * @param paramMap
     * @return
     */
    List<Map<String, Object>> getListByCondition(Map<String, Object> paramMap);

    /**
     * 批量删除企业印象,将有效字段置为0
     * @param idList
     * @return
     */
    int updateIsValidToZeroByIdList(@Param(value = "idList") List<Long> idList);

    /**
     * 批量发布企业印象,将发布字段置为1
     * @param idList
     * @return
     */
    int updateIsPublishToOneByIdList(@Param(value = "idList") List<Long> idList);

    /**
     * 批量撤销企业印象,将发布字段置为0
     * @param idList
     * @return
     */
    int updateIsPublishToZeroByIdList(@Param(value = "idList") List<Long> idList);

    /**
     * 查询所有已发布的企业印象
     * @return
     */
    List<OrgImpression> getByYear(@Param(value = "year") Integer year);

    /**
     * 获取有数据的年的列表
     * @return
     */
    List<String> getYearListWhichHaveData();
}