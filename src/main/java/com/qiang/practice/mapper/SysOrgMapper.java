package com.qiang.practice.mapper;

import com.qiang.practice.model.SysOrg;
import com.qiang.practice.model.vo.SysOrgPNameVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysOrgMapper {
    int deleteByPrimaryKey(Long id);

    int addOneSysOrg(SysOrg record);

    int addOneSysOrgSelective(SysOrg record);

    SysOrg selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysOrg record);

    int updateByPrimaryKey(SysOrg record);

    /**
     * 分页查询数据列表
     * @param sysOrgId
     * @param skipRows
     * @param pageSize
     * @return
     */
    List<SysOrg> getSysOrgByPage(@Param(value = "sysOrgId") Long sysOrgId,
                                 @Param(value = "skipRows") Long skipRows,
                                 @Param(value = "pageSize") Long pageSize);

    /**
     * 根据上级机构id/机构名称/机构简介等条件查询有效的机构列表
     * 有哪个条件就带哪个条件然后分页，没条件则为分页查所有
     * @param paramMap
     * @return
     */
//    List<Map<String, Object>> getValidSysOrgByPIdOrOrgNameOrRemark(Map<String, Object> paramMap);
    String getValidSysOrgByIdOrPNameOrOrgNameOrRemark(Map<String, Object> paramMap);

    List<SysOrgPNameVO> getValidSysOrgByPIdOrOrgNameOrRemark1(Map<String, Object> paramMap);

    /**
     * 查目前最大的排序号
     * @return
     */
    Integer getMaxOrderby();

    /**
     * 批量删除组织机构,将每个机构及其子机构们的有效字段置为0
     * @param id
     * @return
     */
    int updateIsValidToZeroById(Long id);

    /**
     * 根据上级组织机构id获取节点
     * @param pId
     * @return
     */
    List<SysOrg> getSysOrgByPId(@Param(value = "pId") Long pId);

    /**
     * 获取机构名称的数量
     * @param orgName
     * @return
     */
    int getOrgNameCount(@Param(value = "orgName") String orgName,
                       @Param(value = "id") Long id);

    List<SysOrgPNameVO> getSysOrgPNameVOByPId(Long id);

    /**
     * 根据id或名字查询一条记录
     * @return
     */
    List<SysOrgPNameVO> getSysOrgByIdOrName(Map<String, Object> paramMap);

    /**
     * 根据条件查一共有多少条数据
     * @param paramMap
     * @return
     */
    Integer getTotalByPageWithCondition(Map<String, Object> paramMap);

    /**
     * 根据条件分页查询对应的组织机构
     * @param paramMap
     * @return
     */
    List<Map<String, Object>> getListByPage(Map<String, Object> paramMap);

    /**
     * 根据id查询其下第一层子机构
     * @param id
     * @return
     */
    List<Map<String, Object>> getFirstSonOrgByPId(@Param(value = "id") Long id);

    /**
     * 根据id修改orderby字段
     * @param paramMap
     */
    int sort(Map<String, Object> paramMap);

    /**
     * 根据id查单个组织机构的详细信息
     * @param id
     * @return
     */
    SysOrg getById(Long id);

}