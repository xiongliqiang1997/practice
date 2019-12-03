package com.qiang.practice.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiang.practice.base.Constants;
import com.qiang.practice.base.TipsConstants;
import com.qiang.practice.config.ServerConfig;
import com.qiang.practice.mapper.*;
import com.qiang.practice.model.*;
import com.qiang.practice.utils.HttpContextUtils;
import com.qiang.practice.utils.request.ReqParamUtil;
import com.qiang.practice.utils.request.RequestHeaderUtil;
import com.qiang.practice.utils.response.R;
import com.qiang.practice.utils.response.RUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/8/1
 * @Description: TODO
 */
@SuppressWarnings("all")
@Transactional
@Service
public class PlateInfoServiceImpl implements PlateInfoService {
    @Autowired
    private PlateMapper plateMapper;
    @Autowired
    private PlateInfoMapper plateInfoMapper;
    @Autowired
    private OrgImpressionMapper orgImpressionMapper;
    @Autowired
    private OrgAchievementMapper orgAchievementMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ReadLogMapper readLogMapper;
    @Autowired
    private ServerConfig serverConfig;

    @Override
    public R getMenu() {
        //获取左侧菜单所需要的json格式数据
        return R.ok(plateMapper.getPlateNameMenu());
    }

    @Override
    public R getByPage(Map<String, Object> paramMap) {
        //判断请求是否来自官网
        if (StringUtils.equals(Constants.TYPE_WEB_PARAM, RequestHeaderUtil.getType())) {
            paramMap.put("type", RequestHeaderUtil.getType());
        }
        //使用分页插件
        PageHelper.startPage(Integer.valueOf(paramMap.get("page").toString()), Integer.valueOf(paramMap.get("pageSize").toString()));
        //根据条件查询对应的信息发布列表
        List<Map<String, Object>> plateInfoList = plateInfoMapper.getListByCondition(paramMap);
        for (Map<String, Object> plateInfo : plateInfoList) {
            if (plateInfo.get("imgPath") != null) {
                plateInfo.put("imgPath", serverConfig.getImageUrl(plateInfo.get("imgPath").toString()));
            }
        }
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(plateInfoList);
        return R.ok(pageInfo);
    }

    @Override
    public R addOrUpdate(HttpServletRequest request, PlateInfo plateInfo) {
        int rows = 0;
        //如果id为null，表示为新增，否则为修改
        if (null == plateInfo.getId()) {
            request.setAttribute("logType", "新建");
            //设置创建时间和创建人
            plateInfo.setCreateDate(new Date());
            long sysUser = redisService.findByToken(request.getHeader(Constants.TOKEN_HEADER_PARAM)).getId();
            plateInfo.setSysUser(sysUser);
            //设置初始阅读数量
            plateInfo.setReadNum(0);
            //设置发布日期
            if (plateInfo.getIsPublish() == (byte) 1) {
                plateInfo.setPublishDate(new Date());
            }
            plateInfo.setIsValid((byte) 1);
            //判断是否需要同步到企业印象
            if (plateInfo.getOrgImpression() == 1) {
                //将数据同步到企业印象
                OrgImpression orgImpression = ReqParamUtil.copyPlateInfoToOrgImpression(plateInfo, sysUser);
                if (orgImpressionMapper.insert(orgImpression) != 1) {
                    return R.error(TipsConstants.SYNCHRONIZE_ORG_IMPRESSION_FAILURE);
                }
            }
            //判断是否需要同步到企业成果
            if (plateInfo.getOrgAchievement() == 1) {
                //将数据同步到企业成果
                OrgAchievement orgAchievement = ReqParamUtil.copyPlateInfoToOrgAchievement(plateInfo,
                        redisService.findByToken(HttpContextUtils.getHttpServletRequest().getHeader(Constants.TOKEN_HEADER_PARAM)).getId());
                if (orgAchievementMapper.insert(orgAchievement) != 1) {
                    return R.error(TipsConstants.SYNCHRONIZE_ORG_ACHIEVEMENT_FAILURE);
                }
            }
            //新增信息
            rows = plateInfoMapper.insert(plateInfo);
        } else {
            request.setAttribute("logType", "编辑");
            //修改操作，设置修改时间
            plateInfo.setUpdateDate(new Date());
            //判断是否要发布
            if (plateInfo.getIsPublish() == 1) {
                //获取发布日期
                Date publishDate = plateInfoMapper.getPublishDateById(plateInfo.getId());
                //若之前已经发布过了，则不再设置发布日期；若之前没发布过，则设置发布日期
                if (publishDate == null) {
                    plateInfo.setPublishDate(new Date());
                }
            }
            //修改
            rows = plateInfoMapper.updateByPrimaryKeySelective(plateInfo);
        }
        //返回结果
        return RUtil.isOptionOk(rows);
    }

    @Override
    public R getById(HttpServletRequest request, Long id) {
        //如果请求来自官网，阅读数量加一
        if (StringUtils.equals(Constants.TYPE_WEB_PARAM, request.getHeader(Constants.TYPE_HEADER_PARAM))) {
            ReadLog readLog = new ReadLog();
            readLog.setPlateInfo(id);
            String token = request.getHeader(Constants.TOKEN_HEADER_PARAM);
            if (token == null) {
                //游客阅读
                readLog.setSysUser(0L);
            } else {
                //用户阅读
                SysUser sysUser = redisService.findByToken(token);
                if (sysUser == null) { //用户权限刚好过期，认定为游客阅读
                    readLog.setSysUser(0L);
                } else {
                    readLog.setSysUser(sysUser.getId());
                }
            }
            readLog.setReadTime(new Date());
            readLogMapper.insert(readLog);
        }
        //根据id查单个信息
        Map<String, Object> plateInfo = plateInfoMapper.getDetailById(id);
        if (plateInfo.get("imgPath") != null) {
            plateInfo.put("imgPath", serverConfig.getImageUrl(plateInfo.get("imgPath").toString()));
        }
        return R.ok(plateInfo);
    }

    @Override
    public R removeByIdList(List<Long> idList) {
        //判断参数是否为空
        if (idList.isEmpty()) {
            return R.error(TipsConstants.PARAM_NOT_ENOUGH);
        }
        //批量删除信息,将有效字段置为0
        return RUtil.isBatchOk(plateInfoMapper.updateIsValidToZeroByIdList(idList), idList.size());
    }

    @Override
    public R publishByIdList(List<Long> idList) {
        //判断参数是否为空
        if (idList.isEmpty()) {
            return R.error(TipsConstants.PARAM_NOT_ENOUGH);
        }
        //批量发布,将发布字段置为1
        return RUtil.isBatchOk(plateInfoMapper.updateIsPublishToOneByIdList(idList), idList.size());
    }

    @Override
    public R revokeByIdList(List<Long> idList) {
        //判断参数是否为空
        if (idList.isEmpty()) {
            return R.error(TipsConstants.PARAM_NOT_ENOUGH);
        }
        //批量撤销,将发布字段置为0
        return RUtil.isBatchOk(plateInfoMapper.updateIsPublishToZeroByIdList(idList), idList.size());
    }

    @Override
    public R getFirstFourEveryPlate() {
        //获取各板块前四条信息
        List<Map<String, Object>> firstFourPlateInfoList = plateInfoMapper.getFirstFourEveryPlate();
        for (Map<String, Object> plateInfo : firstFourPlateInfoList) {
            if (plateInfo.get("imgPath") != null) {
                plateInfo.put("imgPath", serverConfig.getImageUrl(plateInfo.get("imgPath").toString()));
            }
        }
        return R.ok(firstFourPlateInfoList);
    }
}
