package com.qiang.practice.controller;

import com.qiang.practice.annotation.MyLog;
import com.qiang.practice.annotation.PageCommon;
import com.qiang.practice.model.OrgAchievement;
import com.qiang.practice.service.OrgAchievementService;
import com.qiang.practice.utils.response.R;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/8/20
 * @Description: 企业成果
 */
@SuppressWarnings("all")
@Api(value = "企业成果", tags = "企业成果")
@RestController
public class OrgAchievementController {
    @Autowired
    private OrgAchievementService orgAchievementService;
    /**
     * 分页获取企业成果，若查询条件不为null，则为分页且条件查询
     *
     * @param paramMap
     * @return
     */
    @ApiOperation("分页获取企业成果，若查询条件不为null，则为分页且条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "title", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "eventDate", required = false, dataType = "Date", paramType = "query"),
            @ApiImplicitParam(name = "isPublish", required = false, dataType = "byte", paramType = "query")
    })
    @PageCommon
    @RequestMapping(value = "/api/orgAchievements", method = RequestMethod.GET)
    public R getByPage(@ApiParam(hidden = true) @RequestParam Map<String, Object> paramMap) {
        return orgAchievementService.getByPage(paramMap);
    }

    /**
     * 新建/编辑企业成果
     *
     * @param orgImpression
     * @return
     */
    @ApiOperation("新建/编辑企业成果  id空：新建；  id不空：编辑")
    @MyLog("新建/编辑企业成果")
    @RequestMapping(value = "/api/orgAchievements", method = RequestMethod.POST)
    public R addOrUpdate(HttpServletRequest request, @RequestBody OrgAchievement orgAchievement) {
        return orgAchievementService.addOrUpdate(orgAchievement);
    }

    /**
     * 根据id查单个企业成果的详细信息
     *
     * @param id
     * @return
     */
    @ApiOperation("根据id查单个企业成果的详细信息")
    @RequestMapping(value = "/api/orgAchievements/{id}", method = RequestMethod.GET)
    public R getById(@PathVariable(value = "id") Long id) {
        return orgAchievementService.getById(id);
    }

    /**
     * 根据id数组删除被选中的企业成果
     *
     * @param idList
     * @return
     */
    @ApiOperation("根据id数组删除被选中的企业成果")
    @MyLog("删除企业成果")
    @RequestMapping(value = "/api/orgAchievements/{ids}", method = RequestMethod.DELETE)
    public R removeByIdList(@PathVariable(value = "ids") List<Long> idList) {
        return orgAchievementService.removeByIdList(idList);
    }

    /**
     * 根据id数组发布被选中的企业成果
     *
     * @param idList
     * @return
     */
    @ApiOperation("根据id数组发布被选中的企业成果")
    @MyLog("发布企业成果")
    @RequestMapping(value = "/api/orgAchievements/publish/{ids}", method = RequestMethod.GET)
    public R publishByIdList(@PathVariable(value = "ids") List<Long> idList) {
        return orgAchievementService.publishByIdList(idList);
    }

    /**
     * 根据id数组撤销被选中的企业成果
     *
     * @param idList
     * @return
     */
    @ApiOperation("根据id数组撤销被选中的企业成果")
    @MyLog("撤销企业成果")
    @RequestMapping(value = "/api/orgAchievements/publish/{ids}", method = RequestMethod.DELETE)
    public R revokeByIdList(@PathVariable(value = "ids") List<Long> idList) {
        return orgAchievementService.revokeByIdList(idList);
    }

    /**
     * 获取所有已发布的企业成果
     * @return
     */
    @ApiOperation("获取所有已发布的企业成果")
    @RequestMapping(value = "/api/orgAchievements/all", method = RequestMethod.GET)
    public R getAll() {
        return orgAchievementService.getAll();
    }
}
