package com.qiang.practice.service;

import com.qiang.practice.base.Constants;
import com.qiang.practice.mapper.OrgProfileMapper;
import com.qiang.practice.model.OrgProfile;
import com.qiang.practice.utils.response.R;
import com.qiang.practice.utils.response.RUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Author: CLQ
 * @Date: 2019/7/10
 * @Description: TODO
 */
@SuppressWarnings("all")
@Transactional
@Service
public class OrgProfileServiceImpl implements OrgProfileService {

    @Autowired
    private OrgProfileMapper orgProfileMapper;
    @Autowired
    private RedisService redisService;

    /**
     * 获得目前的企业简介
     * @return
     */
    @Override
    public R getNow() {
        //获得目前的企业简介
        return R.ok(orgProfileMapper.getNow());
    }

    /**
     * 修改企业简介
     * @param orgProfile 简介内容
     * @return
     */
    @Override
    public R update(HttpServletRequest request, OrgProfile orgProfile) {
        //将修改人和修改时间补充好
        orgProfile.setSysUser(redisService.findByToken(request.getHeader(Constants.TOKEN_HEADER_PARAM)).getId());
        orgProfile.setRevisionDate(new Date());
        return RUtil.isAddOk(orgProfileMapper.updateByPrimaryKeyWithBLOBs(orgProfile));
    }
}
