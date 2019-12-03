package com.qiang.practice.mapper;

import com.qiang.practice.model.ProductInboundRecord;
import com.qiang.practice.model.vo.ProductInboundRecordAndDetailVO;

import java.util.List;
import java.util.Map;

public interface ProductInboundRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProductInboundRecord record);

    int insertSelective(ProductInboundRecord record);

    ProductInboundRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductInboundRecord record);

    int updateByPrimaryKey(ProductInboundRecord record);

    /**
     * 条件查询
     * @param paramMap
     * @return
     */
    List<ProductInboundRecordAndDetailVO> getListByCondition(Map<String, Object> paramMap);

    /**
     * 根据id获取入库详情(包括商品名称和第一张商品图片)
     * @param id
     * @return
     */
    ProductInboundRecordAndDetailVO getById(Long id);

    /**
     * 获取所有入库记录（包括入库详情）
     * @return
     */
    List<ProductInboundRecordAndDetailVO> getList();
}