package com.qiang.practice.utils.response;

import lombok.Data;

import java.util.List;

/**
 * @Author: CLQ
 * @Date: 2019/8/5
 * @Description: TODO
 */
@Data
public class StatisticJsonObj {
    private List<StatisticsObj> list;
    private String[] monthArray;
}
