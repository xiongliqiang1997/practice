package com.qiang.practice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class PlateInfo {
    private Long id;

    private Long plate;

    private Long sysUser;

    private String title;

    private String subTitle;

    private Byte isTop;

    private Byte isPublish;

    private Byte orgImpression;

    private Byte orgAchievement;

    private String imgPath;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8:00")
    private Date publishDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8:00")
    private Date eventDate;

    private Integer readNum;

    private Date createDate;

    private Date updateDate;

    private Byte isValid;

    private Date invalidDate;

    private String content;

}