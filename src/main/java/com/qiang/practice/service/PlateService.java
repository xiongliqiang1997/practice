package com.qiang.practice.service;

import com.qiang.practice.model.Plate;
import com.qiang.practice.utils.response.R;
import com.qiang.practice.utils.response.Validator;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/8/1
 * @Description: TODO
 */
public interface PlateService {
    /**
     * 分页获取板块信息，若标题不为null，则为 分页且条件查询
     * @param paramMap
     * @return
     */
    R getByPage(Map<String, Object> paramMap);

    /**
     * 新建或编辑板块信息
     * @param plate
     * @return
     */
    R addOrUpdate(HttpServletRequest request, Plate plate);

    /**
     * 根据id查单个板块的详细信息
     * @param id
     * @return
     */
    R getById(Long id);

    /**
     * 根据id数组删除被选中的板块
     * @param idList
     * @return
     */
    R removeByIdList(List<Long> idList);

    /**
     * 根据id数组板块被选中的板块
     * @param idList
     * @return
     */
    R publishByIdList(List<Long> idList);

    /**
     * 根据id数组撤销被选中的板块
     * @param idList
     * @return
     */
    R revokeByIdList(List<Long> idList);

    /**
     * 查看板块名称是否已存在
     * @param plateName
     * @param id
     * @return
     */
    Validator checkPlateNameExist(String plateName, Long id);

    /**
     * 根据id获得需要排序的数据
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
     * 获取全部板块
     * @return
     */
    R getAll();
}
