package com.qiang.practice.model.vo;

import com.qiang.practice.model.UserOrderProduct;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author: CLQ
 * @Date: 2019/8/23
 * @Description: TODO
 */
@Data
public class UserOrderAndOrderProductVO implements Serializable {
    private Long id;
    private String code;
    private Long sysUser;
    private Byte status;
    private BigDecimal amountTotal;
    private Date createDate;
    private Long addressId;
    private String consigneeName;
    private String consigneePhone;
    private String consigneeAddress;
    private String payChannel;
    private String payNo;
    private Date payTime;
    private List<UserOrderProductLogisticsVO> userOrderProductLogisticsVOList;
    private List<UserOrderProduct> userOrderProductList;
}
