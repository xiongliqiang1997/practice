package com.qiang.practice.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiang.practice.base.Constants;
import com.qiang.practice.base.TipsConstants;
import com.qiang.practice.config.ServerConfig;
import com.qiang.practice.mapper.ProductImgMapper;
import com.qiang.practice.mapper.ProductInboundRecordDetailMapper;
import com.qiang.practice.mapper.ProductInboundRecordMapper;
import com.qiang.practice.mapper.ProductMapper;
import com.qiang.practice.model.Product;
import com.qiang.practice.model.ProductImg;
import com.qiang.practice.model.ProductInboundRecord;
import com.qiang.practice.model.ProductInboundRecordDetail;
import com.qiang.practice.model.vo.ProductImgVO;
import com.qiang.practice.model.vo.ProductInboundRecordAndDetailVO;
import com.qiang.practice.utils.HttpContextUtils;
import com.qiang.practice.utils.request.ReqParamUtil;
import com.qiang.practice.utils.request.RequestHeaderUtil;
import com.qiang.practice.utils.response.R;
import com.qiang.practice.utils.response.RUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/8/21
 * @Description: TODO
 */
@SuppressWarnings("all")
@Transactional
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductImgMapper productImgMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ProductInboundRecordMapper productInboundRecordMapper;
    @Autowired
    private ProductInboundRecordDetailMapper productInboundRecordDetailMapper;
    @Autowired
    private ServerConfig serverConfig;

    @Override
    public R getByPage(Map<String, Object> paramMap) {
        //判断请求是否来自官网
        if (StringUtils.equals(Constants.TYPE_WEB_PARAM, RequestHeaderUtil.getType())) {
            paramMap.put("type", RequestHeaderUtil.getType());
        }
        if (paramMap.get("pageSize") != null) {
            //使用分页插件
            PageHelper.startPage(Integer.valueOf(paramMap.get("page").toString()), Integer.valueOf(paramMap.get("pageSize").toString()));
            //根据条件查询商品
            List<ProductImgVO> productImgVOList = productMapper.getListByCondition(paramMap);
            //从redis为每件商品设置库存,并为商品的每张图片url拼上访问地址, concat方法在数据量大时效率更高些
            for (ProductImgVO productImgVO : productImgVOList) {
                productImgVO.setProductStock(getProductStock(productImgVO.getId()));
                if (productImgVO.getFirstProductImgPath() != null) {
                    productImgVO.setFirstProductImgPath(serverConfig.getImageUrl(productImgVO.getFirstProductImgPath()));
                }
            }
            PageInfo<ProductImgVO> pageInfo = new PageInfo<>(productImgVOList);
            return R.ok(pageInfo);
        } else {
            List<ProductImgVO> productImgVOList = productMapper.getListByCondition(paramMap);
            //从redis为每件商品设置库存,并为商品的每张图片url拼上访问地址, concat方法在数据量大时效率更高些
            for (ProductImgVO productImgVO : productImgVOList) {
                productImgVO.setProductStock(getProductStock(productImgVO.getId()));
                if (productImgVO.getFirstProductImgPath() != null) {
                    productImgVO.setFirstProductImgPath(serverConfig.getImageUrl(productImgVO.getFirstProductImgPath()));
                }
            }
            return R.ok(productImgVOList);
        }
    }

    @Override
    public R addOrUpdate(ProductImgVO productImgVO) {
        int rows = 0;
        //将参数其中的商品信息抽取到商品实体对象中
        Product product = ReqParamUtil.copyProductImgVOToProduct(productImgVO);
        //如果id为null，表示为新增，否则为修改
        if (null == product.getId()) {
            HttpContextUtils.getHttpServletRequest().setAttribute("logType", "新建");
            product.setCreateDate(new Date());
            //设置有效性
            product.setIsValid((byte) 1);
            //设置创建人id
            product.setSysUser(redisService.findByToken(
                    HttpContextUtils.getHttpServletRequest().getHeader(Constants.TOKEN_HEADER_PARAM)).getId());
            //新增
            rows = productMapper.insert(product);
        } else {
            HttpContextUtils.getHttpServletRequest().setAttribute("logType", "编辑");
            //修改操作，设置修改时间
            product.setUpdateDate(new Date());
            //修改
            rows = productMapper.updateByPrimaryKeySelective(product);
        }
        //若是添加商品添加或者修改成功，则开始添加图片；
        if (rows == 1) {
            if (productImgVO.getProductImgList().size() > 20) {
                return R.error(TipsConstants.OVER_PRODUCT_IMG_MAX_NUM);
            } else if (productImgVO.getProductImgList().size() == 0) {
                //没有为商品上传图片，给一个提示
                return R.ok(TipsConstants.PRODUCT_SUCCESS_BUT_IMG_FALIURE);
            } else {
                if (null != productImgVO.getId()) {
                    //若是修改商品，先将图片库中商品原本的图片删除
                    productImgMapper.updateIsValidToZeroByProductId(product.getId());
                }
                //将参数中的图片批量添加
                productImgMapper.batchInsert(productImgVO.getProductImgList(), product.getId());
                return R.okDefault();
            }

        } else {
            return R.error(HttpContextUtils.getHttpServletRequest().getAttribute("logType") + "失败，请稍后重试");
        }

    }

    @Override
    public R getById(Long id) {
        //如果是官网的请求，则浏览数量+1
        if (StringUtils.equals(Constants.TYPE_WEB_PARAM, HttpContextUtils.getHttpServletRequest().getHeader(Constants.TYPE_HEADER_PARAM))) {
            productMapper.addViewNum(id);
        }
        //根据id查单个商品的详细信息（内含图片列表）
        ProductImgVO productImgVO = productMapper.getById(id);
        //从redis读取商品库存
        productImgVO.setProductStock(getProductStock(productImgVO.getId()));
        for (ProductImg productImg : productImgVO.getProductImgList()) {
            if (productImg.getImgPath() != null) {
                //图片url拼上前缀
                productImg.setImgPath(serverConfig.getImageUrl(productImg.getImgPath()));
            }
        }
        return R.ok(productImgVO);
    }

    @Override
    public R removeByIdList(List<Long> idList) {
        //判断参数是否为空
        if (idList.isEmpty()) {
            return R.error(TipsConstants.PARAM_NOT_ENOUGH);
        }
        //批量删除商品，将有效字段置为0
        if (productMapper.updateIsValidToZeroByIdList(idList) == idList.size()) {
            //商品删除成功，再删除图片
            productImgMapper.updateIsValidToZeroByProductIdList(idList);
        }
        return R.okDefault();
    }

    @Override
    public R publishByIdList(List<Long> idList) {
        //判断参数是否为空
        if (idList.isEmpty()) {
            return R.error(TipsConstants.PARAM_NOT_ENOUGH);
        }
        //批量发布商品,将发布字段置为1
        return RUtil.isBatchOk(productMapper.updateIsPublishByIdList(idList, (byte) 1), idList.size());
    }

    @Override
    public R revokeByIdList(List<Long> idList) {
        //判断参数是否为空
        if (idList.isEmpty()) {
            return R.error(TipsConstants.PARAM_NOT_ENOUGH);
        }
        //批量撤销商品,将发布字段置为0
        return RUtil.isBatchOk(productMapper.updateIsPublishByIdList(idList, (byte) 0), idList.size());
    }

    @Override
    public R getByIdListStr(String idListStr) {
        //根据id数组获取商品（内含商品第一张图片）
        List<ProductImgVO> productImgVOList = productMapper.getByIdList(idListStr);
        //为每件商品设置库存(库存在redis中存着)
        for (ProductImgVO productImgVO : productImgVOList) {
            productImgVO.setProductStock(getProductStock(productImgVO.getId()));
            if (productImgVO.getFirstProductImgPath() != null) {
                productImgVO.setFirstProductImgPath(serverConfig.getImageUrl(productImgVO.getFirstProductImgPath()));
            }
        }
        return R.ok(productImgVOList);
    }

    @Override
    public R inbound(ProductInboundRecordAndDetailVO productInboundRecordAndDetailVO) {
        //如果入库的商品列表为空
        if (productInboundRecordAndDetailVO.getProductInboundRecordDetailList().isEmpty()) {
            return R.error(TipsConstants.HAS_NOT_INBOUND_ANY_PRODUCT);
        }
        Map<String, Object> productStockMap = new HashMap<>();
        for (ProductInboundRecordDetail productInboundRecordDetail :
                productInboundRecordAndDetailVO.getProductInboundRecordDetailList()) {
            //如果参数不合法
            if (productInboundRecordDetail.getNum() == null || productInboundRecordDetail.getProductId() == null) {
                return R.error(TipsConstants.PARAM_NOT_ENOUGH);
            }
            //先将商品之前的库存数量取出来
            Object redisProductStock = redisService.hget(Constants.PRODUCT_STOCK, productInboundRecordDetail.getProductId().toString());
            Integer num = redisProductStock == null ? 0 : Integer.valueOf(redisProductStock.toString());
            //将新的商品库存存在map中, 循环结束后将map更新进redis
            productStockMap.put(productInboundRecordDetail.getProductId().toString(), String.valueOf(productInboundRecordDetail.getNum() + num));
        }
        //生成本次入库记录
        ProductInboundRecord productInboundRecord = ReqParamUtil.copyParamToProductInboundRecord(productInboundRecordAndDetailVO);
        productInboundRecord.setSysUser(redisService.findByToken(HttpContextUtils.getHttpServletRequest().getHeader(Constants.TOKEN_HEADER_PARAM)).getId());
        //保存本次入库记录
        if (1 == productInboundRecordMapper.insert(productInboundRecord)) {
            //批量生成各个的商品入库数量
            int rows = productInboundRecordDetailMapper.batchInsert(productInboundRecordAndDetailVO.getProductInboundRecordDetailList(),
                    productInboundRecord.getId());
            if (rows == productInboundRecordAndDetailVO.getProductInboundRecordDetailList().size()) {
                redisService.hset(Constants.PRODUCT_STOCK, productStockMap);
                return R.ok(TipsConstants.INBOUND_SUCCESS);
            } else {
                return R.error(TipsConstants.INBOUND_FAILURE);
            }
        } else {
            return R.error(TipsConstants.INBOUND_FAILURE);
        }

    }

    @Override
    public Integer getProductStock(Long productId) {
        Object redisProductStock = redisService.hget(Constants.PRODUCT_STOCK, productId.toString());
        return redisProductStock == null ? 0 : Integer.valueOf(redisProductStock.toString());
    }

}
