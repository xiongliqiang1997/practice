package com.qiang.practice.utils.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: CLQ
 * @Date: 2019/7/17
 * @Description: TODO
 */
@Data
public class Pagination<T> implements Serializable {
    private Integer page;
    private Integer total;
    private List<T> list;
}
