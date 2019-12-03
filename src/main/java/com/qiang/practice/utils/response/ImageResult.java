package com.qiang.practice.utils.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @Author: CLQ
 * @Date: 2019/7/22
 * @Description: TODO
 */
@Data
@AllArgsConstructor
public class ImageResult {
    private Integer errno;
    private boolean res;
    private String[] data;
    private String msg;
    private List<ImageInfo> imageArray;
    private String fullUrl;
}
