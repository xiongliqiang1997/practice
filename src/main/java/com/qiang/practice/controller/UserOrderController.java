package com.qiang.practice.controller;

import com.qiang.practice.model.UserOrder;
import com.qiang.practice.service.UserOrderService;
import com.qiang.practice.utils.response.R;
import com.qiang.practice.model.vo.UserOrderAndOrderProductVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/8/23
 * @Description: TODO
 */
@SuppressWarnings("all")
@Api(tags = "用户订单")
@RestController
public class UserOrderController {
    @Autowired
    private UserOrderService userOrderService;

    /**
     * 分页获取用户订单列表，若查询条件不为null，则为分页且条件查询
     * @param paramMap
     * @return
     */
    @ApiOperation("分页获取用户订单列表，若查询条件不为null，则为分页且条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页展示数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "productName", value = "商品名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "订单号", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "minAmountTotal", value = "最小订单价格", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "maxAmountTotal", value = "最大订单价格", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "amountTotal", value = "订单价格升降序 升：asc 降：desc", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "订单状态", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/api/userOrders", method = RequestMethod.GET)
    public R getByPage(@ApiParam(hidden = true) @RequestParam Map<String, Object> paramMap) {
        return userOrderService.getByPage(paramMap);
    }

    /**
     * 根据id字符串删除订单
     * @param idListStr
     * @return
     */
    @ApiOperation("根据id字符串删除订单")
    @RequestMapping(value = "/api/userOrders/{ids}", method = RequestMethod.DELETE)
    public R removeByIdList(@PathVariable("ids") String idListStr) {
        return userOrderService.removeByIdListStr(idListStr);
    }

    /**
     * 生成订单
     * @param orderAndOrderProductVO
     * @return
     */
    @ApiOperation("生成订单")
    @RequestMapping(value = "/api/userOrders", method = RequestMethod.POST)
    public R add(@RequestBody UserOrderAndOrderProductVO orderAndOrderProductVO) {
        return userOrderService.add(orderAndOrderProductVO);
    }

    /**
     * 获取订单详情
     *
     * @return
     */
    @ApiOperation("获取订单详情")
    @RequestMapping(value = "/api/userOrders/{id}", method = RequestMethod.GET)
    public R getById(@PathVariable("id") Long id) {
        return userOrderService.getById(id);
    }

    /**
     * 付款
     * @param userOrder
     * @return
     */
    @ApiOperation("付款")
    @RequestMapping(value = "/api/userOrders/pay", method = RequestMethod.POST)
    public R pay(@RequestBody UserOrder userOrder) {
        return userOrderService.pay(userOrder.getId(), userOrder.getCode());
    }
}
