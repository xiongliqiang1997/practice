package com.qiang.practice.mapper;

import com.qiang.practice.model.ProductImg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductImgMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProductImg record);

    int insertSelective(ProductImg record);

    ProductImg selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductImg record);

    int updateByPrimaryKey(ProductImg record);

    /**
     * 根据商品id查询图片
     * @param productId
     * @return
     */
    List<ProductImg> selectListByProductId(Long productId);

    /**
     * 批量添加商品图片
     * @param productImgList
     * @param productId
     */
    void batchInsert(@Param("productImgList") List<ProductImg> productImgList,
                     @Param("productId") Long productId);

    /**
     * 根据商品id将图片置为无效
     * @param productId
     */
    void updateIsValidToZeroByProductId(Long productId);

    /**
     * 根据商品id删除图片
     * @param productIdList
     */
    void updateIsValidToZeroByProductIdList(@Param("productIdList") List<Long> productIdList);
}