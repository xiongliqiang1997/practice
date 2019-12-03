package com.qiang.practice.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiang.practice.base.Constants;
import com.qiang.practice.base.TipsConstants;
import com.qiang.practice.config.ServerConfig;
import com.qiang.practice.mapper.OrgAchievementMapper;
import com.qiang.practice.model.OrgAchievement;
import com.qiang.practice.utils.HttpContextUtils;
import com.qiang.practice.utils.response.R;
import com.qiang.practice.utils.response.RUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/8/20
 * @Description: TODO
 */
@SuppressWarnings("all")
@Transactional
@Service
public class OrgAchievementServiceImpl implements OrgAchievementService {
    @Autowired
    private OrgAchievementMapper orgAchievementMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ServerConfig serverConfig;

    @Override
    public R getByPage(Map<String, Object> paramMap) {
        //使用分页插件
        PageHelper.startPage(Integer.valueOf(paramMap.get("page").toString()), Integer.valueOf(paramMap.get("pageSize").toString()));
        //根据条件查询企业成果
        List<OrgAchievement> orgPageInfo = orgAchievementMapper.getByCondition(paramMap);
        //将结果进行分页封装
        PageInfo<OrgAchievement> pageInfo = new PageInfo<>(orgPageInfo);
        return R.ok(pageInfo);
    }

    @Override
    public R addOrUpdate(OrgAchievement orgAchievement) {
        int rows = 0;
        //如果id为null，表示为新增，否则为修改
        if (null == orgAchievement.getId()) {
            HttpContextUtils.getHttpServletRequest().setAttribute("logType", "新建");
            orgAchievement.setCreateDate(new Date());
            orgAchievement.setSysUser(redisService.findByToken(
                    HttpContextUtils.getHttpServletRequest().getHeader(Constants.TOKEN_HEADER_PARAM)).getId());
            //新增
            rows = orgAchievementMapper.insert(orgAchievement);
        } else {
            HttpContextUtils.getHttpServletRequest().setAttribute("logType", "编辑");
            //修改操作，设置修改时间
            orgAchievement.setUpdateDate(new Date());
            //修改
            rows = orgAchievementMapper.updateByPrimaryKeySelective(orgAchievement);
        }
        //返回结果
        return RUtil.isOptionOk(rows);
    }

    @Override
    public R getById(Long id) {
        //根据id查单个企业成果的详细信息
        OrgAchievement orgAchievement = orgAchievementMapper.selectByPrimaryKey(id);
        orgAchievement.setImgPath(serverConfig.getImageUrl(orgAchievement.getImgPath()));
        return R.ok(orgAchievement);
    }

    @Override
    public R removeByIdList(List<Long> idList) {
        //判断参数是否为空
        if (idList.isEmpty()) {
            return R.error(TipsConstants.PARAM_NOT_ENOUGH);
        }
        //批量删除企业成果,将有效字段置为0
        return RUtil.isBatchOk(orgAchievementMapper.updateIsValidToZeroByIdList(idList), idList.size());
    }

    @Override
    public R publishByIdList(List<Long> idList) {
        //判断参数是否为空
        if (idList.isEmpty()) {
            return R.error(TipsConstants.PARAM_NOT_ENOUGH);
        }
        //批量发布企业成果,将发布字段置为1
        return RUtil.isBatchOk(orgAchievementMapper.updateIsPublishToOneByIdList(idList), idList.size());
    }

    @Override
    public R revokeByIdList(List<Long> idList) {
        //判断参数是否为空
        if (idList.isEmpty()) {
            return R.error(TipsConstants.PARAM_NOT_ENOUGH);
        }
        //批量撤销企业成果,将发布字段置为0
        return RUtil.isBatchOk(orgAchievementMapper.updateIsPublishToZeroByIdList(idList), idList.size());
    }

    @Override
    public R getAll() {
        //获取所有已发布的企业成果
        List<OrgAchievement> orgAchievementList =  orgAchievementMapper.getAll();
        for (OrgAchievement orgAchievement : orgAchievementList) {
            orgAchievement.setImgPath(serverConfig.getImageUrl(orgAchievement.getImgPath()));
        }
        return R.ok(orgAchievementList);
    }
}
