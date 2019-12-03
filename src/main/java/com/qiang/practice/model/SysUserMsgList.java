package com.qiang.practice.model;

import lombok.Data;

import java.util.Date;

@Data
public class SysUserMsgList {
    private Long id;

    private Long sysUser;

    private Long anotherUserId;

    private Date lastMsgDate;

}