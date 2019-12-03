package com.qiang.practice.mapper;

import com.qiang.practice.model.UserOrderProduct;
import com.qiang.practice.model.vo.UserOrderProductLogisticsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserOrderProductMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserOrderProduct record);

    int insertSelective(UserOrderProduct record);

    UserOrderProduct selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserOrderProduct record);

    int updateByPrimaryKey(UserOrderProduct record);

    /**
     * 根据用户订单id查询订单商品列表
     * @param userOrderId
     * @return
     */
    List<UserOrderProductLogisticsVO> selectListByUserOrderId(Long userOrderId);

    /**
     * 为订单批量增加商品
     * @param userOrderProductList
     * @param userOrderId
     * @return
     */
    int batchInsert(@Param("userOrderProductList") List<UserOrderProduct> userOrderProductList,
                    @Param("userOrderId") Long userOrderId);

    /**
     * 根据订单id获取订单中的商品
     * @param userOrderId
     * @return
     */
    List<UserOrderProduct> getByUserOrderId(Long userOrderId);
}