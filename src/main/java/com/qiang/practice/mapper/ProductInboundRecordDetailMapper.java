package com.qiang.practice.mapper;

import com.qiang.practice.model.ProductInboundRecordDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductInboundRecordDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProductInboundRecordDetail record);

    int insertSelective(ProductInboundRecordDetail record);

    ProductInboundRecordDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductInboundRecordDetail record);

    int updateByPrimaryKey(ProductInboundRecordDetail record);

    /**
     * 批量生成各个的商品入库数量
     * @param productInboundRecordDetailList
     */
    int batchInsert(@Param("productInboundRecordDetailList") List<ProductInboundRecordDetail> productInboundRecordDetailList,
                    @Param("productInboundRecordId") Long productInboundRecordId);

    /**
     * 根据入库记录id查询入库详情列表
     * @param productInboundRecordId
     * @return
     */
    List<ProductInboundRecordDetail> getByProductInboundRecordId(Long productInboundRecordId);
}