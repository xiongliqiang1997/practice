package com.qiang.practice.service;

import com.qiang.practice.model.OrgScenery;
import com.qiang.practice.utils.response.R;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/7/10
 * @Description: TODO
 */
public interface OrgSceneryService {

    /**
     * 分页获取企业风光，若各查询条件不为null，则为分页且条件查询
     * @param paramMap
     * @return
     */
    R getByPage(Map<String, Object> paramMap);

    /**
     * 新建/编辑企业风光
     * @param orgScenery
     * @return
     */
    R addOrUpdate(HttpServletRequest request, OrgScenery orgScenery);

    /**
     * 根据id数组删除被选中的企业风光
     * @param idList
     * @return
     */
    R removeByIdList(List<Long> idList);

    /**
     * 根据id查单个企业风光的详细信息
     * @param id
     * @return
     */
    R getById(Long id);

    /**
     * 获取需要排序的企业风光数据
     * @return
     */
    R getSortData();

    /**
     * 保存排序
     * @param param
     * @return
     */
    R sort(Map<String, Object> param);

    /**
     * 获取全部企业风光列表
     * @return
     */
    R getAll();
}
