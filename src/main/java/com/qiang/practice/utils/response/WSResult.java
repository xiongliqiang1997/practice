package com.qiang.practice.utils.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: CLQ
 * @Date: 2019/9/3
 * @Description: WebSocket返回值
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WSResult {
    private int type;
    private boolean isSysUser;
    private Object data;
    private String sign;
    private Long id;
    private Date createDate;

    public WSResult(int type, Object data) {
        this.type = type;
        this.data = data;
    }

    public WSResult(int type, boolean isSysUser, Object data) {
        this.type = type;
        this.isSysUser = isSysUser;
        this.data = data;
    }

    public WSResult(int type, boolean isSysUser, String sign) {
        this.type = type;
        this.isSysUser = isSysUser;
        this.sign = sign;
    }

    public WSResult(int type, boolean isSysUser, String sign, Long msgId, Date createDate) {
        this.type = type;
        this.isSysUser = isSysUser;
        this.sign = sign;
        this.id = msgId;
        this.createDate = createDate;
    }

    public WSResult(int type, boolean isSysUser, Object data, String sign) {
        this.type = type;
        this.isSysUser = isSysUser;
        this.data = data;
        this.sign = sign;
    }

    public boolean getIsSysUser() {
        return isSysUser;
    }

    public void setIsSysUser(boolean isSysUser) {
        this.isSysUser = isSysUser;
    }
}
