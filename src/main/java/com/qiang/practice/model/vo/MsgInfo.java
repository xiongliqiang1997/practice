package com.qiang.practice.model.vo;

import lombok.Data;

/**
 * @Author: CLQ
 * @Date: 2019/9/23
 * @Description: 聊天消息
 */
@Data
public class MsgInfo {
    private String msgType;
    private String sign;
    private String msg;
    private String fileName;
    private Long fileSize;
    private Long separateSize;
    private String toUserId;
}
