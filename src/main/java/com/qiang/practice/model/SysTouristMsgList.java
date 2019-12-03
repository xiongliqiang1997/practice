package com.qiang.practice.model;

import lombok.Data;

import java.util.Date;

@Data
public class SysTouristMsgList {
    private Long id;

    private String sysUser;

    private String anotherUserId;

    private Date lastMsgDate;
}