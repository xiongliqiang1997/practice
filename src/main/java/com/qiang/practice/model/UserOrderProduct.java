package com.qiang.practice.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserOrderProduct {
    private Long id;

    private Long userOrderId;

    private Long productId;

    private String firstProductImgPath;

    private String productName;

    private BigDecimal productPrice;

    private Integer num;

    private String remark;

}