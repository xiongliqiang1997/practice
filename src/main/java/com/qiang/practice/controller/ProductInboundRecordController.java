package com.qiang.practice.controller;

import com.qiang.practice.annotation.PageCommon;
import com.qiang.practice.service.ProductInboundRecordService;
import com.qiang.practice.utils.response.R;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/8/30
 * @Description: 入库记录
 */
@SuppressWarnings("all")
@Api(tags = "入库记录")
@RestController
public class ProductInboundRecordController {
    @Autowired
    private ProductInboundRecordService productInboundRecordService;

    /**
     * 分页查询入库记录，可以包含查询条件
     * @param paramMap
     * @return
     */
    @ApiOperation("分页查询入库记录，可以包含查询条件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页展示数", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "userName", value = "入库人", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "createDate", value = "入库时间", required = false, dataType = "Date", paramType = "query")
    })
    @PageCommon
    @RequestMapping(value = "/api/productInboundRecords", method = RequestMethod.GET)
    public R getByPage(@ApiParam(hidden = true) @RequestParam Map<String, Object> paramMap) {
        return productInboundRecordService.getByPage(paramMap);
    }

    /**
     * 根据id获取入库详情
     * @param id
     * @return
     */
    @ApiOperation("根据id获取入库详情")
    @RequestMapping(value = "/api/productInboundRecords/{id}", method = RequestMethod.GET)
    public R getById(@PathVariable Long id) {
        return productInboundRecordService.getById(id);
    }

    /**
     * 将入库记录导出为Excel
     * @return
     */
    @ApiOperation("入库 ===> coding中")
    @RequestMapping(value = "/api/products/inbound/excel", method = RequestMethod.GET)
    public void inbound(HttpServletResponse response) {
        productInboundRecordService.downloadExcel(response);
    }
}
