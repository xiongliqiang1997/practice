package com.qiang.practice.service;

import com.qiang.practice.model.UserAddress;
import com.qiang.practice.utils.response.R;

import java.util.List;

/**
 * @Author: CLQ
 * @Date: 2019/8/22
 * @Description: TODO
 */
public interface UserAddressService {
    /**
     * 获取用户的收货地址列表
     * 若isDefaultAddress为true，则只查默认收货地址
     * @return
     */
    R getListBySysUser(Boolean isDefaultAddress);

    /**
     * 根据id获取某一个收货地址的详细信息
     * @param id
     * @return
     */
    R getById(Long id);

    /**
     * 新建或编辑收货地址
     * @param userAddress
     * @return
     */
    R addOrUpdate(UserAddress userAddress);

    /**
     * 根据id数组删除收货地址
     * @param idList
     * @return
     */
    R removeByIdList(List<Long> idList);
}
