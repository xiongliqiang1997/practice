package com.qiang.practice.model;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: ProductTypeTreeJson
 * @Author: CLQ
 * @Date: 2019/8/21
 * @Description: TODO
 */
@Data
public class ProductTypeTreeJson {
    private Long id;
    private String typeName;
    private List<ProductTypeTreeJson> children;
}
