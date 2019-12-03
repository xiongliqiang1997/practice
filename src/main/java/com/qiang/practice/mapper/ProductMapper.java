package com.qiang.practice.mapper;

import com.qiang.practice.model.Product;
import com.qiang.practice.model.UserOrderProduct;
import com.qiang.practice.model.vo.ProductImgVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ProductMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKeyWithBLOBs(Product record);

    int updateByPrimaryKey(Product record);

    /**
     * 根据分类id将该分类以及其子分类下的商品删除
     * @param productTypeId
     */
    void updateIsValidToZeroByTypeId(Long productTypeId);

    /**
     * 根据条件查询商品
     * @param paramMap
     * @return
     */
    List<ProductImgVO> getListByCondition(Map<String, Object> paramMap);

    /**
     * 根据id查单个商品的详细信息（包括图片列表）
     * @param id
     * @return
     */
    ProductImgVO getById(Long id);

    /**
     * 批量删除商品,将有效字段置为0
     * @param idList
     * @return
     */
    int updateIsValidToZeroByIdList(@Param("idList") List<Long> idList);

    /**
     * 批量发布商品,将发布字段置为1
     * @param idList
     * @return
     */
    int updateIsPublishByIdList(@Param("idList") List<Long> idList,
                                @Param("isPublish") Byte isPublish);

    /**
     * 将该商品的浏览数量+1
     * @param id
     */
    void addViewNum(Long id);

    /**
     * 根据id数组获取商品（内含商品第一张图片）
     * @param idListStr
     * @return
     */
    List<ProductImgVO> getByIdList(String idListStr);

    /**
     * 批量为每件商品减库存
     * @param userOrderProductList
     * @return
     */
    int batchReduceProductStock(@Param("userOrderProductList") List<UserOrderProduct> userOrderProductList);

    /**
     * 检查订单中的各商品是否还存在
     * @param UserOrderProductList
     * @return
     */
    List<String> checkIsValidByIdList(@Param("UserOrderProductList") List<UserOrderProduct> UserOrderProductList);

    /**
     * 根据productId计算总金额
     * @param userOrderProductList
     * @return
     */
    BigDecimal getAmountTotal(List<UserOrderProduct> userOrderProductList);

    /**
     * 根据商品id字符串获取商品信息和商品第一张图片
     * @return
     */
    List<ProductImgVO> getJustProductInfoAndFirstImgByIdListStr(String idListStr);
}