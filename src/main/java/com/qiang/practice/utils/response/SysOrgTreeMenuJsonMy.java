package com.qiang.practice.utils.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: CLQ
 * @Date: 2019/7/11
 * @Description: TODO
 */
@Data
public class SysOrgTreeMenuJsonMy implements Serializable {
    private Long id;
    private String text;
    private List<SysOrgTreeMenuJsonMy> nodes;
}
