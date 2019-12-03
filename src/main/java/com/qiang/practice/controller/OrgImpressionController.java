package com.qiang.practice.controller;

import com.qiang.practice.annotation.MyLog;
import com.qiang.practice.annotation.PageCommon;
import com.qiang.practice.model.OrgImpression;
import com.qiang.practice.service.OrgImpressionService;
import com.qiang.practice.utils.response.R;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * 企业印象 前端控制器
 *
 * @author 陈立强
 * @since 2019-07-05
 */
@Api(value = "企业印象", tags = "企业印象")
@RestController
public class OrgImpressionController {

    @Autowired
    private OrgImpressionService orgImpressionService;

    /**
     * 分页获取企业印象，若查询条件不为null，则为分页且条件查询
     *
     * @param paramMap
     * @return
     */
    @ApiOperation("分页获取企业印象列表，若查询条件不为null，则为分页且条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "title", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "eventDate", required = false, dataType = "Date", paramType = "query"),
            @ApiImplicitParam(name = "isPublish", required = false, dataType = "byte", paramType = "query")
    })
    @PageCommon
    @RequestMapping(value = "/api/orgImpressions", method = RequestMethod.GET)
    public R getByPage(@ApiParam(hidden = true) @RequestParam Map<String, Object> paramMap) {
        return orgImpressionService.getOrgImpressionByPage(paramMap);
    }

    /**
     * 新建/编辑企业印象
     *
     * @param orgImpression
     * @return
     */
    @ApiOperation("新建/编辑企业印象  id空：新建；  id不空：编辑")
    @MyLog("新建/编辑企业印象")
    @RequestMapping(value = "/api/orgImpressions", method = RequestMethod.POST)
    public R addOrUpdate(HttpServletRequest request, @RequestBody OrgImpression orgImpression) {
        return orgImpressionService.addOrUpdate(request, orgImpression);
    }

    /**
     * 根据id查单个企业印象的详细信息
     *
     * @param id
     * @return
     */
    @ApiOperation("根据id查单个企业印象的详细信息")
    @RequestMapping(value = "/api/orgImpressions/{id}", method = RequestMethod.GET)
    public R getById(@PathVariable(value = "id") Long id) {
        return orgImpressionService.getById(id);
    }

    /**
     * 根据id数组删除被选中的企业印象
     *
     * @param idList
     * @return
     */
    @ApiOperation("根据id数组删除被选中的企业印象")
    @MyLog("删除企业印象")
    @RequestMapping(value = "/api/orgImpressions/{ids}", method = RequestMethod.DELETE)
    public R removeByIdList(@PathVariable(value = "ids") List<Long> idList) {
        return orgImpressionService.removeByIdList(idList);
    }

    /**
     * 根据id数组发布被选中的企业印象
     *
     * @param idList
     * @return
     */
    @ApiOperation("根据id数组发布被选中的企业印象")
    @MyLog("发布企业印象")
    @RequestMapping(value = "/api/orgImpressions/publish/{ids}", method = RequestMethod.GET)
    public R publishByIdList(@PathVariable(value = "ids") List<Long> idList) {
        return orgImpressionService.publishByIdList(idList);
    }

    /**
     * 根据id数组撤销被选中的企业印象
     *
     * @param idList
     * @return
     */
    @ApiOperation("根据id数组撤销被选中的企业印象")
    @MyLog("撤销企业印象")
    @RequestMapping(value = "/api/orgImpressions/publish/{ids}", method = RequestMethod.DELETE)
    public R revokeByIdList(@PathVariable(value = "ids") List<Long> idList) {
        return orgImpressionService.revokeByIdList(idList);
    }

    /**
     * 获取有数据的年的列表
     * @return
     */
    @ApiOperation("获取有数据的年的列表")
    @RequestMapping(value = "/api/orgImpressions/years", method = RequestMethod.GET)
    public R getYearListWhichHaveData() {
        return orgImpressionService.getYearListWhichHaveData();
    }

    /**
     * 获取某一年已发布的企业印象
     * @return
     */
    @ApiOperation("获取某一年已发布的企业印象")
    @RequestMapping(value = "/api/orgImpressions/all", method = RequestMethod.GET)
    public R getByYear(@RequestParam(required = false) Integer year) {
        return orgImpressionService.getByYear(year);
    }

}
