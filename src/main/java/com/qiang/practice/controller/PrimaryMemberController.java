package com.qiang.practice.controller;


import com.qiang.practice.annotation.MyLog;
import com.qiang.practice.model.PrimaryMember;
import com.qiang.practice.service.PrimaryMemberService;
import com.qiang.practice.utils.response.R;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 主要成员 前端控制器
 *
 * @author 陈立强
 * @since 2019-07-05
 */
@Api(value = "主要成员", tags = "主要成员")
@RestController
public class PrimaryMemberController {

    @Autowired
    private PrimaryMemberService primaryMemberService;

    /**
     * 获取主要成员，若各查询条件不为null，则为条件查询
     *
     * @param paramMap
     * @return
     */
    @ApiOperation("获取主要成员列表，若各查询条件不为null，则为条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "nation", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "politicCountenance", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "title", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "birthday", required = false, dataType = "Date", paramType = "query"),
            @ApiImplicitParam(name = "workDate", required = false, dataType = "Date", paramType = "query"),
    })
    @RequestMapping(value = "/api/primaryMembers", method = RequestMethod.GET)
    public R getByCondition(@ApiParam(hidden = true) @RequestParam Map<String, Object> paramMap) {
        return primaryMemberService.getByCondition(paramMap);
    }

    /**
     * 新建/编辑主要成员
     *
     * @param primaryMember
     * @return
     */
    @ApiOperation("新建/编辑主要成员  id为空：新建； id非空：编辑")
    @MyLog("新建/编辑主要成员")
    @RequestMapping(value = "/api/primaryMembers", method = RequestMethod.POST)
    public R addOrUpdate(HttpServletRequest request, @RequestBody PrimaryMember primaryMember) {
        //新建或编辑主要成员
        return primaryMemberService.addOrUpdate(request, primaryMember);
    }

    /**
     * 根据id查单个成员的详细信息
     *
     * @param id
     * @return
     */
    @ApiOperation("根据id查单个成员的详细信息")
    @RequestMapping(value = "/api/primaryMembers/{id}", method = RequestMethod.GET)
    public R getById(@PathVariable(value = "id") Long id) {
        return primaryMemberService.getById(id);
    }

    /**
     * 根据id数组删除被选中的主要成员
     *
     * @param idList
     * @return
     */
    @ApiOperation("根据id数组删除被选中的主要成员")
    @MyLog("删除主要成员")
    @RequestMapping(value = "/api/primaryMembers/{ids}", method = RequestMethod.DELETE)
    public R removeByIdList(@PathVariable(value = "ids") List<Long> idList) {
        return primaryMemberService.removeByIdList(idList);
    }

    /**
     * 获取需要排序的主要成员数据
     *
     * @return
     */
    @ApiOperation("获取需要排序的主要成员数据")
    @RequestMapping(value = "/api/primaryMembers/sort", method = RequestMethod.GET)
    public R getSortData() {
        return primaryMemberService.getSortData();
    }

    /**
     * 保存排序
     *
     * @param param
     * @return
     */
    @ApiOperation("保存排序")
    @MyLog("排序主要成员")
    @RequestMapping(value = "/api/primaryMembers/sort", method = RequestMethod.POST)
    public R sort(@RequestBody Map<String, Object> param) {
        return primaryMemberService.sort(param);
    }
}
