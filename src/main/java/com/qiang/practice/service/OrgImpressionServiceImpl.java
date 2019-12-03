package com.qiang.practice.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiang.practice.base.Constants;
import com.qiang.practice.base.TipsConstants;
import com.qiang.practice.config.ServerConfig;
import com.qiang.practice.mapper.OrgImpressionMapper;
import com.qiang.practice.model.OrgImpression;
import com.qiang.practice.utils.response.R;
import com.qiang.practice.utils.response.RUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
@Transactional
@Service
public class OrgImpressionServiceImpl implements OrgImpressionService {
    @Autowired
    private OrgImpressionMapper orgImpressionMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ServerConfig serverConfig;

    @Override
    public R getOrgImpressionByPage(Map<String, Object> paramMap) {
        //使用分页插件
        PageHelper.startPage(Integer.valueOf(paramMap.get("page").toString()), Integer.valueOf(paramMap.get("pageSize").toString()));
        //根据条件查询对应的企业印象
        List<Map<String, Object>> orgPageInfo = orgImpressionMapper.getListByCondition(paramMap);
        //将结果进行分页封装
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(orgPageInfo);
        return R.ok(pageInfo);
    }

    @Override
    public R addOrUpdate(HttpServletRequest request, OrgImpression orgImpression) {
        int rows = 0;
        //如果id为null，表示为新增，否则为修改
        if (null == orgImpression.getId()) {
            request.setAttribute("logType", "新建");
            orgImpression.setCreateDate(new Date());
            orgImpression.setSysUser(redisService.findByToken(request.getHeader(Constants.TOKEN_HEADER_PARAM)).getId());
            //新增
            rows = orgImpressionMapper.insert(orgImpression);
        } else {
            request.setAttribute("logType", "编辑");
            //设置修改时间
            orgImpression.setUpdateDate(new Date());
            //修改（只修改已经修改过的字段，没有改的字段不做操作）
            rows = orgImpressionMapper.updateByPrimaryKeySelective(orgImpression);
        }
        //返回结果
        return RUtil.isOptionOk(rows);
    }

    @Override
    public R getById(Long id) {
        //根据id查单个企业印象的详细信息
        OrgImpression orgImpression = orgImpressionMapper.selectByPrimaryKey(id);
        orgImpression.setImgPath(serverConfig.getImageUrl(orgImpression.getImgPath()));
        return R.ok(orgImpression);
    }

    @Override
    public R removeByIdList(List<Long> idList) {
        //判断参数是否为空
        if (idList.isEmpty()) {
            return R.error(TipsConstants.PARAM_NOT_ENOUGH);
        }
        //批量删除企业印象,将有效字段置为0
        return RUtil.isBatchOk(orgImpressionMapper.updateIsValidToZeroByIdList(idList), idList.size());
    }

    @Override
    public R publishByIdList(List<Long> idList) {
        //判断参数是否为空
        if (idList.isEmpty()) {
            return R.error(TipsConstants.PARAM_NOT_ENOUGH);
        }
        //批量发布企业印象,将发布字段置为1
        return RUtil.isBatchOk(orgImpressionMapper.updateIsPublishToOneByIdList(idList), idList.size());
    }

    @Override
    public R revokeByIdList(List<Long> idList) {
        //判断参数是否为空
        if (idList.isEmpty()) {
            return R.error(TipsConstants.PARAM_NOT_ENOUGH);
        }
        //批量撤销企业印象,将发布字段置为0
        return RUtil.isBatchOk(orgImpressionMapper.updateIsPublishToZeroByIdList(idList), idList.size());
    }

    @Override
    public R getByYear(Integer year) {
        //获取某一年已发布的企业印象，如果年为null，则默认查当前年
        List<OrgImpression> orgImpressionList = orgImpressionMapper.getByYear(year);
        for (OrgImpression orgImpression : orgImpressionList) {
            orgImpression.setImgPath(serverConfig.getImageUrl(orgImpression.getImgPath()));
        }
        return R.ok(orgImpressionList);
    }

    @Override
    public R getYearListWhichHaveData() {
        //获取有数据的年的列表
        return R.ok(orgImpressionMapper.getYearListWhichHaveData());
    }
}
