package com.qiang.practice.service;

import com.qiang.practice.model.ProductType;
import com.qiang.practice.utils.response.R;

import java.util.List;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/8/21
 * @Description: TODO
 */
public interface ProductTypeService {
    /**
     * 分页获取商品分类，若查询条件不为null，则为 分页且条件查询
     * @param paramMap
     * @return
     */
    R getByPage(Map<String, Object> paramMap);

    /**
     * 获取树形菜单所需要的json格式数据
     * @param id
     * @return
     */
    R getTreeMenu(Long id);

    /**
     * 新建或编辑商品分类
     * @param productType
     * @return
     */
    R addOrUpdate(ProductType productType);

    /**
     * 根据id数组删除商品分类
     * @param idList
     * @return
     */
    R removeByIdList(List<Long> idList);

    /**
     * 根据id查单个商品分类的详细信息
     * @param id
     * @return
     */
    R getById(Long id);

    /**
     * 根据id获得需要排序的商品分类
     * @param id
     * @return
     */
    R getSortData(Long id);

    /**
     * 保存排序
     * @param param
     * @return
     */
    R sort(Map<String, Object> param);
}
