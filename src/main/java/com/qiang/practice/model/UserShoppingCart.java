package com.qiang.practice.model;

import lombok.Data;

import java.util.Date;

@Data
public class UserShoppingCart {
    private Long id;

    private Long sysUser;

    private Long productId;

    private Integer num;

    private Date createDate;

}