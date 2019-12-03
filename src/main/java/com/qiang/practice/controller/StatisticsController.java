package com.qiang.practice.controller;

import com.qiang.practice.service.StatisticsService;
import com.qiang.practice.utils.response.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: CLQ
 * @Date: 2019/8/5
 * @Description: TODO
 */
@Api(value = "统计分析", tags = "统计分析")
@RestController
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 获取发布数量统计数据
     *
     * @param year
     * @return
     */
    @ApiOperation("获取发布数量统计数据")
    @RequestMapping(value = "/api/publishCounts", method = RequestMethod.GET)
    public R getPublishCounts(@RequestParam(value = "year", required = false) String year) {
        return statisticsService.getPublishCounts(year);
    }

    /**
     * 获取阅读数量统计数据
     *
     * @param year
     * @return
     */
    @ApiOperation("获取阅读数量统计数据")
    @RequestMapping(value = "/api/readCounts", method = RequestMethod.GET)
    public R getReadCounts(@RequestParam(value = "year", required = false) String year) {
        return statisticsService.getReadCounts(year);
    }

    /**
     * 将该年发布数量的统计数据制成Excel并返回给前端下载
     *
     * @param year
     * @return
     */
    @ApiOperation("将发布数量的统计数据制成Excel并弹出该Excel的下载框")
    @RequestMapping(value = "/api/publishCounts/excel", method = RequestMethod.GET)
    public void downloadPublishCountsExcel(HttpServletRequest request, HttpServletResponse response,
                                           @RequestParam(value = "year", required = false) String year) {
        statisticsService.downloadPublishCountsExcel(request, response, year);
    }

    /**
     * 将该年阅读数量的统计数据制成Excel并返回给前端下载
     *
     * @param year
     * @return
     */
    @ApiOperation("将阅读数量的统计数据制成Excel并弹出该Excel的下载框")
    @RequestMapping(value = "/api/readCounts/excel", method = RequestMethod.GET)
    public void downloadReadCountsExcel(HttpServletRequest request, HttpServletResponse response,
                                        @RequestParam(value = "year", required = false) String year) {
        statisticsService.downloadReadCountsExcel(request, response, year);
    }
}
