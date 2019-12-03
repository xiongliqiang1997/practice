package com.qiang.practice.mapper;

import com.qiang.practice.model.Plate;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PlateMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Plate record);

    int insertSelective(Plate record);

    Plate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Plate record);

    int updateByPrimaryKey(Plate record);

    /**
     * 根据动态条件查询对应的企业印象
     *
     * @param paramMap
     * @return
     */
    List<Map<String, Object>> getListByPage(Map<String, Object> paramMap);

    /**
     * 在已经有的最大排序号上加1
     *
     * @return
     */
    Integer getMaxOrderby();

    /**
     * 批量删除板块,将有效字段置为0
     *
     * @param idList
     * @return
     */
    int updateIsValidToZeroByIdList(@Param(value = "idList") List<Long> idList);

    /**
     * 批量启用板块,将发布字段置为1
     *
     * @param idList
     * @return
     */
    int updateIsPublishToOneByIdList(@Param(value = "idList") List<Long> idList);

    /**
     * 批量撤销板块,将发布字段置为0
     *
     * @param idList
     * @return
     */
    int updateIsPublishToZeroByIdList(@Param(value = "idList") List<Long> idList);

    /**
     * 获取该板块名的数量
     *
     * @param plateName
     * @param id
     * @return
     */
    int getPlateNameCount(@Param(value = "plateName") String plateName,
                          @Param(value = "id") Long id);

    /**
     * 获取需要排序的板块数据
     *
     * @return
     */
    List<Map<String, Object>> getSortData();

    /**
     * 根据id修改orderby字段
     *
     * @param paramMap
     * @return
     */
    int sort(Map<String, Object> paramMap);

    /**
     * 获取左侧菜单所需要的json格式数据
     *
     * @return
     */
    List<Map<String, Object>> getPlateNameMenu();

    /**
     * 获取全部板块列表
     * @return
     */
    List<Map<String, Object>> getPlateList();


}