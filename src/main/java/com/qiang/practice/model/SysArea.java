package com.qiang.practice.model;

import lombok.Data;

import java.util.Date;

@Data
public class SysArea {
    private Integer id;

    private Integer pId;

    private String areaName;

    private Byte areaType;

    private Date createDate;

    private Byte isValid;

    private Date invalidDate;

}