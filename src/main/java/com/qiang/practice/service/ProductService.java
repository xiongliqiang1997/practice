package com.qiang.practice.service;

import com.qiang.practice.model.vo.ProductImgVO;
import com.qiang.practice.model.vo.ProductInboundRecordAndDetailVO;
import com.qiang.practice.utils.response.R;

import java.util.List;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/8/21
 * @Description: TODO
 */
public interface ProductService {
    /**
     * 分页获取商品，若查询条件不为null，则为 分页且条件查询
     * @param paramMap
     * @return
     */
    R getByPage(Map<String, Object> paramMap);

    /**
     * 新增或编辑商品
     * @param productImgVO
     * @return
     */
    R addOrUpdate(ProductImgVO productImgVO);

    /**
     * 根据id查单个商品的详细信息（内含图片列表）
     * @param id
     * @return
     */
    R getById(Long id);

    /**
     * 根据id数组删除选中的商品
     * @param idList
     * @return
     */
    R removeByIdList(List<Long> idList);

    /**
     * 根据id数组发布选中的商品
     * @param idList
     * @return
     */
    R publishByIdList(List<Long> idList);

    /**
     * 根据id数组撤销选中的商品
     * @param idList
     * @return
     */
    R revokeByIdList(List<Long> idList);

    /**
     * 根据id数组获取商品（内含图片列表）
     * @param idListStr
     * @return
     */
    R getByIdListStr(String idListStr);

    /**
     * 入库
     * @param productInboundRecordAndDetailVO
     * @return
     */
    R inbound(ProductInboundRecordAndDetailVO productInboundRecordAndDetailVO);

    Integer getProductStock(Long productId);

}
