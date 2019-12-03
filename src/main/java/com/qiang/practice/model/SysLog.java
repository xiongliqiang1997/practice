package com.qiang.practice.model;

import lombok.Data;

import java.util.Date;

@Data
public class SysLog {
    private Long id;

    private Long sysUser;

    private String logType;

    private String userName;

    private Byte logResult;

    private Date createDate;

    private String content;

}