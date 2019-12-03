package com.qiang.practice.service;

import com.qiang.practice.base.Constants;
import com.qiang.practice.base.TipsConstants;
import com.qiang.practice.config.ServerConfig;
import com.qiang.practice.mapper.PrimaryMemberMapper;
import com.qiang.practice.model.PrimaryMember;
import com.qiang.practice.utils.NumUtil;
import com.qiang.practice.utils.response.R;
import com.qiang.practice.utils.response.RUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/7/31
 * @Description: TODO
 */
@SuppressWarnings("all")
@Transactional
@Service
public class PrimaryMemberServiceImpl implements PrimaryMemberService {
    @Autowired
    private PrimaryMemberMapper primaryMemberMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ServerConfig serverConfig;

    @Override
    public R getByCondition(Map<String, Object> paramMap) {
        //查询，若各查询条件不为null，则为动态条件查询
        List<Map<String, Object>> primaryMemberList = primaryMemberMapper.getByCondition(paramMap);
        //为每个图片的url加上ip和端口
        for (Map<String, Object> primaryMember : primaryMemberList) {
            primaryMember.put("imgPath", serverConfig.getImageUrl(primaryMember.get("imgPath").toString()));
        }
        return R.ok(primaryMemberList);
    }

    @Override
    public R addOrUpdate(HttpServletRequest request, PrimaryMember primaryMember) {
        int rows = 0;
        //如果id为null，表示为新增，否则为修改
        if (null == primaryMember.getId()) {
            request.setAttribute("logType", "新建");
            primaryMember.setCreateDate(new Date());
            primaryMember.setSysUser(redisService.findByToken(request.getHeader(Constants.TOKEN_HEADER_PARAM)).getId());
            //先查一下目前的最大排序号, 然后在此最大排序号基础上+1
            primaryMember.setOrderby(NumUtil.getOrderbyByMaxOrderby(primaryMemberMapper.getMaxOrderby()));
            //新增
            rows = primaryMemberMapper.insert(primaryMember);
        } else {
            request.setAttribute("logType", "编辑");
            //修改操作，设置修改时间
            primaryMember.setUpdateDate(new Date());
            //修改（只修改已经修改过的字段，没有改的字段不做操作）
            rows = primaryMemberMapper.updateByPrimaryKeySelective(primaryMember);
        }
        //返回结果
        return RUtil.isOptionOk(rows);
    }

    @Override
    public R getById(Long id) {
        //根据id获取详情
        PrimaryMember primaryMember = primaryMemberMapper.selectByPrimaryKey(id);
        //图片url拼串
        primaryMember.setImgPath(serverConfig.getImageUrl(primaryMember.getImgPath()));
        return R.ok(primaryMember);
    }

    @Override
    public R removeByIdList(List<Long> idList) {
        //判断参数是否为空
        if (idList.isEmpty()) {
            return R.error(TipsConstants.PARAM_NOT_ENOUGH);
        }
        //批量删除主要成员,将有效字段置为0
        return RUtil.isBatchOk(primaryMemberMapper.updateIsValidToZeroByIdList(idList), idList.size());
    }

    @Override
    public R getSortData() {
        //获取需要排序的主要成员数据
        return R.ok(primaryMemberMapper.getSortData());
    }

    @Override
    public R sort(Map<String, Object> param) {
        List<Map<String, Object>> oldSort = (List<Map<String, Object>>) param.get("oldSortList");
        List<Map<String, Object>> newSort = (List<Map<String, Object>>) param.get("newSortList");
        int flag = 1, count = 0;
        for (int i = 0; i < oldSort.size(); i++) {
            if (!oldSort.get(i).get("id").toString().equals(newSort.get(i).get("id").toString())) {
                oldSort.get(i).put("id", newSort.get(i).get("id").toString());
                //根据id修改orderby字段
                int result = primaryMemberMapper.sort(oldSort.get(i));
                if (result != 1) {
                    flag = 0;
                }
            }
        }
        return RUtil.isOptionOk(flag);
    }
}
