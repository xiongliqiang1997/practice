package com.qiang.practice.service;

import com.qiang.practice.utils.response.R;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: CLQ
 * @Date: 2019/8/5
 * @Description: TODO
 */
public interface StatisticsService {

    /**
     * 获取发布数量统计数据
     * @param year
     * @return
     */
    R getPublishCounts(String year);

    /**
     * 获取阅读数量统计数据
     * @param year
     * @return
     */
    R getReadCounts(String year);

    /**
     * 将发布数量的统计数据制成Excel并返回给前端下载
     * @param year
     * @return
     */
    void downloadPublishCountsExcel(HttpServletRequest request, HttpServletResponse response, String year);

    /**
     * 将阅读数量的统计数据制成Excel并返回给前端下载
     * @param request
     * @param response
     * @param year
     */
    void downloadReadCountsExcel(HttpServletRequest request, HttpServletResponse response, String year);

}
