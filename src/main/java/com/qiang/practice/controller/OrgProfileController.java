package com.qiang.practice.controller;


import com.qiang.practice.annotation.MyLog;
import com.qiang.practice.model.OrgProfile;
import com.qiang.practice.service.OrgProfileService;
import com.qiang.practice.utils.response.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 企业简介 前端控制器
 *
 * @author 陈立强
 * @since 2019-07-05
 */
@Api(value = "企业简介", tags = "企业简介")
@RestController
public class OrgProfileController {

    @Autowired
    private OrgProfileService orgProfileService;

    /**
     * 获得当前的企业简介
     *
     * @return
     */
    @ApiOperation("获得当前的企业简介")
    @RequestMapping(value = "/api/orgProfiles", method = RequestMethod.GET)
    public R getNow() {
        return orgProfileService.getNow();
    }

    /**
     * 修改企业简介
     *
     * @param orgProfile
     * @return
     */
    @ApiOperation("修改企业简介")
    @MyLog("修改企业简介")
    @RequestMapping(value = "/api/orgProfiles", method = RequestMethod.POST)
    public R update(HttpServletRequest request, @RequestBody OrgProfile orgProfile) {
        return orgProfileService.update(request, orgProfile);
    }

}
