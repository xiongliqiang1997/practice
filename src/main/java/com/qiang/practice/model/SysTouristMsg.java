package com.qiang.practice.model;

import lombok.Data;

import java.util.Date;

@Data
public class SysTouristMsg {
    private Long id;

    private String fromUserId;

    private String toUserId;

    private String msgType;

    private Byte isRecall;

    private Byte isRead;

    private Date createDate; //默认返回到前端是时间戳

    private String createDateStr;

    private String msg;

    private String originUrl;

    private String fileName;

    private String fileUrl;

    private String contentMD5;
}