package com.qiang.practice.service;

import com.qiang.practice.model.UserShoppingCart;
import com.qiang.practice.utils.response.R;

import java.util.List;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/8/23
 * @Description: TODO
 */
public interface UserShoppingCartService {
    /**
     * 获取用户的购物车列表
     * @return
     */
    R getListBySysUser(Map<String, Object> paramMap);

    /**
     * 添加商品到购物车
     * @param userShoppingCart
     * @return
     */
    R add(UserShoppingCart userShoppingCart);

    /**
     * 将浏览器session中的购物车列表同步到数据库
     * @param sessionUserShoppingCartList session中的购物车列表
     * @return
     */
    R synchronize(List<UserShoppingCart> sessionUserShoppingCartList);

    /**
     * 根据商品id修改购物车中改商品的数量
     * @param productId
     * @param num
     * @return
     */
    R updateProductNum(Long productId, Integer num);

    /**
     * 根据id数组删除购物车中的商品
     * @param productIdListStr
     * @return
     */
    R removeByProductIdList(String productIdListStr);

}
