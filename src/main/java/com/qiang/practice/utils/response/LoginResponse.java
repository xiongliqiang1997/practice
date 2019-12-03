package com.qiang.practice.utils.response;

import com.qiang.practice.model.SysUser;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: CLQ
 * @Date: 2019/8/8
 * @Description: TODO
 */
@Data
@AllArgsConstructor
public class LoginResponse {

    private Integer code;

    private String msg;

    private String token;

    private SysUser user;
}