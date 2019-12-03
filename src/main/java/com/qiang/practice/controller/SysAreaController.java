package com.qiang.practice.controller;

import com.qiang.practice.service.SysAreaService;
import com.qiang.practice.utils.response.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: CLQ
 * @Date: 2019/8/26
 * @Description: TODO
 */
@Api(tags = "地理位置信息")
@RestController
public class SysAreaController {
    @Autowired
    private SysAreaService sysAreaService;

    /**
     * 根据地区id获取地区信息
     * @param id
     * @return
     */
    @ApiOperation("根据地区id获取地区信息")
    @RequestMapping(value = "/api/sysAreas/{id}", method = RequestMethod.GET)
    public R getById(@PathVariable Integer id) {
        return sysAreaService.getById(id);
    }

    /**
     * 根据上级地区id获取地区列表(不传代表查一级地区)
     * @param pId
     * @return
     */
    @ApiOperation("根据上级地区id获取地区列表(不传代表查一级地区)")
    @RequestMapping(value = "/api/sysAreas", method = RequestMethod.GET)
    public R getByPId(@ApiParam(value = "上级地区id") @RequestParam(required = false) Integer pId) {
        return sysAreaService.getByPId(pId);
    }
}
