package com.qiang.practice.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: CLQ
 * @Date: 2019/8/28
 * @Description: TODO
 */
@Data
public class UserOrderProductLogisticsVO {
    private Long id;

    private Long userOrderId;

    private Long productId;

    private Byte logisticsStatus;

    private String firstProductImgPath;

    private String productName;

    private BigDecimal productPrice;

    private Integer num;

    private String remark;
}
