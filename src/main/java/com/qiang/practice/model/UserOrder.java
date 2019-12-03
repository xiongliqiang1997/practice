package com.qiang.practice.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserOrder {
    private Long id;

    private String code;

    private Long sysUser;

    private Byte status;

    private BigDecimal amountTotal;

    private Long addressId;

    private String consigneeName;

    private String consigneePhone;

    private String consigneeAddress;

    private String payChannel;

    private String payNo;

    private Date createDate;

    private Date payTime;

    private Byte isValid;

    private Date invalidDate;

}