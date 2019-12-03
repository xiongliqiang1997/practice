package com.qiang.practice.controller;

import com.qiang.practice.annotation.MyLog;
import com.qiang.practice.annotation.PageCommon;
import com.qiang.practice.model.SysUser;
import com.qiang.practice.model.vo.UpdatePwdObj;
import com.qiang.practice.service.SysUserService;
import com.qiang.practice.utils.request.LoginParam;
import com.qiang.practice.utils.response.R;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 用户管理 前端控制器
 *
 * @author 陈立强
 * @since 2019-07-05
 */
@Api(value = "用户管理", tags = "用户管理")
@RestController
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 分页获取有效的用户，若各查询条件不为null，则为分页且条件查询
     *
     * @param paramMap
     * @return
     */
    @ApiOperation("分页获取有效的用户，若各查询条件不为null，则为分页且条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "orgId", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "userName", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userSex", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "loginName", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orgName", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "haveAuthority", required = false, dataType = "String", paramType = "query")
    })
    @PageCommon
    @RequestMapping(value = "/api/sysUsers", method = RequestMethod.GET)
    public R getByPage(@ApiParam(hidden = true) @RequestParam Map<String, Object> paramMap) {
        return sysUserService.getByPage(paramMap);
    }

    /**
     * 查看登录名是否已存在
     *
     * @param loginName
     * @return
     */
    @ApiOperation("查看登录名是否已存在")
    @RequestMapping(value = "/api/sysUsers/loginName", method = RequestMethod.GET)
    public Object checkLoginNameExist(@RequestParam(value = "loginName") String loginName,
                                      @RequestParam(value = "id", required = false) Long id) {
        return sysUserService.checkLoginNameExist(loginName, id);
    }

    /**
     * 新建/编辑用户
     *
     * @param sysUser
     * @return
     */
    @ApiOperation("新建/编辑用户  id空：新建；  id非空：编辑")
    @MyLog("新建/编辑用户")
    @RequestMapping(value = "/api/sysUsers", method = RequestMethod.POST)
    public R addOrUpdate(HttpServletRequest request, @RequestBody SysUser sysUser) {
        return sysUserService.addOrUpdate(request, sysUser);
    }


    /**
     * 根据id查单个用户的详细信息
     *
     * @param id
     * @return
     */
    @ApiOperation("根据id查单个用户的详细信息")
    @RequestMapping(value = "/api/sysUsers/{id}", method = RequestMethod.GET)
    public R getById(@PathVariable(value = "id") Long id) {
        return sysUserService.getById(id);
    }

    /**
     * 根据id数组删除选中的用户
     *
     * @param idList
     * @return
     */
    @ApiOperation("根据id数组删除选中的用户")
    @MyLog("删除用户")
    @RequestMapping(value = "/api/sysUsers/{ids}", method = RequestMethod.DELETE)
    public R removeByIdList(@PathVariable(value = "ids") List<Long> idList) {
        return sysUserService.removeByIdList(idList);
    }

    /**
     * 根据组织机构id获得需要排序的用户数据
     *
     * @param orgId
     * @return
     */
    @ApiOperation("根据组织机构id获得需要排序的用户数据")
    @RequestMapping(value = "/api/sysUsers/sort", method = RequestMethod.GET)
    public R getSortData(@RequestParam(value = "id", required = false) Long orgId) {
        return sysUserService.getSortData(orgId);
    }

    /**
     * 保存对用户的新排序
     *
     * @param param
     * @return
     */
    @ApiOperation("保存对用户的新排序")
    @MyLog("排序用户")
    @RequestMapping(value = "/api/sysUsers/sort", method = RequestMethod.POST)
    public R sort(@RequestBody Map<String, Object> param) {
        return sysUserService.sort(param);
    }

    /**
     * 登录
     *
     * @param request
     * @param loginParam
     * @return
     */
    @ApiOperation("登录")
    @MyLog("登录")
    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    public R login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginParam loginParam) {
        return sysUserService.login(request, response, loginParam);
    }

    /**
     * 注销
     *
     * @param request
     * @param response
     */
    @ApiOperation("注销")
    @MyLog("注销")
    @RequestMapping(value = "/api/logout", method = RequestMethod.GET)
    public R logout(HttpServletRequest request, HttpServletResponse response) {
        return sysUserService.logout(request, response);
    }

    /**
     * 根据token获取用户信息
     *
     * @param request
     * @return
     */
    @ApiOperation("根据token获取用户信息")
    @RequestMapping(value = "/api/loginUser", method = RequestMethod.GET)
    public R getByToken(HttpServletRequest request) {
        return sysUserService.getByToken(request);
    }

    /**
     * 修改密码
     * @param updatePwdObj
     * @return
     */
    @ApiOperation("修改密码")
    @RequestMapping(value = "/api/updatePassword", method = RequestMethod.POST)
    public R updatePassword(@RequestBody UpdatePwdObj updatePwdObj) {
        return sysUserService.updatePassword(updatePwdObj);
    }

}
