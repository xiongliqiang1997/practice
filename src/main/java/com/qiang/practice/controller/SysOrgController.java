package com.qiang.practice.controller;

import com.qiang.practice.annotation.MyLog;
import com.qiang.practice.annotation.PageCommon;
import com.qiang.practice.model.SysOrg;
import com.qiang.practice.service.SysOrgService;
import com.qiang.practice.utils.response.R;
import com.qiang.practice.utils.response.Validator;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 组织机构 前端控制器
 *
 * @author 陈立强
 * @since 2019-07-05
 */
@Api(value = "组织机构", tags = "组织机构")
@RestController
public class SysOrgController {

    @Autowired
    private SysOrgService sysOrgService;

    /**
     * 分页获取组织机构，若上级组织机构id不为null，则分页查该机构的子机构
     * 若各查询条件不为null，则为 分页且条件查询
     *
     * @param paramMap
     * @return
     */
    @ApiOperation("分页获取组织机构, 若各查询条件不为null，则为分页且条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "id", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "pName", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orgName", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "remark", required = false, dataType = "String", paramType = "query")
    })
    @PageCommon
    @RequestMapping(value = "/api/sysOrgs", method = RequestMethod.GET)
    public R getByPage(@ApiParam(hidden = true) @RequestParam Map<String, Object> paramMap) {
        //分页获取有效的组织机构，若上级组织机构id不为null，则分页查该机构所有有效的的子机构
        return sysOrgService.getByPage(paramMap);
    }

    /**
     * 查看该机构名称是否已存在
     *
     * @param orgName
     * @return
     */
    @ApiOperation("查看该机构名称是否已存在")
    @RequestMapping(value = "/api/sysOrgs/sysName", method = RequestMethod.GET)
    public Validator checkOrgNameExist(@RequestParam(value = "orgName") String orgName,
                                    @RequestParam(value = "id", required = false) Long id) {
        //查看该机构名称是否已存在
        return sysOrgService.checkOrgNameExist(orgName, id);
    }

    /**
     * 新建/编辑组织机构
     *
     * @param request
     * @param sysOrg
     * @return
     */
    @ApiOperation("新建/编辑组织机构  id空：新建；  id非空：编辑")
    @MyLog("新建/编辑组织机构")
    @RequestMapping(value = "/api/sysOrgs", method = RequestMethod.POST)
    public R addOrUpdate(HttpServletRequest request, @RequestBody SysOrg sysOrg) {
        //新建或编辑组织机构
        return sysOrgService.addOrUpdate(request, sysOrg);
    }


    /**
     * 根据id数组删除被选中的组织机构
     *
     * @param idList
     * @return
     */
    @ApiOperation("根据id数组删除被选中的组织机构")
    @MyLog("删除组织机构")
    @RequestMapping(value = "/api/sysOrgs/{ids}", method = RequestMethod.DELETE)
    public R removeByIdList(@PathVariable(value = "ids") List<Long> idList) {
        return sysOrgService.removeByIdList(idList);
    }

    /**
     * 获取树形菜单所需要的json格式数据
     *
     * @return
     */
    @ApiOperation("获取vue树形菜单插件所需要的json格式数据")
    @RequestMapping(value = "/api/sysOrgs/tree", method = RequestMethod.GET)
    public R getTreeMenu(@RequestParam(value = "id", required = false) Long id) {
        //获取树形菜单所需要的json格式数据
        return sysOrgService.getTreeMenu(id);
    }

    /**
     * 获取树形菜单所需要的json格式数据My
     *
     * @return
     */
    @ApiOperation("获取bootstrap树形菜单插件所需要的json格式数据")
    @RequestMapping(value = "/api/sysOrgs/myTree", method = RequestMethod.GET)
    public R getMyTreeMenu(@RequestParam(value = "id", required = false) Long id) {
        //获取树形菜单所需要的json格式数据
        return sysOrgService.getMyTreeMenu(id);
    }

    /**
     * 根据id查单个组织机构的详细信息
     *
     * @param id
     * @return
     */
    @ApiOperation("根据id查单个组织机构的详细信息")
    @RequestMapping(value = "/api/sysOrgs/{id}", method = RequestMethod.GET)
    public R getById(HttpServletRequest request, @PathVariable(value = "id") Long id) {
        //根据id查单个组织机构的详细信息
        return R.ok(sysOrgService.getById(id));
    }

    /**
     * 根据组织机构id获得需要排序的组织机构数据
     *
     * @param id
     * @return
     */
    @ApiOperation("根据组织机构id获得需要排序的组织机构数据")
    @RequestMapping(value = "/api/sysOrgs/sort", method = RequestMethod.GET)
    public R getSortData(@RequestParam(value = "id", required = false) Long id) {
        //根据id获得需要排序的数据
        return sysOrgService.getSysOrgSortData(id);
    }

    /**
     * 保存排序
     *
     * @param param
     * @return
     */
    @ApiOperation("保存排序")
    @MyLog("排序组织机构")
    @RequestMapping(value = "/api/sysOrgs/sort", method = RequestMethod.POST)
    public R sort(@RequestBody Map<String, Object> param) {
        //保存排序
        return sysOrgService.sort(param);
    }
}
