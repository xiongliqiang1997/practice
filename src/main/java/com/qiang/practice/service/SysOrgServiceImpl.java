package com.qiang.practice.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiang.practice.base.Constants;
import com.qiang.practice.base.TipsConstants;
import com.qiang.practice.mapper.SysOrgMapper;
import com.qiang.practice.mapper.SysUserMapper;
import com.qiang.practice.model.SysOrg;
import com.qiang.practice.utils.NumUtil;
import com.qiang.practice.utils.TreeJsonUtil;
import com.qiang.practice.utils.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: CLQ
 * @Date: 2019/7/9
 * @Description: TODO
 */
@SuppressWarnings("all")
@CacheConfig(cacheNames = {"practiceCache"})
@Transactional
@Service
public class SysOrgServiceImpl implements SysOrgService {

    @Autowired
    private SysOrgMapper sysOrgMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public R getByPage(Map<String, Object> paramMap) {
        //使用分页插件
        PageHelper.startPage(Integer.valueOf(paramMap.get("page").toString()), Integer.valueOf(paramMap.get("pageSize").toString()));
        //根据条件分页查询对应的组织机构
        List<Map<String, Object>> orgPageInfo = sysOrgMapper.getListByPage(paramMap);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(orgPageInfo);
        return R.ok(pageInfo);
    }

    @Override
    public R addOrUpdate(HttpServletRequest request, SysOrg sysOrg) {
        if (!checkOrgNameExist(sysOrg.getOrgName(), sysOrg.getId()).isRes()) {
            request.setAttribute("notLog", 1);
            return R.error(TipsConstants.ORG_NAME_HAS_EXISTED);
        }
        int rows = 0;
        //如果上级机构id为空，表示要将这条记录置为顶级机构，将pid设为0
        sysOrg.setpId(NumUtil.ifNumNullChangeToZero(sysOrg.getpId()));
        //如果id为null，表示为新增，否则为修改
        if (null == sysOrg.getId()) {
            request.setAttribute("logType", "新建");
            sysOrg.setCreateDate(new Date());
            //先查一下目前的最大排序号, 然后在此最大排序号基础上+1
            sysOrg.setOrderby(NumUtil.getOrderbyByMaxOrderby(sysOrgMapper.getMaxOrderby()));
            //新增组织机构
            rows = sysOrgMapper.addOneSysOrg(sysOrg);

        } else {
            request.setAttribute("logType", "编辑");
            //修改操作，设置修改时间
            sysOrg.setUpdateDate(new Date());
            //修改组织机构信息（只修改已经修改过的字段，没有改的字段不做操作）
            rows = sysOrgMapper.updateByPrimaryKeySelective(sysOrg);
        }
        TreeJsonUtil.updateTreeMenuJson(redisTemplate, sysOrgMapper);
        //返回结果
        return RUtil.isOptionOk(rows);
    }

    @Override
    public R removeByIdList(List<Long> idList) {
        //判断参数是否为空
        if (idList.isEmpty()) {
            return R.error(TipsConstants.PARAM_NOT_ENOUGH);
        }
        for (Long id : idList) {
            //批量删除组织机构,将每个机构及其子机构们的有效字段置为0
            sysOrgMapper.updateIsValidToZeroById(id);
            //同时将每个机构下的员工都删除
            sysUserMapper.updateIsValidToZeroByOrgId(id);
        }
        TreeJsonUtil.updateTreeMenuJson(redisTemplate, sysOrgMapper);
        return R.okDefault();
    }

    @Override
    public R getTreeMenu(Long id) {
        /**
         * 获取如下格式json数据：
         * var tree = [
         *              {
         *                  id:1,
         *                 orgName: "Parent 1",
         *                 children: [
         *                           {
         *                              id:1,
         *                             orgName: "Child 1",
         *                             children: [
         *                                       {
         *                                          id:1,
         *                                         orgName: "Grandchild 1"
         *                                       },
         *                                       {
         *                                         orgName: "Grandchild 2"
         *                                       }
         *                                   ]
         *                           }
         *                       ]
         *           ]
         */
        if (null != id) {
            //如果传了id，则代表是在编辑页加载树形菜单，要忽略该id以及该id的子机构
            List<SysOrgTreeMenuJson> sysOrgTreeMenuJsons = TreeJsonUtil.getTreeMenuJson(sysOrgMapper, 0L, id);
            return R.ok(sysOrgTreeMenuJsons);
        } else {
            //不传id，查全部
            //先看看redis里有没有
            String treeMenuJson = redisTemplate.opsForValue().get(Constants.TREE_MENU_JSON);
            List<SysOrgTreeMenuJson> sysOrgTreeMenuJsons = null;
            if (null != treeMenuJson) {
                //redis里有的话，直接返回
                sysOrgTreeMenuJsons = JSONObject.parseObject(treeMenuJson, new TypeReference<ArrayList<SysOrgTreeMenuJson>>() {
                });
                return R.ok(sysOrgTreeMenuJsons);
            } else {
                //redis里没有，去数据库查
                sysOrgTreeMenuJsons = TreeJsonUtil.getTreeMenuJson(sysOrgMapper, 0L);
                if (null != sysOrgTreeMenuJsons) {
                    //将从数据库查出来的数据先放到redis里，然后返回
                    treeMenuJson = JSONObject.toJSONString(sysOrgTreeMenuJsons);
                    redisTemplate.opsForValue().set(Constants.TREE_MENU_JSON,
                            treeMenuJson,
                            15L,
                            TimeUnit.MINUTES);
                }
                return R.ok(sysOrgTreeMenuJsons);
            }
        }

    }

    @Override
    public R getMyTreeMenu(Long id) {
        if (null != id) {
            List<SysOrgTreeMenuJsonMy> sysOrgTreeMenuJsons = TreeJsonUtil.getMyTreeMenuJson(sysOrgMapper, 0L, id);
            return R.ok(sysOrgTreeMenuJsons);
        } else {
            String treeMenuJson = redisTemplate.opsForValue().get(Constants.TREE_MENU_JSON_MY);
            List<SysOrgTreeMenuJsonMy> sysOrgTreeMenuJsons = null;
            if (null != treeMenuJson) {
                sysOrgTreeMenuJsons = JSONObject.parseObject(treeMenuJson, new TypeReference<ArrayList<SysOrgTreeMenuJsonMy>>() {
                });
                return R.ok(sysOrgTreeMenuJsons);
            } else {
                sysOrgTreeMenuJsons = TreeJsonUtil.getMyTreeMenuJson(sysOrgMapper, 0L);
                if (null != sysOrgTreeMenuJsons) {
                    treeMenuJson = JSONObject.toJSONString(sysOrgTreeMenuJsons);
                    redisTemplate.opsForValue().set(Constants.TREE_MENU_JSON_MY,
                            treeMenuJson,
                            15L,
                            TimeUnit.MINUTES);
                }
                return R.ok(sysOrgTreeMenuJsons);
            }
        }

    }

    @Override
    public Validator checkOrgNameExist(String orgName, Long id) {
        //查看该机构名称是否已存在
        return RUtil.isNameExist(sysOrgMapper.getOrgNameCount(orgName, id));
    }

    @Override
    public SysOrg getById(Long id) {
        return sysOrgMapper.getById(id);
    }

    @Override
    public R getSysOrgSortData(Long id) {
        //参数处理
        id = NumUtil.ifIdNull(id);
        //根据id查询其下第一层子机构
        return R.ok(sysOrgMapper.getFirstSonOrgByPId(id));
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
                int result = sysOrgMapper.sort(oldSort.get(i));
                if (result != 1) {
                    flag = 0;
                }
            }
        }
        if (flag == 1) {
            TreeJsonUtil.updateTreeMenuJson(redisTemplate, sysOrgMapper);
        }
        return RUtil.isOptionOk(flag);
    }

}
