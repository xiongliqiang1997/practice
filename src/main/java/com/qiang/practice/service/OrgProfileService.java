package com.qiang.practice.service;

import com.qiang.practice.model.OrgProfile;
import com.qiang.practice.utils.response.R;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: CLQ
 * @Date: 2019/7/9
 * @Description: TODO
 */
public interface OrgProfileService {

    /**
     * 获得目前的企业简介
     * @return
     */
    R getNow();

    /**
     * 修改企业简介
     * @param orgProfile
     * @return
     */
    R update(HttpServletRequest request, OrgProfile orgProfile);
}
