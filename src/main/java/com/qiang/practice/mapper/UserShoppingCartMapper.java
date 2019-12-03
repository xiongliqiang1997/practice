package com.qiang.practice.mapper;

import com.qiang.practice.model.UserShoppingCart;
import com.qiang.practice.model.vo.UserShoppingCartProductVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserShoppingCartMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserShoppingCart record);

    int insertSelective(UserShoppingCart record);

    UserShoppingCart selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserShoppingCart record);

    int updateByPrimaryKey(UserShoppingCart record);

    /**
     * 获取用户的购物车列表（包含条件筛选）
     * @param sysUser
     * @return
     */
    List<UserShoppingCartProductVO> getListBySysUserAndCondition (@Param("sysUser") Long sysUser,
                                                                 @Param("paramMap") Map<String, Object> paramMap);

    /**
     * 根据用户id和商品id获取记录
     * @param sysUserId
     * @param productId
     * @return
     */
    UserShoppingCart getBySysUserAndProductId(@Param("sysUserId") Long sysUserId,
                                              @Param("productId") Long productId);

    /**
     * 获取用户的购物车列表（不包含条件筛选）
     * @param sysUser
     * @return
     */
    List<UserShoppingCart> getListBySysUser(@Param("sysUser") Long sysUser);

    /**
     * 批量修改购物车信息
     * @param updateUserShoppingCartList
     * @return
     */
    int batchUpdate(@Param("updateUserShoppingCartList") List<UserShoppingCart> updateUserShoppingCartList);

    /**
     * 批量添加购物车
     * @param userShoppingCartList
     * @return
     */
    int batchInsert(@Param("userShoppingCartList") List<UserShoppingCart> userShoppingCartList,
                    @Param("sysUser") Long sysUser);

    /**
     * 根据商品id修改购物车中改商品的数量
     * @param productId
     * @param num
     * @return
     */
    int updateProductNum(@Param("productId") Long productId,
                         @Param("num") Integer num,
                         @Param("sysUser") Long sysUser);


    /**
     * 根据id数组删除购物车中的商品
     * @return
     */
    int removeByProductIdList(@Param("productIdListStr") String productIdListStr,
                              @Param("sysUser") Long sysUser);
}