package com.qiang.practice.controller;


import com.qiang.practice.annotation.MyLog;
import com.qiang.practice.annotation.PageCommon;
import com.qiang.practice.model.Plate;
import com.qiang.practice.service.PlateService;
import com.qiang.practice.utils.response.R;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 板块管理 前端控制器
 *
 * @author 陈立强
 * @since 2019-07-05
 */
@Api(value = "板块管理", tags = "板块管理")
@RestController
public class PlateController {

    @Autowired
    private PlateService plateService;

    /**
     * 分页获取板块信息，若查询条件不为null，则为 分页且条件查询
     *
     * @param paramMap
     * @return
     */
    @ApiOperation("分页获取板块信息列表，若查询条件不为null，则为 分页且条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "plateName", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "isUse", required = false, dataType = "byte", paramType = "query"),
    })
    @PageCommon
    @RequestMapping(value = "/api/plates", method = RequestMethod.GET)
    public R getByPage(@ApiParam(hidden = true) @RequestParam Map<String, Object> paramMap) {
        return plateService.getByPage(paramMap);
    }

    /**
     * 查看板块名称是否已存在
     *
     * @param plateName
     * @param id
     * @return
     */
    @ApiOperation("查看板块名称是否已存在")
    @RequestMapping(value = "/api/plates/plateName", method = RequestMethod.GET)
    public Object checkPlateNameExist(@RequestParam(value = "plateName") String plateName,
                                      @RequestParam(value = "id", required = false) Long id) {
        return plateService.checkPlateNameExist(plateName, id);
    }

    /**
     * 新建/编辑板块
     *
     * @param plate
     * @return
     */
    @ApiOperation("新建/编辑板块 id为空时保存新建，不为空时保存编辑")
    @MyLog("新建/编辑板块")
    @RequestMapping(value = "/api/plates", method = RequestMethod.POST)
    public R addOrUpdate(HttpServletRequest request, @RequestBody Plate plate) {
        return plateService.addOrUpdate(request, plate);
    }

    /**
     * 根据id查单个板块的详细信息
     *
     * @param id
     * @return
     */
    @ApiOperation("查单个板块的详细信息")
    @RequestMapping(value = "/api/plates/{id}", method = RequestMethod.GET)
    public R getById(@PathVariable(value = "id") Long id) {
        return plateService.getById(id);
    }

    /**
     * 根据id数组删除被选中的板块
     *
     * @param idList
     * @return
     */
    @ApiOperation("根据id数组删除被选中的板块")
    @MyLog("删除板块")
    @RequestMapping(value = "/api/plates/{ids}", method = RequestMethod.DELETE)
    public R removeByIdList(@PathVariable(value = "ids") List<Long> idList) {
        return plateService.removeByIdList(idList);
    }

    /**
     * 根据id数组发布被选中的板块
     *
     * @param idList
     * @return
     */
    @ApiOperation("根据id数组发布被选中的板块")
    @MyLog("发布板块")
    @RequestMapping(value = "/api/plates/publish/{ids}", method = RequestMethod.GET)
    public R publishByIdList(@PathVariable(value = "ids") List<Long> idList) {
        return plateService.publishByIdList(idList);
    }

    /**
     * 根据id数组撤销被选中的板块
     *
     * @param idList
     * @return
     */
    @ApiOperation("根据id数组撤销被选中的板块")
    @MyLog("撤销板块")
    @RequestMapping(value = "/api/plates/publish/{ids}", method = RequestMethod.DELETE)
    public R revokeByIdList(@PathVariable(value = "ids") List<Long> idList) {
        return plateService.revokeByIdList(idList);
    }

    /**
     * 获取需要排序的板块数据
     *
     * @return
     */
    @ApiOperation("获取需要排序的板块数据")
    @RequestMapping(value = "/api/plates/sort", method = RequestMethod.GET)
    public R getSortData() {
        return plateService.getSortData();
    }

    /**
     * 保存排序
     *
     * @param param
     * @return
     */
    @ApiOperation("保存排序")
    @MyLog("排序板块")
    @RequestMapping(value = "/api/plates/sort", method = RequestMethod.POST)
    public R sort(@RequestBody Map<String, Object> param) {
        return plateService.sort(param);
    }

    /**
     * 获取全部板块
     * @return
     */
    @ApiOperation("获取全部板块")
    @RequestMapping(value = "/api/plates/all", method = RequestMethod.GET)
    public R getAll() {
        return plateService.getAll();
    }
}
