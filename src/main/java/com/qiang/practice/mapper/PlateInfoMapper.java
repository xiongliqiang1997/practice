package com.qiang.practice.mapper;

import com.qiang.practice.model.PlateInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface PlateInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PlateInfo record);

    int insertSelective(PlateInfo record);

    PlateInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PlateInfo record);

    int updateByPrimaryKeyWithBLOBs(PlateInfo record);

    int updateByPrimaryKey(PlateInfo record);

    /**
     * 根据条件查询对应的信息发布列表
     * @param paramMap
     * @return
     */
    List<Map<String, Object>> getListByCondition(Map<String, Object> paramMap);

    /**
     * 根据板块id数组删除这些板块下的信息
     * @param idList
     */
    void updateIsValidToZeroByPlateIdList(@Param(value = "idList") List<Long> idList);

    /**
     * 根据id查询详细信息
     * @param id
     * @return
     */
    Map<String, Object> getDetailById(Long id);

    /**
     * 批量删除信息,将有效字段置为0
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
     * 根据年份、月份和板块id查询该板块在这一年这一月的发布数量
     * @param plateId
     * @param month
     * @param year
     * @return
     */
    int getPublishCountByMonth(@Param(value = "plate") Long plateId,
                               @Param(value = "month") String month,
                               @Param(value = "year") String year);

    /**
     * 根据id获取发布日期
     * @return
     */
    Date getPublishDateById(Long id);

    /**
     * 根据板块id获取该板块前四条信息
     * @param plate
     * @return
     */
    List<Map<String, Object>> getFirstFourInfoByPlate(@Param("plate") Long plate,
                                                      @Param("showNum") Integer showNum);

    /**
     * 获取各板块前四条信息
     * @return
     */
    List<Map<String, Object>> getFirstFourEveryPlate();
}