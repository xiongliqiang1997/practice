package com.qiang.practice.model.vo;

import com.qiang.practice.model.ProductInboundRecordDetail;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: CLQ
 * @Date: 2019/8/29
 * @Description: TODO
 */
@Data
public class ProductInboundRecordAndDetailVO {
    private Long id;

    private Long sysUser;

    private String userName;

    private String productSource;

    private Integer productCount;

    private Date createDate;

    private String remark;

    private List<ProductInboundRecordDetail> productInboundRecordDetailList;
}
