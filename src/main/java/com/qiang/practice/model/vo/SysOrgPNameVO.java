package com.qiang.practice.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: CLQ
 * @Date: 2019/7/15
 * @Description: TODO
 */
@Data
public class SysOrgPNameVO implements Serializable {
    private Long id;

    @JsonProperty(value = "pId")
    private Long pId;

    private String orgName;

    private String remark;

    private Integer orderby;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8:00")
    private Date createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8:00")
    private Date updateDate;

    private Byte isValid;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8:00")
    private Date invalidDate;

    @JsonProperty(value = "pName")
    private String pName;
}
