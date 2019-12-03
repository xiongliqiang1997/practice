package com.qiang.practice.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.qiang.practice.model.SysTouristMsg;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SysTouristMsgAndDateVO {
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lastMsgDate;
    private String lastMsgDateStr;
    private List<SysTouristMsg> sysUserMsgList;
}
