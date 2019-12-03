package com.qiang.practice.mapper;

import com.qiang.practice.model.UserAddress;
import com.qiang.practice.model.vo.UserAddressAreaVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserAddressMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserAddress record);

    int insertSelective(UserAddress record);

    UserAddress selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserAddress record);

    int updateByPrimaryKey(UserAddress record);

    /**
     * 获取用户的收货地址列表
     * 若isDefaultAddress为true，则只查默认收货地址
     * @return
     */
    List<UserAddressAreaVO> getListBySysUser(@Param("sysUser") Long sysUser,
                                       @Param("isDefaultAddress") Boolean isDefaultAddress);

    /**
     * 根据sysUser将isDefaultAddress字段从originValue修改为newValue
     * @param sysUser
     * @param originValue
     * @param newValue
     * @return
     */
    int updateIsDefaultBySysUser(@Param("sysUser") Long sysUser,
                            @Param("originValue") byte originValue,
                            @Param("newValue") byte newValue);


    /**
     * 根据id数组，将is_valid字段置为newValue
     * @param idList
     * @param newValue
     * @return
     */
    int updateIsValidByIdList(List<Long> idList, byte newValue);

    /**
     * 获取收货地址详情
     * @param id
     * @return
     */
    UserAddressAreaVO getById(@Param("id") Long id,
                              @Param("sysUserId") Long sysUserId);

    /**
     * 根据id查询详情（包含选择的市县数组）
     * @param id
     * @return
     */
    UserAddressAreaVO getByIdIncludeAreaList(Long id);
}