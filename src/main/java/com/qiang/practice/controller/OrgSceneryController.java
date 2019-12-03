package com.qiang.practice.controller;


import com.qiang.practice.annotation.MyLog;
import com.qiang.practice.annotation.PageCommon;
import com.qiang.practice.model.OrgScenery;
import com.qiang.practice.service.OrgSceneryService;
import com.qiang.practice.utils.response.R;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 企业风光 前端控制器
 *
 * @author 陈立强
 * @since 2019-07-05
 */
@Api(value = "企业风光", tags = "企业风光")
@RestController
public class OrgSceneryController {

    @Autowired
    private OrgSceneryService orgSceneryService;

    /**
     * 分页获取企业风光，若各查询条件不为null，则为分页且条件查询
     *
     * @param paramMap
     * @return
     */
    @ApiOperation("分页获取企业风光列表，若各查询条件不为null，则为分页且条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "title", required = false, dataType = "String", paramType = "query"),
    })
    @PageCommon
    @RequestMapping(value = "/api/orgScenerys", method = RequestMethod.GET)
    public R getByPage(@ApiParam(hidden = true) @RequestParam Map<String, Object> paramMap) {
        return orgSceneryService.getByPage(paramMap);
    }


    /**
     * 新建/编辑企业风光
     *
     * @param orgScenery
     * @return
     */
    @ApiOperation("新建/编辑企业风光  id空： 新建； id不空：编辑")
    @MyLog("新建/编辑企业风光")
    @RequestMapping(value = "/api/orgScenerys", method = RequestMethod.POST)
    public R addOrUpdate(HttpServletRequest request, @RequestBody OrgScenery orgScenery) {
        return orgSceneryService.addOrUpdate(request, orgScenery);
    }

    /**
     * 根据id数组删除被选中的企业风光
     *
     * @param idList
     * @return
     */
    @ApiOperation("根据id数组删除被选中的企业风光")
    @MyLog("删除企业风光")
    @RequestMapping(value = "/api/orgScenerys/{ids}", method = RequestMethod.DELETE)
    public R removeByIdList(@PathVariable(value = "ids") List<Long> idList) {
        return orgSceneryService.removeByIdList(idList);
    }

    /**
     * 根据id查单个企业风光的详细信息
     *
     * @param id
     * @return
     */
    @ApiOperation("根据id查单个企业风光的详细信息")
    @RequestMapping(value = "/api/orgScenerys/{id}", method = RequestMethod.GET)
    public R getById(@PathVariable(value = "id") Long id) {
        return orgSceneryService.getById(id);
    }

    /**
     * 获取需要排序的企业风光数据
     *
     * @return
     */
    @ApiOperation("获取需要排序的企业风光数据")
    @RequestMapping(value = "/api/orgScenerys/sort", method = RequestMethod.GET)
    public R getSortData() {
        return orgSceneryService.getSortData();
    }

    /**
     * 保存排序
     *
     * @param param
     * @return
     */
    @ApiOperation("保存排序")
    @MyLog("排序企业风光")
    @RequestMapping(value = "/api/orgScenerys/sort", method = RequestMethod.POST)
    public R sort(@RequestBody Map<String, Object> param) {
        return orgSceneryService.sort(param);
    }

    /**
     * 官网：获取全部企业风光
     * @return
     */
    @ApiOperation("官网：获取全部企业风光")
    @RequestMapping(value = "/api/orgScenerys/web", method = RequestMethod.GET)
    public R getAll() {
        return orgSceneryService.getAll();
    }

}
