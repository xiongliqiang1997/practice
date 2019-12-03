package com.qiang.practice.mapper;

import com.qiang.practice.model.UserOrder;
import com.qiang.practice.model.vo.UserOrderAndOrderProductVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface UserOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserOrder record);

    int insertSelective(UserOrder record);

    UserOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserOrder record);

    int updateByPrimaryKey(UserOrder record);

    /**
     * 根据条件分页查询用户订单列表
     * @param paramMap
     * @return
     */
    List<UserOrderAndOrderProductVO> getListByPageAndCondition(Map<String, Object> paramMap);

    /**
     * 根据id字符串将订单有效字段置为newValue
     * @param idListStr
     * @param newValue
     * @return
     */
    int updateIsValid(@Param("idListStr") String idListStr,
                      @Param("newValue") byte newValue);

    /**
     * 获取订单详情
     * @return
     */
    UserOrderAndOrderProductVO getById(Long id);

    /**
     * 通过订单id、订单编号、订单创建人id完成付款
     * @param id
     * @param sysUser
     * @param code
     * @param payNo
     * @param payedStatus
     * @param notPayStatus
     */
    int pay(@Param("id") Long id,
            @Param("sysUser") Long sysUser,
            @Param("code") String code,
            @Param("payNo") String payNo,
            @Param("payedStatus") Byte payedStatus,
            @Param("notPayStatus") Byte notPayStatus);

    /**
     * 获取所有的订单
     * @return
     */
    List<UserOrder> getAll();

    /**
     * 将订单状态置为已发货
     * @return
     */
    int sendProduct(@Param("id") Long id,
                    @Param("status") Byte status);

    /**
     * 根据id修改订单总金额
     * @param id
     * @param code
     * @param amountTotal
     * @return
     */
    int updateAmountTotal(@Param("id") Long id,
                          @Param("code") String code,
                          @Param("amountTotal") BigDecimal amountTotal);

}