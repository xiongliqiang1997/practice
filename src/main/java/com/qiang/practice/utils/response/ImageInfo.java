package com.qiang.practice.utils.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: CLQ
 * @Date: 2019/8/23
 * @Description: TODO
 */
@AllArgsConstructor
@Data
public class ImageInfo implements Serializable {
    private String name;
    private String url;
}
