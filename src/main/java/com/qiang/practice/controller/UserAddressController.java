package com.qiang.practice.controller;

import com.qiang.practice.model.UserAddress;
import com.qiang.practice.service.UserAddressService;
import com.qiang.practice.utils.response.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: CLQ
 * @Date: 2019/8/22
 * @Description: TODO
 */
@Api(tags = "用户收货地址")
@RestController
public class UserAddressController {
    @Autowired
    private UserAddressService userAddressService;

    /**
     * 获取用户的收货地址列表
     * @return
     */
    @ApiOperation("获取用户的收货地址列表，若url后面跟上 ?isDefaultAddress=true，则只查默认收货地址")
    @RequestMapping(value = "/api/userAddresss", method = RequestMethod.GET)
    public R getListBySysUser(@ApiParam(value = "是否查默认收货地址  为true则查默认收货地址 为false或不传均为查所有收货地址", example = "true")
                                  @RequestParam(required = false) Boolean isDefaultAddress) {
        return userAddressService.getListBySysUser(isDefaultAddress);
    }

    /**
     * 根据id获取某一个收货地址的详细信息
     * @param id
     * @return
     */
    @ApiOperation("根据id获取某一个收货地址的详细信息")
    @RequestMapping(value = "/api/userAddresss/{id}", method = RequestMethod.GET)
    public R getById(@PathVariable("id") Long id) {
        return userAddressService.getById(id);
    }

    /**
     * 新建/编辑收货地址
     * @param userAddress
     * @return
     */
    @ApiOperation("新建/编辑收货地址  id空：新建    id非空：编辑")
    @RequestMapping(value = "/api/userAddresss", method = RequestMethod.POST)
    public R addOrUpdate(@RequestBody UserAddress userAddress) {
        return userAddressService.addOrUpdate(userAddress);
    }

    /**
     * 根据id数组删除收货地址
     * @param idList
     * @return
     */
    @ApiOperation("根据id数组删除收货地址（数据格式：1,2,3），若删除单个，则只传一个id即可")
    @RequestMapping(value = "/api/userAddresss/{ids}", method = RequestMethod.DELETE)
    public R removeByIdList(@ApiParam(value = "要删除的收货地址的id字符串", example = "1,2")
                                @PathVariable("ids") List<Long> idList) {
        return userAddressService.removeByIdList(idList);
    }
}
