package com.qiang.practice.model.vo;

import lombok.Data;

/**
 * @Author: CLQ
 * @Date: 2019/9/6
 * @Description: TODO
 */
@Data
public class UpdatePwdObj {
    private Long id;
    private String oldLoginPwd;
    private String newLoginPwd;
}
