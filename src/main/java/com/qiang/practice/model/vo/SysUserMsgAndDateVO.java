package com.qiang.practice.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.qiang.practice.model.SysUserMsg;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: CLQ
 * @Date: 2019/9/12
 * @Description: TODO
 */
@Data
public class SysUserMsgAndDateVO {
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lastMsgDate;
    private String lastMsgDateStr;
    private List<SysUserMsg> sysUserMsgList;
}
