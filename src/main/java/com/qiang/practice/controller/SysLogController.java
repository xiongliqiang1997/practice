package com.qiang.practice.controller;


import com.qiang.practice.annotation.PageCommon;
import com.qiang.practice.service.SysLogService;
import com.qiang.practice.utils.response.R;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 日志管理 前端控制器
 *
 * @author 陈立强
 * @since 2019-07-05
 */
@Api(value = "日志管理", tags = "日志管理")
@RestController
public class SysLogController {
    @Autowired
    private SysLogService sysLogService;

    /**
     * 分页获取日志，若各查询条件不为null，则为分页且条件查询
     * @param paramMap
     * @return
     */
    @ApiOperation("分页获取日志，若各查询条件不为null，则为分页且条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "createDate", required = false, dataType = "Date", paramType = "query"),
            @ApiImplicitParam(name = "userName", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "logType", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "logResult", required = false, dataType = "byte", paramType = "query"),
            @ApiImplicitParam(name = "content", required = false, dataType = "String", paramType = "query"),
    })
    @PageCommon
    @RequestMapping(value = "/api/sysLogs", method = RequestMethod.GET)
    public R getByPageAndCondition(@ApiParam(hidden = true) @RequestParam Map<String, Object> paramMap) {
        return sysLogService.getByPageAndCondition(paramMap);
    }

    @ApiOperation("导出截止目前为止所有的Excel，并弹出下载框")
    @RequestMapping(value = "/api/sysLogs/excel", method = RequestMethod.GET)
    public void downloadExcel(HttpServletRequest request, HttpServletResponse response) {
        sysLogService.downloadExcel(request, response);
    }

    /**
     * 根据id查详情
     * @param id
     * @return
     */
    @ApiOperation("根据id查详情")
    @RequestMapping(value = "/api/sysLogs/{id}", method = RequestMethod.GET)
    public R getById(@PathVariable Long id) {
        return sysLogService.getById(id);
    }
}
