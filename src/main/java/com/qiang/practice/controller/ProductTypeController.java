package com.qiang.practice.controller;

import com.qiang.practice.annotation.MyLog;
import com.qiang.practice.annotation.PageCommon;
import com.qiang.practice.model.ProductType;
import com.qiang.practice.service.ProductTypeService;
import com.qiang.practice.utils.response.R;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/8/21
 * @Description: TODO
 */
@SuppressWarnings("all")
@Api(tags = "商品分类")
@RestController
public class ProductTypeController {
    @Autowired
    private ProductTypeService productTypeService;

    /**
     * 获取商品分类树形菜单所需要的json格式数据
     *
     * @return
     */
    @ApiOperation("获取分类树形菜单所需要的json格式数据，若id不为空，则表示排除id项及其子孙项")
    @RequestMapping(value = "/api/productTypes/tree", method = RequestMethod.GET)
    public R getTreeMenu(@RequestParam(value = "id", required = false) Long id) {
        return productTypeService.getTreeMenu(id);
    }

    /**
     * 分页获取商品分类，若查询条件不为null，则为 分页且条件查询
     *
     * @param paramMap
     * @return
     */
    @ApiOperation("分页获取商品分类，若查询条件不为null，则为 分页且条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "分类id", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "typeName", value = "分类名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pName", value = "上级分类名称", required = false, dataType = "String", paramType = "query")
    })
    @PageCommon
    @RequestMapping(value = "/api/productTypes", method = RequestMethod.GET)
    public R getByPage(@ApiParam(hidden = true) @RequestParam Map<String, Object> paramMap) {
        return productTypeService.getByPage(paramMap);
    }

    /**
     * 新建/编辑商品分类
     *
     * @param productType
     * @return
     */
    @ApiOperation("新建/编辑商品分类， id空：新建；  id非空：编辑")
    @MyLog("新建/编辑商品分类")
    @RequestMapping(value = "/api/productTypes", method = RequestMethod.POST)
    public R addOrUpdate(@RequestBody ProductType productType) {
        return productTypeService.addOrUpdate(productType);
    }

    /**
     * 根据id数组删除被选中的商品分类
     *
     * @param idList
     * @return
     */
    @ApiOperation("根据id数组删除被选中的商品分类")
    @MyLog("删除商品分类")
    @RequestMapping(value = "/api/productTypes/{ids}", method = RequestMethod.DELETE)
    public R removeSysOrgsByIdList(@PathVariable(value = "ids") List<Long> idList) {
        return productTypeService.removeByIdList(idList);
    }

    /**
     * 根据id查单个商品分类的详细信息
     *
     * @param id
     * @return
     */
    @ApiOperation("根据id查单个商品分类的详细信息")
    @RequestMapping(value = "/api/productTypes/{id}", method = RequestMethod.GET)
    public R getById(@PathVariable(value = "id") Long id) {
        return productTypeService.getById(id);
    }

    /**
     * 根据id获得需要排序的商品分类
     *
     * @param id
     * @return
     */
    @ApiOperation("根据id获得需要排序的商品分类，若id为空，则表示查一级菜单")
    @RequestMapping(value = "/api/productTypes/sort", method = RequestMethod.GET)
    public R getSortData(@RequestParam(value = "id", required = false) Long id) {
        return productTypeService.getSortData(id);
    }

    /**
     * 保存排序
     *
     * @param param
     * @return
     */
    @ApiOperation("保存排序，参数格式：{ oldSortList: ObjectArray, newSortList: ObjectArray }")
    @MyLog("排序商品分类")
    @RequestMapping(value = "/api/productTypes/sort", method = RequestMethod.POST)
    public R sort(@RequestBody Map<String, Object> param) {
        return productTypeService.sort(param);
    }

}
