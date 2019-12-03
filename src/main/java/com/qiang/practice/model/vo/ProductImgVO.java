package com.qiang.practice.model.vo;

import com.qiang.practice.model.ProductImg;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author: CLQ
 * @Date: 2019/8/22
 * @Description: 商品图片
 */
@Data
public class ProductImgVO implements Serializable {
    private Long id;

    private String productName;

    private Long productType;

    private String typeName;

    private Long sysUser;

    private String userName;

    private String loginName;

    private BigDecimal productPrice;

    private Integer productStock;

    private Integer viewNum;

    private Byte isPublish;

    private String remark;

    private String firstProductImgPath;

    private Date createDate;

    private List<ProductImg> productImgList;
}
