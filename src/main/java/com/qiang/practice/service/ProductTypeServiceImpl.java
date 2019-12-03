package com.qiang.practice.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiang.practice.base.Constants;
import com.qiang.practice.base.TipsConstants;
import com.qiang.practice.mapper.ProductMapper;
import com.qiang.practice.mapper.ProductTypeMapper;
import com.qiang.practice.model.ProductType;
import com.qiang.practice.model.ProductTypeTreeJson;
import com.qiang.practice.utils.HttpContextUtils;
import com.qiang.practice.utils.NumUtil;
import com.qiang.practice.utils.TreeJsonUtil;
import com.qiang.practice.utils.response.R;
import com.qiang.practice.utils.response.RUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: CLQ
 * @Date: 2019/8/21
 * @Description: TODO
 */
@SuppressWarnings("all")
@Transactional
@Service
public class ProductTypeServiceImpl implements ProductTypeService {
    @Autowired
    private ProductTypeMapper productTypeMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public R getByPage(Map<String, Object> paramMap) {
        //使用分页插件
        PageHelper.startPage(Integer.valueOf(paramMap.get("page").toString()), Integer.valueOf(paramMap.get("pageSize").toString()));
        //根据条件查询
        List<Map<String, Object>> orgPageInfo = productTypeMapper.getByCondition(paramMap);
        //结果封装
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(orgPageInfo);
        return R.ok(pageInfo);
    }

    @Override
    public R getTreeMenu(Long id) {
        /**
         * 获取如下格式json数据：
         * var tree = [
         *              {
         *                 id:1,
         *                 typeName: "Parent 1",
         *                 children: [
         *                           {
         *                             id:1,
         *                             typeName: "Child 1",
         *                             children: [
         *                                       {
         *                                          id:1,
         *                                          typeName: "Grandchild 1",
         *                                          children: null
         *                                       },
         *                                       {
         *                                          id:1,
         *                                          typeName: "Grandchild 2",
         *                                          children: null
         *                                       }
         *                                   ]
         *                           }
         *                       ]
         *           ]
         */
        if (null != id) {
            List<ProductTypeTreeJson> productTypeTreeJson = TreeJsonUtil.getProductTypeTreeJson(productTypeMapper, 0L, id);
            return R.ok(productTypeTreeJson);
        } else {
            String treeMenuJson = redisService.get(Constants.PRODUCT_TYPE_TREE_MENU_JSON);
            if (StringUtils.isNotEmpty(treeMenuJson)) {
                return R.ok(JSONObject.parseObject(treeMenuJson, new TypeReference<ArrayList<ProductTypeTreeJson>>() {
                }));
            } else {
                List<ProductTypeTreeJson> treeMenuJsonList = TreeJsonUtil.getProductTypeTreeJson(productTypeMapper, 0L);
                if (!treeMenuJsonList.isEmpty()) {
                    treeMenuJson = JSONObject.toJSONString(treeMenuJsonList);
                    redisService.set(Constants.PRODUCT_TYPE_TREE_MENU_JSON,
                            treeMenuJson, 15L, TimeUnit.MINUTES);
                }
                return R.ok(treeMenuJsonList);
            }
        }

    }

    @Override
    public R addOrUpdate(ProductType productType) {
        //首先判断分类名是否重复
        if (productTypeMapper.getTypeNameCount(productType.getTypeName(), productType.getId(), productType.getpId()) > 0) {
            HttpContextUtils.getHttpServletRequest().setAttribute("notLog", 1);
            Map<String, Object> pType = productTypeMapper.getById(productType.getpId());
            return R.error("在 \"" + pType.get("typeName").toString() + "\" 分类下，该名称已存在");
        }
        int rows = 0;
        //如果商品分类为空，表示要将这条记录置为顶级分类
        productType.setpId(NumUtil.ifNumNullChangeToZero(productType.getpId()));
        //如果id为null，表示为新增，否则为修改
        if (null == productType.getId()) {
            HttpContextUtils.getHttpServletRequest().setAttribute("logType", "新建");
            productType.setCreateDate(new Date());
            //先查一下目前的最大排序号, 然后在此最大排序号基础上+1
            productType.setOrderby(NumUtil.getOrderbyByMaxOrderby(productTypeMapper.getMaxOrderby()));
            //新增
            rows = productTypeMapper.insert(productType);
        } else {
            HttpContextUtils.getHttpServletRequest().setAttribute("logType", "编辑");
            //设置修改时间
            productType.setUpdateDate(new Date());
            //修改
            rows = productTypeMapper.updateByPrimaryKeySelective(productType);
        }
        TreeJsonUtil.updateProductTypeTreeMenuJson(redisTemplate, productTypeMapper);
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
            //批量删除商品分类,将每个分类及其子分类的有效字段置为0
            productTypeMapper.updateIsValidToZeroById(id);
            //同时将每个分类下的商品都删除
            productMapper.updateIsValidToZeroByTypeId(id);
        }
        TreeJsonUtil.updateProductTypeTreeMenuJson(redisTemplate, productTypeMapper);
        return R.okDefault();
    }

    @Override
    public R getById(Long id) {
        //根据id查单个商品分类的详细信息
        return R.ok(productTypeMapper.getById(id));
    }

    @Override
    public R getSortData(Long id) {
        //根据id查询其下第一层子分类，若id为null，则默认查一级分类
        return R.ok(productTypeMapper.getFirstSonById(id));
    }

    @Override
    public R sort(Map<String, Object> param) {
        //拿到参数
        List<Map<String, Object>> oldSort = (List<Map<String, Object>>) param.get("oldSortList");
        List<Map<String, Object>> newSort = (List<Map<String, Object>>) param.get("newSortList");
        int flag = 1, count = 0;
        for (int i = 0; i < oldSort.size(); i++) {
            if (!oldSort.get(i).get("id").toString().equals(newSort.get(i).get("id").toString())) {
                //如果该id的orderby字段变了
                oldSort.get(i).put("id", newSort.get(i).get("id").toString());
                //根据id修改orderby字段
                int result = productTypeMapper.sort(oldSort.get(i));
                if (result != 1) {
                    flag = 0;
                }
            }
        }
        TreeJsonUtil.updateProductTypeTreeMenuJson(redisTemplate, productTypeMapper);
        return RUtil.isOptionOk(flag);
    }
}
