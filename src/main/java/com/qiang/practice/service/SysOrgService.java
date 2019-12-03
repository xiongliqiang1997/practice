package com.qiang.practice.service;

import com.qiang.practice.model.SysOrg;
import com.qiang.practice.utils.response.R;
import com.qiang.practice.utils.response.Validator;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/7/9
 * @Description: TODO
 */
public interface SysOrgService {

    /**
     *  分页获取有效的组织机构，若上级组织机构id不为null，则分页查该机构下有效的子机构
     * @param paramMap
     * @return
     */
    R getByPage(Map<String, Object> paramMap);

    /**
     * 新建或编辑组织机构
     * @param sysOrg
     * @return
     */
    R addOrUpdate(HttpServletRequest request, SysOrg sysOrg);

    /**
     * 根据id数组删除被选中的组织机构
     * @param idList
     * @return
     */
    R removeByIdList(List<Long> idList);

    /**
     * 获取树形菜单所需要的json格式数据
     * @return
     */
    R getTreeMenu(Long id);
    R getMyTreeMenu(Long id);

    /**
     * 查看该机构名称是否已存在
     * @param orgName
     * @return
     */
    Validator checkOrgNameExist(String orgName, Long id);

    /**
     * 根据id查单个组织机构的详细信息
     * @param id
     * @return
     */
    SysOrg getById(Long id);

    /**
     * 根据id获得需要排序的数据
     * @param id
     * @return
     */
    R getSysOrgSortData(Long id);

    /**
     * 保存排序
     * @param param
     * @return
     */
    R sort(Map<String, Object> param);
}
