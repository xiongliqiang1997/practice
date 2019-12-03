package com.qiang.practice.controller;

import com.qiang.practice.model.UserShoppingCart;
import com.qiang.practice.service.UserShoppingCartService;
import com.qiang.practice.utils.response.R;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/8/23
 * @Description: TODO
 */
@Api(tags = "用户购物车")
@RestController
public class UserShoppingCartController {
    @Autowired
    private UserShoppingCartService userShoppingCartService;

    /**
     * 获取用户的购物车列表，若查询条件不为空，则为条件查询
     * @param paramMap
     * @return
     */
    @ApiOperation("获取用户的购物车列表，若商品分类id不为空，则查购物车中该分类下的商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productName", value = "商品名称", example = "早秋新款xxx……", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/api/userShoppingCarts", method = RequestMethod.GET)
    public R getListBySysUser(@ApiParam(hidden = true) @RequestParam Map<String, Object> paramMap) {
        return userShoppingCartService.getListBySysUser(paramMap);
    }

    /**
     * 添加商品到购物车
     * @param userShoppingCart 用户session中的购物车列表
     * @return
     */
    @ApiOperation("添加商品到购物车")
    @RequestMapping(value = "/api/userShoppingCarts", method = RequestMethod.POST)
    public R add(@RequestBody UserShoppingCart userShoppingCart) {
        return userShoppingCartService.add(userShoppingCart);
    }

    /**
     * 将浏览器session中的购物车列表同步到数据库
     * @param sessionUserShoppingCartList session中的购物车列表
     * @return
     */
    @ApiOperation("将浏览器session中的购物车列表同步到数据库")
    @RequestMapping(value = "/api/userShoppingCarts/session", method = RequestMethod.POST)
    public R synchronize(@RequestBody List<UserShoppingCart> sessionUserShoppingCartList) {
        return userShoppingCartService.synchronize(sessionUserShoppingCartList);
    }

    /**
     * 根据商品id修改购物车中的某商品数量
     * @param productId
     * @return
     */
    @ApiOperation("根据商品id修改购物车中的某商品数量")
    @RequestMapping(value = "/api/userShoppingCarts/productNum/{productId}", method = RequestMethod.GET)
    public R addProductNum(@PathVariable("productId") Long productId,
                           @ApiParam(value = "商品数量", required = true) @RequestParam Integer num) {
        return userShoppingCartService.updateProductNum(productId, num);
    }

    /**
     * 根据id数组删除购物车中的商品
     * @param productIdListStr
     * @return
     */
    @ApiOperation("根据id数组删除购物车中的商品")
    @RequestMapping(value = "/api/userShoppingCarts/{productIds}", method = RequestMethod.DELETE)
    public R removeByProductIdList(@PathVariable("productIds") String productIdListStr) {
        return userShoppingCartService.removeByProductIdList(productIdListStr);
    }

}
