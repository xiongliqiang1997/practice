package com.qiang.practice.model;

import lombok.Data;

@Data
public class ProductInboundRecordDetail {
    private Long id;

    private Long productInboundRecordId;

    private Long productId;

    private String productName;

    private String firstProductImgPath;

    private Integer num;
}