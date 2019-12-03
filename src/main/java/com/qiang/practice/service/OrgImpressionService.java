package com.qiang.practice.service;

import com.qiang.practice.model.OrgImpression;
import com.qiang.practice.utils.response.R;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/7/9
 * @Description: TODO
 */
public interface OrgImpressionService {
    /**
     * 分页获取企业印象，若标题不为null，则为 分页且条件查询
     * @param paramMap
     * @return
     */
    R getOrgImpressionByPage(Map<String, Object> paramMap);

    /**
     * 新建或编辑企业印象
     * @param orgImpression
     * @return
     */
    R addOrUpdate(HttpServletRequest request, OrgImpression orgImpression);

    /**
     * 根据id查单个企业印象的详细信息
     * @param id
     * @return
     */
    R getById(Long id);

    /**
     * 根据id数组删除被选中的企业印象
     * @param idList
     * @return
     */
    R removeByIdList(List<Long> idList);

    /**
     * 根据id数组发布被选中的企业印象
     * @param idList
     * @return
     */
    R publishByIdList(List<Long> idList);

    /**
     * 根据id数组撤销被选中的企业印象
     * @param idList
     * @return
     */
    R revokeByIdList(List<Long> idList);

    /**
     * 获取某一年已发布的企业印象
     * @return
     */
    R getByYear(Integer year);

    /**
     * 获取有数据的年的列表
     * @return
     */
    R getYearListWhichHaveData();
}
