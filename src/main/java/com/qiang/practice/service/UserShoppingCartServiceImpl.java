package com.qiang.practice.service;

import com.qiang.practice.base.Constants;
import com.qiang.practice.base.TipsConstants;
import com.qiang.practice.config.ServerConfig;
import com.qiang.practice.mapper.ProductMapper;
import com.qiang.practice.mapper.UserShoppingCartMapper;
import com.qiang.practice.model.UserShoppingCart;
import com.qiang.practice.model.vo.UserShoppingCartProductVO;
import com.qiang.practice.utils.HttpContextUtils;
import com.qiang.practice.utils.response.R;
import com.qiang.practice.utils.response.RUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/8/23
 * @Description: TODO
 */
@SuppressWarnings("all")
@Transactional
@Service
public class UserShoppingCartServiceImpl implements UserShoppingCartService {
    @Autowired
    private UserShoppingCartMapper userShoppingCartMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private ServerConfig serverConfig;

    @Override
    public R getListBySysUser(Map<String, Object> paramMap) {
        //获取用户的购物车列表
        List<UserShoppingCartProductVO> userShoppingCartProductVOList = userShoppingCartMapper.getListBySysUserAndCondition(redisService.findByToken(
                HttpContextUtils.getHttpServletRequest().getHeader(Constants.TOKEN_HEADER_PARAM)
        ).getId(), paramMap);
        //将redis中的各商品的库存设置进去, 同时将图片url拼上ip前缀
        for (UserShoppingCartProductVO userShoppingCartProductVO : userShoppingCartProductVOList) {
            userShoppingCartProductVO.setProductStock(productService.getProductStock(userShoppingCartProductVO.getProductId()));
            userShoppingCartProductVO.setFirstProductImgPath(
                    serverConfig.getImageUrl(userShoppingCartProductVO.getFirstProductImgPath()));
        }
        return R.ok(userShoppingCartProductVOList);
    }

    @Override
    public R add(UserShoppingCart userShoppingCart) {
        //判断购买数量是否大于库存
        if (userShoppingCart.getNum() > productService.getProductStock(userShoppingCart.getProductId())) {
            return R.error(TipsConstants.PRODUCT_STOCK_NOT_ENOUGH);
        }
        //判断是否买了0件
        if (userShoppingCart.getNum() < 1) {
            return R.error(TipsConstants.CONNOT_ADD_ZONE_PRODUCT_TO_SHOPPING_CART);
        }
        Long sysUserId = redisService.findByToken(
                HttpContextUtils.getHttpServletRequest().getHeader(Constants.TOKEN_HEADER_PARAM)
        ).getId();
        //判断购物车中是否已经有此商品了
        UserShoppingCart databaseUserShoppingCart = userShoppingCartMapper
                .getBySysUserAndProductId(sysUserId, userShoppingCart.getProductId());
        int rows = 0;
        if (databaseUserShoppingCart != null) {
            //已经有了，session和数据库中的商品数量叠加
            databaseUserShoppingCart.setNum(databaseUserShoppingCart.getNum() + userShoppingCart.getNum());
            rows = userShoppingCartMapper.updateByPrimaryKey(databaseUserShoppingCart);
        } else {
            userShoppingCart.setSysUser(sysUserId);
            userShoppingCart.setCreateDate(new Date());
            rows = userShoppingCartMapper.insert(userShoppingCart);
        }
        return RUtil.isSingleOk(rows, TipsConstants.ADD_SHOPPING_CART_SUCCESS,
                TipsConstants.ADD_SHOPPING_CART_FAILURE, null);
    }

    @Override
    public R synchronize(List<UserShoppingCart> sessionUserShoppingCartList) {
        //获取当前用户id
        Long sysUserId = redisService.findByToken(
                HttpContextUtils.getHttpServletRequest().getHeader(Constants.TOKEN_HEADER_PARAM))
                .getId();
        //拿到当前用户在数据库中的购物车列表
        List<UserShoppingCart> databaseUserShoppingCartList = userShoppingCartMapper.getListBySysUser(sysUserId);
        //将用户浏览器session中的购物车列表与数据库中的购物车列表进行比对
        //数据库中没有的就加入数据库，数据库中有但是数量不一致的就用session中的商品数量进行覆盖
        List<UserShoppingCart> addUserShoppingCartList = new ArrayList<>();
        List<UserShoppingCart> updateUserShoppingCartList = new ArrayList<>();
        int stockZoneFlag = 0;
        for (UserShoppingCart tempSessionUserShoppingCart : sessionUserShoppingCartList) {
            boolean flag = false;
            //循环数据库中的购物车列表进行对比
            for (UserShoppingCart tempDatabaseUserShoppingCart : databaseUserShoppingCartList) {
                if (tempDatabaseUserShoppingCart.getProductId() == tempSessionUserShoppingCart.getProductId()) {
                    //说明是同一件商品，然后判断商品的数量是否一致
                    if (tempDatabaseUserShoppingCart.getNum() == tempSessionUserShoppingCart.getNum()) {
                        //一致，表示数据库中有该数据，跳过
                        flag = true;
                        break;
                    } else {
                        //排除添加0件商品的情况
                        if (tempSessionUserShoppingCart.getNum() > 0) {
                            flag = true;
                            //不一致，用session中的商品数量进行覆盖
                            tempDatabaseUserShoppingCart.setNum(tempSessionUserShoppingCart.getNum());
                            updateUserShoppingCartList.add(tempDatabaseUserShoppingCart);
                        } else {
                            stockZoneFlag++;
                        }
                    }
                }
            }
            if (!flag) {
                if (tempSessionUserShoppingCart.getNum() > 0) {
                    //表示循环完之后，数据库中没有这一条商品，则需要加进去
                    tempSessionUserShoppingCart.setSysUser(sysUserId);
                    addUserShoppingCartList.add(tempSessionUserShoppingCart);
                    databaseUserShoppingCartList.add(tempSessionUserShoppingCart);
                } else {
                    stockZoneFlag ++;
                }
            }
        }
        //将需要修改的购物车信息进行批量修改
        if (!updateUserShoppingCartList.isEmpty()) {
            userShoppingCartMapper.batchUpdate(updateUserShoppingCartList);
        }
        if (!addUserShoppingCartList.isEmpty()) {
            if (userShoppingCartMapper.batchInsert(addUserShoppingCartList, sysUserId) == addUserShoppingCartList.size()) {
                if (stockZoneFlag > 0) {
                    return R.ok(TipsConstants.HAS_CLEAR_STOCK_ZONE_PRODUCT);
                } else {
                    return R.ok(TipsConstants.SYNCHRONIZE_USER_SHOPPING_CART_SUCCESS);
                }
            } else {
                return R.error(TipsConstants.ADD_SHOPPING_CART_FAILURE);
            }

        } else {
            if (stockZoneFlag > 0) {
                return R.ok(TipsConstants.HAS_CLEAR_STOCK_ZONE_PRODUCT);
            }
            return R.ok(TipsConstants.SYNCHRONIZE_USER_SHOPPING_CART_SUCCESS);
        }

    }

    @Override
    public R updateProductNum(Long productId, Integer num) {
        //根据商品id修改购物车中改商品的数量
        return RUtil.isOptionOk(userShoppingCartMapper.updateProductNum(
                productId, num, redisService.findByToken(
                        HttpContextUtils.getHttpServletRequest().getHeader(Constants.TOKEN_HEADER_PARAM))
                        .getId()
                )
        );
    }

    @Override
    public R removeByProductIdList(String productIdListStr) {
        //根据id数组删除购物车中的商品
        return RUtil.isBatchOk(userShoppingCartMapper.removeByProductIdList(productIdListStr,
                redisService.findByToken(HttpContextUtils.getHttpServletRequest().getHeader(Constants.TOKEN_HEADER_PARAM)).getId()),
                productIdListStr.split(",").length);
    }

}
