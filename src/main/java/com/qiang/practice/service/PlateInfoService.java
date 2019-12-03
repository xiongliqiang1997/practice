package com.qiang.practice.service;

import com.qiang.practice.model.PlateInfo;
import com.qiang.practice.utils.response.R;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/8/1
 * @Description: TODO
 */
public interface PlateInfoService {
    /**
     * 获取左侧菜单所需要的json格式数据
     * @return
     */
    R getMenu();

    /**
     * 分页获取信息发布情况，若各查询条件不为null，则为 分页且条件查询
     * @param paramMap
     * @return
     */
    R getByPage(Map<String, Object> paramMap);

    /**
     * 新建或编辑信息
     * @param plateInfo
     * @return
     */
    R addOrUpdate(HttpServletRequest request, PlateInfo plateInfo);

    /**
     * 根据id查单个信息
     * @param id
     * @return
     */
    R getById(HttpServletRequest request, Long id);

    /**
     * 根据id数组删除被选中的信息
     * @param idList
     * @return
     */
    R removeByIdList(List<Long> idList);

    /**
     * 根据id数组发布被选中的信息
     * @param idList
     * @return
     */
    R publishByIdList(List<Long> idList);

    /**
     * 根据id数组撤销被选中的信息
     * @param idList
     * @return
     */
    R revokeByIdList(List<Long> idList);

    /**
     * 获取各板块的前四个信息
     * @return
     */
    R getFirstFourEveryPlate();
}
