package com.qiang.practice.service;

import com.qiang.practice.model.vo.UserOrderAndOrderProductVO;
import com.qiang.practice.utils.response.R;

import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/8/23
 * @Description: TODO
 */
public interface UserOrderService {
    /**
     * 分页获取用户订单列表，若查询条件不为null，则为分页且条件查询
     * @param paramMap
     * @return
     */
    R getByPage(Map<String, Object> paramMap);

    /**
     * 根据id字符串删除订单
     * @param idListStr
     * @return
     */
    R removeByIdListStr(String idListStr);

    /**
     * 生成订单
     * @param orderAndOrderProductVO
     * @return
     */
    R add(UserOrderAndOrderProductVO orderAndOrderProductVO);

    /**
     * 获取订单详情
     * @param id
     * @return
     */
    R getById(Long id);

    /**
     * 付款
     * @return
     */
    R pay(Long id, String code);

    /**
     * 发货
     */
    void changeOrderStatus();
}
