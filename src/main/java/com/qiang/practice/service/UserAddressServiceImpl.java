package com.qiang.practice.service;

import com.qiang.practice.base.Constants;
import com.qiang.practice.mapper.UserAddressMapper;
import com.qiang.practice.model.UserAddress;
import com.qiang.practice.utils.HttpContextUtils;
import com.qiang.practice.utils.response.R;
import com.qiang.practice.utils.response.RUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author: CLQ
 * @Date: 2019/8/22
 * @Description: TODO
 */
@SuppressWarnings("all")
@Transactional
@Service
public class UserAddressServiceImpl implements UserAddressService {
    @Autowired
    private UserAddressMapper userAddressMapper;
    @Autowired
    private RedisService redisService;

    @Override
    public R getListBySysUser(Boolean isDefaultAddress) {
        //获取用户的收货地址列表，若isDefaultAddress为true，则只查默认收货地址
        return R.ok(userAddressMapper.getListBySysUser(redisService.findByToken(
                HttpContextUtils.getHttpServletRequest().getHeader(Constants.TOKEN_HEADER_PARAM)
                ).getId(), isDefaultAddress));
    }

    @Override
    public R getById(Long id) {
        return R.ok(userAddressMapper.getByIdIncludeAreaList(id));
    }

    @Override
    public R addOrUpdate(UserAddress userAddress) {
        int rows = 0;
        //是否要将本地址设置为默认收货地址
        if (userAddress.getIsDefaultAddress() == (byte) 1) {
            //将之前的默认收货地址取消
            userAddressMapper.updateIsDefaultBySysUser( redisService.findByToken(
                    HttpContextUtils.getHttpServletRequest().getHeader(Constants.TOKEN_HEADER_PARAM))
                    .getId(), (byte)1, (byte)0 );
        }
        //如果id为null，表示为新增，否则为修改
        if (null == userAddress.getId()) {
            HttpContextUtils.getHttpServletRequest().setAttribute("logType", "新建");
            userAddress.setCreateDate(new Date());
            //设置所属用户
            userAddress.setSysUser(redisService.findByToken(
                    HttpContextUtils.getHttpServletRequest().getHeader(Constants.TOKEN_HEADER_PARAM))
                    .getId());

            //设置有效性
            userAddress.setIsValid((byte)1);
            //新增收货地址
            rows = userAddressMapper.insert(userAddress);

        } else {
            HttpContextUtils.getHttpServletRequest().setAttribute("logType", "编辑");
            //设置修改时间
            userAddress.setUpdateDate(new Date());
            //修改
            rows = userAddressMapper.updateByPrimaryKeySelective(userAddress);
        }
        //返回结果
        return RUtil.isOptionOk(rows);
    }

    @Override
    public R removeByIdList(List<Long> idList) {
        //根据id数组将is_valid字段置为0
        return RUtil.isBatchOk(userAddressMapper.updateIsValidByIdList(idList, (byte)0), idList.size());
    }
}
