package com.qiang.practice.model;

import java.io.Serializable;
import java.util.Date;

public class ReadLog implements Serializable {
    private Long id;

    private Long sysUser;

    private Long plateInfo;

    private Date readTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSysUser() {
        return sysUser;
    }

    public void setSysUser(Long sysUser) {
        this.sysUser = sysUser;
    }

    public Long getPlateInfo() {
        return plateInfo;
    }

    public void setPlateInfo(Long plateInfo) {
        this.plateInfo = plateInfo;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }
}