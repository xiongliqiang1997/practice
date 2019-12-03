package com.qiang.practice.controller;

import com.qiang.practice.annotation.MyLog;
import com.qiang.practice.model.vo.ProductImgVO;
import com.qiang.practice.model.vo.ProductInboundRecordAndDetailVO;
import com.qiang.practice.service.ProductService;
import com.qiang.practice.utils.response.R;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/8/21
 * @Description: 商品
 */
@SuppressWarnings("all")
@Api(tags = "商品")
@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    /**
     * 分页获取商品，若查询条件不为null，则为分页且条件查询
     *
     * @param paramMap
     * @return
     */
    @ApiOperation("分页获取商品，若查询条件不为null，则为分页且条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页展示数", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "typeName", value = "所属分类名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "productName", value = "商品名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "productStock", value = "库存升降序 升：asc 降：desc", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "isPublish", value = "是否已发布", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "productType", value = "所属分类id", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "minProductPrice", value = "最小价格", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "maxProductPrice", value = "最大价格", required = false, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "productPrice", value = "价格升降序 升：asc 降：desc", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "viewNum", value = "浏览数量升降序 升：asc 降：desc", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/api/products", method = RequestMethod.GET)
    public R getByPage(@ApiParam(hidden = true) @RequestParam Map<String, Object> paramMap) {
        return productService.getByPage(paramMap);
    }

    /**
     * 新建/编辑商品
     *
     * @param productImgVO
     * @return
     */
    @ApiOperation("新建/编辑商品。注：id空：新建  id非空：编辑；参数格式与分页查询时的返回值格式一致")
    @MyLog("新建/编辑商品")
    @RequestMapping(value = "/api/products", method = RequestMethod.POST)
    public R addOrUpdate(@RequestBody ProductImgVO productImgVO) {
        return productService.addOrUpdate(productImgVO);
    }

    /**
     * 根据id查单个商品的详细信息（内含图片列表）
     *
     * @param id
     * @return
     */
    @ApiOperation("根据id查单个商品的详细信息（内含图片列表）")
    @RequestMapping(value = "/api/products/{id}", method = RequestMethod.GET)
    public R getById(@PathVariable(value = "id") Long id) {
        return productService.getById(id);
    }

    /**
     * 根据id数组删除选中的商品
     *
     * @param idList
     * @return
     */
    @ApiOperation("根据id数组删除选中的商品")
    @MyLog("删除商品")
    @RequestMapping(value = "/api/products/{ids}", method = RequestMethod.DELETE)
    public R removeByIdList(@PathVariable(value = "ids") List<Long> idList) {
        return productService.removeByIdList(idList);
    }

    /**
     * 根据id数组发布选中的商品
     *
     * @param idList
     * @return
     */
    @ApiOperation("根据id数组发布选中的商品")
    @MyLog("发布商品")
    @RequestMapping(value = "/api/products/publish/{ids}", method = RequestMethod.GET)
    public R publishByIdList(@PathVariable(value = "ids") List<Long> idList) {
        return productService.publishByIdList(idList);
    }

    /**
     * 根据id数组撤销选中的商品
     *
     * @param idList
     * @return
     */
    @ApiOperation("根据id数组撤销选中的商品")
    @MyLog("撤销商品")
    @RequestMapping(value = "/api/products/publish/{ids}", method = RequestMethod.DELETE)
    public R revokeByIdList(@PathVariable(value = "ids") List<Long> idList) {
        return productService.revokeByIdList(idList);
    }

    /**
     * 根据id数组获取商品
     * @param idListStr
     * @return
     */
    @ApiOperation("根据id数组获取商品")
    @RequestMapping(value = "/api/products/details/{ids}", method = RequestMethod.GET)
    public R getByIdList(@PathVariable(value = "ids") String idListStr) {
        return productService.getByIdListStr(idListStr);
    }

    /**
     * 入库
     * @param productInboundRecordAndDetailVO
     * @return
     */
    @ApiOperation("入库 ===> coding中")
    @RequestMapping(value = "/api/products/inbound", method = RequestMethod.POST)
    public R inbound(@RequestBody ProductInboundRecordAndDetailVO productInboundRecordAndDetailVO) {
        return productService.inbound(productInboundRecordAndDetailVO);
    }

}
