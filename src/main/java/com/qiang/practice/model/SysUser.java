package com.qiang.practice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.security.Principal;
import java.util.Date;

@Data
public class SysUser implements Principal {
    private Long id;

    private Long orgId;

    private String userName;

    private Byte userSex;

    private String loginName;

    private String loginPwd;

    private String phone;

    private String imgPath;

    private Byte haveAuthority;

    private Byte isCustomerService;

    private Byte status;

    private Integer orderby;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8:00")
    private Date createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8:00")
    private Date updateDate;

    private Byte isValid;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8:00")
    private Date invalidDate;

    @Override
    public String getName() {
        return id.toString();
    }
}