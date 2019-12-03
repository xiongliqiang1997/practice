package com.qiang.practice.utils.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: CLQ
 * @Date: 2019/8/6
 * @Description: Bootstrap-Validator返回值
 */
@Data
@AllArgsConstructor
public class Validator {
    private boolean res;
    private String msg;
    private Object data;
    private boolean valid;
}
