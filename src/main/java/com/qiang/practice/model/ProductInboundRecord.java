package com.qiang.practice.model;

import java.util.Date;

public class ProductInboundRecord {
    private Long id;

    private Long sysUser;

    private String productSource;

    private Date createDate;

    private String remark;

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

    public String getProductSource() {
        return productSource;
    }

    public void setProductSource(String productSource) {
        this.productSource = productSource;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}