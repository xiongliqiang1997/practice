package com.qiang.practice.model;

import lombok.Data;

import java.util.Date;

@Data
public class UserAddress {
    private Long id;

    private Long sysUser;

    private String consigneeName;

    private String consigneePhone;

    private Integer province;

    private Integer city;

    private Integer area;

    private String street;

    private Byte isDefaultAddress;

    private Date createDate;

    private Date updateDate;

    private Byte isValid;

    private Date invalidDate;

}