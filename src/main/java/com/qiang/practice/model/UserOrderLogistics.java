package com.qiang.practice.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserOrderLogistics {
    private Long id;

    private Long userOrderProductId;

    private String code;

    private String logisticsType;

    private BigDecimal fee;

    private BigDecimal realPay;

    private Byte status;

    private Date createDate;

}