package com.qiang.practice.model;

import lombok.Data;

import java.util.Date;

@Data
public class SysUserMsg {
    private Long id;

    private Long fromUserId;

    private Long toUserId;

    private String msgType;

    private Byte isRecall;

    private Byte isRead;

    private boolean isSysUser;

    private Boolean isBothDelete;

    private Date createDate;

    private String createDateStr; //默认返回到前端是时间戳

    private String msg;

    private String originUrl;

    private String fileName;

    private String fileUrl;

    private String contentMD5;

    public boolean getIsSysUser() {
        return isSysUser;
    }

    public void setIsSysUser(boolean isSysUser) {
        this.isSysUser = isSysUser;
    }
}