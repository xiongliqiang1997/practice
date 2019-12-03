package com.qiang.practice.service;

import com.qiang.practice.model.OrgAchievement;
import com.qiang.practice.utils.response.R;

import java.util.List;
import java.util.Map;

/**
 * @InterfaceName: OrgAchievementService
 * @Author: CLQ
 * @Date: 2019/8/20
 * @Description: TODO
 */
public interface OrgAchievementService {
    /**
     * 分页获取企业成果，若查询条件不为null，则为分页且条件查询
     * @param paramMap
     * @return
     */
    R getByPage(Map<String, Object> paramMap);

    /**
     * 新建或编辑企业成果
     * @param orgAchievement
     * @return
     */
    R addOrUpdate(OrgAchievement orgAchievement);

    /**
     * 根据id查单个企业成果的详细信息
     * @param id
     * @return
     */
    R getById(Long id);

    /**
     * 根据id数组删除被选中的企业成果
     * @param idList
     * @return
     */
    R removeByIdList(List<Long> idList);

    /**
     * 根据id数组发布被选中的企业成果
     * @param idList
     * @return
     */
    R publishByIdList(List<Long> idList);

    /**
     * 根据id数组撤销被选中的企业成果
     * @param idList
     * @return
     */
    R revokeByIdList(List<Long> idList);

    /**
     * 获取所有已发布的企业成果
     * @return
     */
    R getAll();
}
