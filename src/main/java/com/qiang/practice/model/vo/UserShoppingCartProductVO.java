package com.qiang.practice.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: CLQ
 * @Date: 2019/8/23
 * @Description: TODO
 */
@Data
public class UserShoppingCartProductVO implements Serializable {
    private Long id;

    private Long productId;

    private Long productType;

    private String typeName;

    private String productName;

    private String firstProductImgPath;

    private BigDecimal productPrice;

    private Integer num;

    private Integer productStock;

    private BigDecimal totalProductPrice;

    private Date createDate;
}
