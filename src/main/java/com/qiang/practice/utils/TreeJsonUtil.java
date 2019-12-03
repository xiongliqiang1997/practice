package com.qiang.practice.utils;

import com.alibaba.fastjson.JSONObject;
import com.qiang.practice.base.Constants;
import com.qiang.practice.mapper.ProductTypeMapper;
import com.qiang.practice.mapper.SysOrgMapper;
import com.qiang.practice.model.ProductType;
import com.qiang.practice.model.ProductTypeTreeJson;
import com.qiang.practice.model.SysOrg;
import com.qiang.practice.model.vo.SysOrgPNameVO;
import com.qiang.practice.utils.response.SysOrgTreeMenuJson;
import com.qiang.practice.utils.response.SysOrgTreeMenuJsonMy;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: CLQ
 * @Date: 2019/7/11
 * @Description: 树形菜单Json数据处理工具类
 */
@SuppressWarnings("all")
public class TreeJsonUtil {

    public static List<SysOrgTreeMenuJson> getTreeMenuJson(SysOrgMapper sysOrgMapper, Long pId) {
        List<SysOrgTreeMenuJson> treeMenuJsons = new ArrayList<>();
        //根据上级组织机构id获取节点
        List<SysOrg> sysOrgList = sysOrgMapper.getSysOrgByPId(pId);
        if (sysOrgList.size() == 0) {
            return null;
        }
        for (SysOrg org : sysOrgList) {
            SysOrgTreeMenuJson treeMenuJson = new SysOrgTreeMenuJson();
            treeMenuJson.setOrgName(org.getOrgName());
            treeMenuJson.setId(org.getId());
            treeMenuJson.setRemark(org.getRemark());
            //根据该节点的id来查询该节点的子节点
            List<SysOrg> sonNodeList = sysOrgMapper.getSysOrgByPId(org.getId());
            if (sonNodeList.size() > 0) {
                List<SysOrgTreeMenuJson> sonTreeMenuList = getTreeMenuJson(sysOrgMapper, org.getId());
                treeMenuJson.setChildren(sonTreeMenuList);
            }
            treeMenuJsons.add(treeMenuJson);
        }

        return treeMenuJsons;
    }

    public static List<SysOrgTreeMenuJson> getTreeMenuJson(SysOrgMapper sysOrgMapper, Long pId, Long id) {
        List<SysOrgTreeMenuJson> treeMenuJsons = new ArrayList<>();
        //根据上级组织机构id获取节点
        List<SysOrg> sysOrgList = sysOrgMapper.getSysOrgByPId(pId);
        if (sysOrgList.size() == 0) {
            return null;
        }
        for (SysOrg org : sysOrgList) {
            if (org.getId() == id) {
                continue;
            }
            SysOrgTreeMenuJson treeMenuJson = new SysOrgTreeMenuJson();
            treeMenuJson.setOrgName(org.getOrgName());
            treeMenuJson.setId(org.getId());
            //根据该节点的id来查询该节点的子节点
            List<SysOrg> sonNodeList = sysOrgMapper.getSysOrgByPId(org.getId());
            if (sonNodeList.size() > 0) {
                List<SysOrgTreeMenuJson> sonTreeMenuList = getTreeMenuJson(sysOrgMapper, org.getId(), id);
                treeMenuJson.setChildren(sonTreeMenuList);
            }
            treeMenuJsons.add(treeMenuJson);
        }

        return treeMenuJsons;
    }

    public static List<SysOrgTreeMenuJsonMy> getMyTreeMenuJson(SysOrgMapper sysOrgMapper, Long pId) {
        List<SysOrgTreeMenuJsonMy> treeMenuJsons = new ArrayList<>();
        //根据上级组织机构id获取节点
        List<SysOrg> sysOrgList = sysOrgMapper.getSysOrgByPId(pId);
        if (sysOrgList.size() == 0) {
            return null;
        }
        for (SysOrg org : sysOrgList) {
            SysOrgTreeMenuJsonMy treeMenuJson = new SysOrgTreeMenuJsonMy();
            treeMenuJson.setText(org.getOrgName());
            treeMenuJson.setId(org.getId());
            //根据该节点的id来查询该节点的子节点
            List<SysOrg> sonNodeList = sysOrgMapper.getSysOrgByPId(org.getId());
            if (sonNodeList.size() > 0) {
                List<SysOrgTreeMenuJsonMy> sonTreeMenuList = getMyTreeMenuJson(sysOrgMapper, org.getId());
                treeMenuJson.setNodes(sonTreeMenuList);
            }
            treeMenuJsons.add(treeMenuJson);
        }

        return treeMenuJsons;
    }

    public static List<SysOrgTreeMenuJsonMy> getMyTreeMenuJson(SysOrgMapper sysOrgMapper, Long pId, Long id) {
        List<SysOrgTreeMenuJsonMy> treeMenuJsons = new ArrayList<>();
        //根据上级组织机构id获取节点
        List<SysOrg> sysOrgList = sysOrgMapper.getSysOrgByPId(pId);
        if (sysOrgList.size() == 0) {
            return null;
        }
        for (SysOrg org : sysOrgList) {
            if (org.getId() == id) {
                continue;
            }
            SysOrgTreeMenuJsonMy treeMenuJson = new SysOrgTreeMenuJsonMy();
            treeMenuJson.setText(org.getOrgName());
            treeMenuJson.setId(org.getId());
            //根据该节点的id来查询该节点的子节点
            List<SysOrg> sonNodeList = sysOrgMapper.getSysOrgByPId(org.getId());
            if (sonNodeList.size() > 0) {
                List<SysOrgTreeMenuJsonMy> sonTreeMenuList = getMyTreeMenuJson(sysOrgMapper, org.getId(), id);
                treeMenuJson.setNodes(sonTreeMenuList);
            }
            treeMenuJsons.add(treeMenuJson);
        }

        return treeMenuJsons;
    }

    public static List<SysOrgPNameVO> getSysorgList(SysOrgMapper sysOrgMapper, Map<String, Object> paramMap) {
//        Integer pageSize
        List<SysOrgPNameVO> resultList = new ArrayList<>();
        //拿到该父节点
        List<SysOrgPNameVO> sysOrgList = sysOrgMapper.getSysOrgByIdOrName(paramMap);
//        resultList.add(sysOrgPNameVO);
        //根据上级组织机构id获取节点
        /*List<SysOrgPNameVO> sysOrgList = sysOrgMapper.getValidSysOrgByPIdOrOrgNameOrRemark1(paramMap);
        if (sysOrgList.size() == 0) {
            return null;
        }*/
        Map<String, Object> param = new HashMap<>();
        for (SysOrgPNameVO orgVO : sysOrgList) {
            resultList.add(orgVO);
            List<SysOrgPNameVO> flagList = sysOrgMapper.getSysOrgPNameVOByPId(orgVO.getId());
            if (0 != flagList.size()) {
                param.put("pId", orgVO.getId());
                flagList = getSysorgList(sysOrgMapper, param);
                resultList.addAll(flagList);
            }
        }

        return resultList;
    }

    public static void updateTreeMenuJson(RedisTemplate<String, String> redisTemplate, SysOrgMapper sysOrgMapper) {
        redisTemplate.delete(Constants.TREE_MENU_JSON);
        redisTemplate.delete(Constants.TREE_MENU_JSON_MY);
        List<SysOrgTreeMenuJsonMy> sysOrgTreeMenuJsonsMy = TreeJsonUtil.getMyTreeMenuJson(sysOrgMapper, 0L);
        redisTemplate.opsForValue().set(Constants.TREE_MENU_JSON_MY,
                JSONObject.toJSONString(sysOrgTreeMenuJsonsMy),
                15L,
                TimeUnit.MINUTES);
        List<SysOrgTreeMenuJson> sysOrgTreeMenuJsons = TreeJsonUtil.getTreeMenuJson(sysOrgMapper, 0L);
        redisTemplate.opsForValue().set(Constants.TREE_MENU_JSON,
                JSONObject.toJSONString(sysOrgTreeMenuJsons),
                15L,
                TimeUnit.MINUTES);
    }

    public static void updateProductTypeTreeMenuJson(RedisTemplate<String, String> redisTemplate, ProductTypeMapper productTypeMapper) {
        redisTemplate.delete(Constants.PRODUCT_TYPE_TREE_MENU_JSON);
        List<ProductTypeTreeJson> treeMenuJsons = TreeJsonUtil.getProductTypeTreeJson(productTypeMapper, 0L);
        redisTemplate.opsForValue().set(Constants.PRODUCT_TYPE_TREE_MENU_JSON,
                JSONObject.toJSONString(treeMenuJsons),
                15L,
                TimeUnit.MINUTES);
    }

    public static String dealLineBreaks(String text) {
        if (text.contains("\n")) {
            if (text.contains("\r\n")) {
                text = text.replace("\r\n", "<br/>");
            } else {
                text = text.replace("\n", "<br/>");
            }
        }
        return text;
    }

    public static String dealBr(String text) {
        if (text.contains("<br/>")) {
            text = text.replace("<br/>", "\n");
        }
        return text;
    }

    /**
     * 根据上级分类id 获取商品分类树形菜单（将自己以及自己的子菜单排除在外）
     * @param productTypeMapper
     * @param pId
     * @param id
     * @return
     */
    public static List<ProductTypeTreeJson> getProductTypeTreeJson(ProductTypeMapper productTypeMapper, long pId, Long id) {
        List<ProductTypeTreeJson> treeJson = new ArrayList<>();
        //根据上级分类id获取节点
        List<ProductType> productTypeList = productTypeMapper.getByPId(pId);
        if (productTypeList.isEmpty()) {
            return null;
        }
        for (ProductType productType : productTypeList) {
            if (productType.getId() == id) {
                continue;
            }
            ProductTypeTreeJson tempTreeJson = new ProductTypeTreeJson();
            tempTreeJson.setTypeName(productType.getTypeName());
            tempTreeJson.setId(productType.getId());
            //根据该节点的id来查询该节点的子节点
            List<ProductType> sonNodeList = productTypeMapper.getByPId(productType.getId());
            if (!sonNodeList.isEmpty()) {
                List<ProductTypeTreeJson> sonTreeMenuList = getProductTypeTreeJson(productTypeMapper, productType.getId(), id);
                tempTreeJson.setChildren(sonTreeMenuList);
            }
            treeJson.add(tempTreeJson);
        }

        return treeJson;
    }

    /**
     * 根据上级分类id 获取商品分类树形菜单
     * @param productTypeMapper
     * @param pId
     * @param id
     * @return
     */
    public static List<ProductTypeTreeJson> getProductTypeTreeJson(ProductTypeMapper productTypeMapper, long pId) {
        List<ProductTypeTreeJson> treeJson = new ArrayList<>();
        //根据上级分类id获取节点
        List<ProductType> productTypeList = productTypeMapper.getByPId(pId);
        if (productTypeList.isEmpty()) {
            return null;
        }
        for (ProductType productType : productTypeList) {
            ProductTypeTreeJson tempTreeJson = new ProductTypeTreeJson();
            tempTreeJson.setTypeName(productType.getTypeName());
            tempTreeJson.setId(productType.getId());
            //根据该节点的id来查询该节点的子节点
            List<ProductType> sonNodeList = productTypeMapper.getByPId(productType.getId());
            if (!sonNodeList.isEmpty()) {
                List<ProductTypeTreeJson> sonTreeMenuList = getProductTypeTreeJson(productTypeMapper, productType.getId());
                tempTreeJson.setChildren(sonTreeMenuList);
            }
            treeJson.add(tempTreeJson);
        }

        return treeJson;
    }
}
