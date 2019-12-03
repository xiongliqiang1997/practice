package com.qiang.practice.service;

import com.qiang.practice.utils.response.R;

/**
 * @Author: CLQ
 * @Date: 2019/8/26
 * @Description: TODO
 */
public interface SysAreaService {
    /**
     * 根据地区id获取地区信息
     * @param id
     * @return
     */
    R getById(Integer id);

    /**
     * 根据上级地区id获取地区列表
     * @param pId
     * @return
     */
    R getByPId(Integer pId);
}
