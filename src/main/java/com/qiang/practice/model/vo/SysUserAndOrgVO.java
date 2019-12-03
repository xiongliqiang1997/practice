package com.qiang.practice.model.vo;

import lombok.Data;

/**
 * @Author: CLQ
 * @Date: 2019/9/9
 * @Description: TODO
 */
@Data
public class SysUserAndOrgVO {
    private Long id;

    private Long orgId;

    private String userName;

    private Byte userSex;

    private String loginName;

    private String phone;

    private String imgPath;

    private Byte haveAuthority;

    private Byte isCustomerService;

    private String orgName;
}
