package com.qiang.practice.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Author: CLQ
 * @Date: 2019/9/3
 * @Description: TODO
 */
@Data
public class SysUserMsgListUserNameMsgVO {
    private Long id;

    private Long sysUser;

    private Long anotherUserId;

    private String userName;

    private String imgPath;

    private Integer unReadNum;

    private String lastMsg;

    private Date lastMsgDate;

    private String lastMsgDateStr;

    private Byte status;

}
