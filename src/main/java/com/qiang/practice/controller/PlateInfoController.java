package com.qiang.practice.controller;


import com.qiang.practice.annotation.MyLog;
import com.qiang.practice.annotation.PageCommon;
import com.qiang.practice.model.PlateInfo;
import com.qiang.practice.service.PlateInfoService;
import com.qiang.practice.utils.response.R;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 板块信息 前端控制器
 *
 * @author 陈立强
 * @since 2019-07-05
 */
@Api(value = "信息发布", tags = "信息发布")
@RestController
public class PlateInfoController {

    @Autowired
    private PlateInfoService plateInfoService;

    /**
     * 获取左侧菜单所需要的json格式数据
     *
     * @return
     */
    @ApiOperation("获取左侧菜单所需要的json格式数据")
    @RequestMapping(value = "/api/plateInfos/menu", method = RequestMethod.GET)
    public R getTreeMenu() {
        return plateInfoService.getMenu();
    }

    /**
     * 分页获取信息发布情况，若各查询条件不为null，则为 分页且条件查询
     *
     * @param paramMap
     * @return
     */
    @ApiOperation("分页获取信息发布列表，若各查询条件不为null，则为 分页且条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "plate", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "title", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "subTitle", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userName", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "createDate", required = false, dataType = "Date", paramType = "query"),
            @ApiImplicitParam(name = "isTop", required = false, dataType = "byte", paramType = "query"),
            @ApiImplicitParam(name = "isPublish", required = false, dataType = "byte", paramType = "query"),
    })
    @PageCommon
    @RequestMapping(value = "/api/plateInfos", method = RequestMethod.GET)
    public R getByPage(@ApiParam(hidden = true) @RequestParam Map<String, Object> paramMap) {
        return plateInfoService.getByPage(paramMap);
    }

    /**
     * 新建/编辑信息
     *
     * @param plateInfo
     * @return
     */
    @ApiOperation("新建/编辑信息  id为空：新建； id非空：编辑")
    @MyLog("新建/编辑信息")
    @RequestMapping(value = "/api/plateInfos", method = RequestMethod.POST)
    public R addOrUpdate(HttpServletRequest request, @RequestBody PlateInfo plateInfo) {
        return plateInfoService.addOrUpdate(request, plateInfo);
    }

    /**
     * 根据id查单个信息
     *
     * @param id
     * @return
     */
    @ApiOperation("根据id查单个信息")
    @RequestMapping(value = "/api/plateInfos/{id}", method = RequestMethod.GET)
    public R getById(HttpServletRequest request, @PathVariable(value = "id") Long id) {
        return plateInfoService.getById(request, id);
    }


    /**
     * 根据id数组删除被选中的信息
     *
     * @param idList
     * @return
     */
    @ApiOperation("根据id数组删除被选中的信息")
    @MyLog("删除信息")
    @RequestMapping(value = "/api/plateInfos/{ids}", method = RequestMethod.DELETE)
    public R removeByIdList(@PathVariable(value = "ids") List<Long> idList) {
        return plateInfoService.removeByIdList(idList);
    }

    /**
     * 根据id数组发布被选中的信息
     *
     * @param idList
     * @return
     */
    @ApiOperation("根据id数组发布被选中的信息")
    @MyLog("发布信息")
    @RequestMapping(value = "/api/plateInfos/publish/{ids}", method = RequestMethod.GET)
    public R publishByIdList(@PathVariable(value = "ids") List<Long> idList) {
        return plateInfoService.publishByIdList(idList);
    }

    /**
     * 根据id数组撤销被选中的信息
     *
     * @param idList
     * @return
     */
    @ApiOperation("根据id数组撤销被选中的信息")
    @MyLog("撤销信息")
    @RequestMapping(value = "/api/plateInfos/publish/{ids}", method = RequestMethod.DELETE)
    public R revokeByIdList(@PathVariable(value = "ids") List<Long> idList) {
        return plateInfoService.revokeByIdList(idList);
    }

    /**
     * 获取各板块的前四个信息
     * @return
     */
    @ApiOperation("获取各板块的前四个信息")
    @RequestMapping(value = "/api/plateInfos/firstThreeEveryPlate", method = RequestMethod.GET)
    public R getFirstFourEveryPlate() {
        return plateInfoService.getFirstFourEveryPlate();
    }

}
