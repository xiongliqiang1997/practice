package com.qiang.practice.service;

import com.qiang.practice.model.PrimaryMember;
import com.qiang.practice.utils.response.R;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/7/31
 * @Description: TODO
 */
public interface PrimaryMemberService {
    /**
     * 获取主要成员，若各查询条件不为null，则为 条件查询
     * @param paramMap
     * @return
     */
    R getByCondition(Map<String, Object> paramMap);

    /**
     * 新建或编辑主要成员
     * @param primaryMember
     * @return
     */
    R addOrUpdate(HttpServletRequest request, PrimaryMember primaryMember);

    /**
     * 根据id查单个成员的详细信息
     * @param id
     * @return
     */
    R getById(Long id);

    /**
     * 根据id数组删除被选中的主要成员
     * @param idList
     * @return
     */
    R removeByIdList(List<Long> idList);

    /**
     * 获取需要排序的主要成员数据
     * @return
     */
    R getSortData();

    /**
     * 保存排序
     * @param param
     * @return
     */
    R sort(Map<String, Object> param);
}
