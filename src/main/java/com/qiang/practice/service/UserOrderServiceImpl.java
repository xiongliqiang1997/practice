package com.qiang.practice.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiang.practice.base.Constants;
import com.qiang.practice.base.TipsConstants;
import com.qiang.practice.config.ServerConfig;
import com.qiang.practice.mapper.*;
import com.qiang.practice.model.UserOrder;
import com.qiang.practice.model.UserOrderLogistics;
import com.qiang.practice.model.UserOrderProduct;
import com.qiang.practice.model.vo.ProductImgVO;
import com.qiang.practice.model.vo.UserAddressAreaVO;
import com.qiang.practice.model.vo.UserOrderAndOrderProductVO;
import com.qiang.practice.model.vo.UserOrderProductLogisticsVO;
import com.qiang.practice.utils.*;
import com.qiang.practice.utils.request.ReqParamUtil;
import com.qiang.practice.utils.response.R;
import com.qiang.practice.utils.response.RUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/8/23
 * @Description: TODO
 */
@SuppressWarnings("all")
@Transactional
@Service
public class UserOrderServiceImpl implements UserOrderService {
    @Autowired
    private UserOrderMapper userOrderMapper;
    @Autowired
    private UserOrderProductMapper userOrderProductMapper;
    @Autowired
    private UserOrderLogisticsMapper userOrderLogisticsMapper;
    @Autowired
    private UserAddressMapper userAddressMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private UserShoppingCartMapper userShoppingCartMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ServerConfig serverConfig;

    @Override
    public R getByPage(Map<String, Object> paramMap) {
        //参数非空判断
        if (RUtil.isPageParamAbsent(paramMap)) {
            return R.error("请携带接口规定参数");
        } else {
            //将当前用户id查出来
            paramMap.put("sysUser", redisService.findByToken(
                    HttpContextUtils.getHttpServletRequest().getHeader(Constants.TOKEN_HEADER_PARAM))
                    .getId());
            paramMap.putIfAbsent("page", 1);
            //使用分页插件
            PageHelper.startPage(Integer.valueOf(paramMap.get("page").toString()), Integer.valueOf(paramMap.get("pageSize").toString()));
            List<UserOrderAndOrderProductVO> userOrderAndOrderProductVOList = userOrderMapper.getListByPageAndCondition(paramMap);
            for (UserOrderAndOrderProductVO userOrderAndOrderProductVO : userOrderAndOrderProductVOList) {
                for (UserOrderProductLogisticsVO userOrderProductLogisticsVO : userOrderAndOrderProductVO.getUserOrderProductLogisticsVOList()) {
                    userOrderProductLogisticsVO.setFirstProductImgPath(
                            serverConfig.getImageUrl(userOrderProductLogisticsVO.getFirstProductImgPath()));
                }
            }
            PageInfo<UserOrderAndOrderProductVO> pageInfo = new PageInfo<>(userOrderAndOrderProductVOList);
            return R.ok(pageInfo);

        }
    }

    @Override
    public R removeByIdListStr(String idListStr) {
        //根据id字符串将订单的有效字段置为0
        return RUtil.isBatchOk(userOrderMapper.updateIsValid(idListStr, (byte) 0), idListStr.split(",").length);
    }

    @Override
    public R add(UserOrderAndOrderProductVO orderAndOrderProductVO) {
        //首先判断要添加到订单中的各个商品有哪些已经不存在
        List<String> notExistProductNameList = productMapper.checkIsValidByIdList(orderAndOrderProductVO.getUserOrderProductList());
        if (!notExistProductNameList.isEmpty()) {
            //证明有商品不存在了
            return R.error(TipsConstants.PRODUCT_HAS_DOWN_WHEN_CREATE_ORDER, notExistProductNameList);
        }
        //然后判断要添加到订单中的各个商品库存是否足够
        for (UserOrderProduct userOrderProduct : orderAndOrderProductVO.getUserOrderProductList()) {
            //去redis中取库存
            Object redisProductStock = redisService.hget(Constants.PRODUCT_STOCK, userOrderProduct.getProductId().toString());
            if (redisProductStock == null) {
                return R.error(TipsConstants.PRODUCT_STOCK_ZONE);
            } else {
                Integer productStock = Integer.valueOf(redisProductStock.toString());
                if (productStock - userOrderProduct.getNum() < 1) {
                    return R.error(TipsConstants.PRODUCT_STOCK_NOT_ENOUGH);
                }
            }
        }
        //将接收到的信息拷贝到订单实体对象中
        UserOrder userOrder = ReqParamUtil.copyOrderAndOrderProductVOToOrder(orderAndOrderProductVO);
        //获取所属用户id
        Long sysUserId = redisService.findByToken(
                HttpContextUtils.getHttpServletRequest().getHeader(Constants.TOKEN_HEADER_PARAM))
                .getId();
        //设置所属用户id
        userOrder.setSysUser(sysUserId);
        //设置订单编号
        userOrder.setCode(UUIDUtil.getUUID());
        //获取收货地址信息
        UserAddressAreaVO userAddressAreaVO = userAddressMapper.getById(userOrder.getAddressId(), sysUserId);
        if (userAddressAreaVO == null) {
            return R.error(TipsConstants.CANNOT_USE_OTHER_PERSON_ADDRESS);
        }
        userOrder.setConsigneeName(userAddressAreaVO.getConsigneeName());
        userOrder.setConsigneePhone(userAddressAreaVO.getConsigneePhone());
        userOrder.setConsigneeAddress(
                userAddressAreaVO.getProvinceObj().getAreaName()
                        + userAddressAreaVO.getCityObj().getAreaName()
                        + userAddressAreaVO.getAreaObj().getAreaName()
                        + userAddressAreaVO.getStreet()
        );
        //生成订单
        if (userOrderMapper.insert(userOrder) == 1) {
            StringBuffer productIdListStrBuffer = new StringBuffer();
            for (UserOrderProduct userOrderProduct : orderAndOrderProductVO.getUserOrderProductList()) {
                productIdListStrBuffer.append(userOrderProduct.getProductId() + ",");
            }
            String productIdListStr = productIdListStrBuffer.toString();
            productIdListStr = productIdListStr.substring(0, productIdListStr.length() - 1);
            //获取订单中每个商品的信息
            List<ProductImgVO> productImgVOList = productMapper.getJustProductInfoAndFirstImgByIdListStr(productIdListStr.toString());
            BigDecimal userOrderAmountTotal = new BigDecimal(0);
            //循环为订单中每个商品保存一个历史信息(订单id、商品名称、商品价格、第一张商品图片)
            for (ProductImgVO productImgVO : productImgVOList) {
                for (UserOrderProduct userOrderProduct : orderAndOrderProductVO.getUserOrderProductList()) {
                    if (userOrderProduct.getProductId() == productImgVO.getId()) {
                        userOrderProduct.setUserOrderId(userOrder.getId());
                        userOrderProduct.setFirstProductImgPath(productImgVO.getFirstProductImgPath());
                        userOrderProduct.setProductName(productImgVO.getProductName());
                        userOrderProduct.setProductPrice(productImgVO.getProductPrice());
                        userOrderAmountTotal = userOrderAmountTotal.add(MathUtil.multiply(productImgVO.getProductPrice(), userOrderProduct.getNum()));
                        break;
                    } else {
                        continue;
                    }
                }
            }
            //为订单设置总金额
            if (userOrderMapper.updateAmountTotal(userOrder.getId(), userOrder.getCode(), userOrderAmountTotal) != 1) {
                return R.error(TipsConstants.CREATE_USER_ORDER_FAIL);
            }
            System.out.println(userOrderAmountTotal);
            //成功，则为订单新增商品
            int rows = userOrderProductMapper.batchInsert(
                    orderAndOrderProductVO.getUserOrderProductList(), userOrder.getId());
            if (rows == orderAndOrderProductVO.getUserOrderProductList().size()) {
                //为用户清理购物车，已经加入订单的商品从购物车移除
                userShoppingCartMapper.removeByProductIdList(productIdListStr.toString(), sysUserId);
                Map<String, Object> idAndCodeMap = new HashMap<>();
                idAndCodeMap.put("id", userOrder.getId());
                idAndCodeMap.put("code", userOrder.getCode());
                return R.ok(TipsConstants.CREATE_USER_ORDER_SUCCESS, idAndCodeMap);
            } else {
                return R.error(TipsConstants.CREATE_USER_ORDER_FAIL);
            }
        } else {
            //生成订单失败
            return R.error(TipsConstants.CREATE_USER_ORDER_FAIL);
        }
    }

    @Override
    public R getById(Long id) {
        //获取订单详情
        UserOrderAndOrderProductVO userOrderAndOrderProductVO = userOrderMapper.getById(id);
        for (UserOrderProductLogisticsVO userOrderProductLogisticsVO : userOrderAndOrderProductVO.getUserOrderProductLogisticsVOList()) {
            //图片url拼上http前缀
            userOrderProductLogisticsVO.setFirstProductImgPath(
                    serverConfig.getImageUrl(userOrderProductLogisticsVO.getFirstProductImgPath()));
        }
        return R.ok(userOrderMapper.getById(id));
    }

    @Override
    public R pay(Long id, String code) {
        //获取订单创建人id
        Long sysUserId = redisService.findByToken(
                HttpContextUtils.getHttpServletRequest().getHeader(Constants.TOKEN_HEADER_PARAM))
                .getId();
        //先拿到订单信息
        UserOrder userOrder = userOrderMapper.selectByPrimaryKey(id);
        //判断订单状态
        if (userOrder.getStatus() == Constants.USER_ORDER_HAS_CANCELLED_STATUS) {
            //订单已取消，无法支付
            return R.error(TipsConstants.USER_ORDER_HAS_CANCELLED);
        } else if (userOrder.getStatus() != Constants.USER_ORDER_WAIT_PAY_STATUS) {
            //订单支付过了，无法再次支付
            return R.error(TipsConstants.USER_ORDER_HAS_PAYED);
        } else {
            //首先拿到订单中的商品
            List<UserOrderProduct> userOrderProductList = userOrderProductMapper.getByUserOrderId(id);
            //为订单中的每件商品减库存，redis
            Map<String, Object> productStockMap = new HashMap<>();
            for (UserOrderProduct userOrderProduct : userOrderProductList) {
                //先拿到原先的库存
                Object redisProductStock = redisService.hget(Constants.PRODUCT_STOCK, userOrderProduct.getProductId().toString());
                if (redisProductStock == null) {
                    //库存为0，无法支付
                    return R.error(TipsConstants.PRODUCT_STOCK_ZONE);
                } else {
                    Integer productStock = Integer.valueOf(redisProductStock.toString());
                    if (productStock - userOrderProduct.getNum() < 1) {
                        //库存不足
                        return R.error(TipsConstants.PRODUCT_STOCK_NOT_ENOUGH);
                    } else {
                        //商品库存充足
                        productStockMap.put(userOrderProduct.getProductId().toString(),
                                String.valueOf(productStock - userOrderProduct.getNum()));
                    }
                }
            }
            //减库存
            redisService.hset(Constants.PRODUCT_STOCK, productStockMap);
            //修改订单状态
            int rows = userOrderMapper.pay(id, sysUserId, code,
                    MD5Util.getMD5("pay" + sysUserId + System.currentTimeMillis()),
                    Constants.USER_ORDER_HAS_PAYED_STATUS, Constants.USER_ORDER_WAIT_PAY_STATUS);
            //返回结果
            return RUtil.isSingleOk(rows, TipsConstants.PAY_SUCCESS, TipsConstants.PAY_FAILURE, null);
        }
    }

    @Override
    public void changeOrderStatus() {
        //首先拿到所有的订单
        List<UserOrder> userOrderList = userOrderMapper.getAll();
        for (UserOrder userOrder : userOrderList) {
            if (userOrder.getStatus() == Constants.USER_ORDER_HAS_PAYED_STATUS
                    && userOrder.getPayNo() != null) {
                if (userOrder.getPayTime() != null) {
                    //已付款的订单，付款1分钟后发货
                    if (DateUtil.getHourDiff(userOrder.getPayTime()) > 0 || DateUtil.getDayDiff(userOrder.getPayTime()) > 0 || DateUtil.getMiniteDiff(userOrder.getPayTime()) > Constants.SEND_PRODUCT_AFTER_PAY) {
                        //首先为订单中的每件商品生成物流信息
                        List<UserOrderProduct> userOrderProductList = userOrderProductMapper.getByUserOrderId(userOrder.getId());
                        List<UserOrderLogistics> userOrderLogisticsList = new ArrayList<>();
                        for (UserOrderProduct userOrderProduct : userOrderProductList) {
                            UserOrderLogistics userOrderLogistics = new UserOrderLogistics();
                            userOrderLogistics.setCode(UUIDUtil.getUUID());
                            userOrderLogistics.setUserOrderProductId(userOrderProduct.getId());
                            userOrderLogistics.setLogisticsType("顺丰快递");
                            userOrderLogistics.setFee(new BigDecimal(15.00));
                            userOrderLogistics.setRealPay(new BigDecimal(5.00));
                            userOrderLogistics.setStatus(Constants.LOGISTICS_HAS_OUT_OF_STOCK_STATUS);
                            userOrderLogisticsList.add(userOrderLogistics);
                        }
                        //批量生成物流信息
                        userOrderLogisticsMapper.batchInsert(userOrderLogisticsList);
                        //然后将订单状态置为已经发货
                        userOrderMapper.sendProduct(userOrder.getId(), Constants.USER_ORDER_HAS_SEND_STATUS);
                    }
                }
            } else if (userOrder.getStatus() == Constants.USER_ORDER_HAS_SEND_STATUS) {
                if (userOrder.getPayTime() != null) {
                    //已发货的订单，发货2分钟后订单完成
                    if (DateUtil.getHourDiff(userOrder.getPayTime()) > 0 || DateUtil.getDayDiff(userOrder.getPayTime()) > 0 || DateUtil.getMiniteDiff(userOrder.getPayTime()) > Constants.FINISH_ORDER_AFTER_SEND) {
                        //为订单中的每件商品更新物流信息
                        List<UserOrderProduct> userOrderProductList = userOrderProductMapper.getByUserOrderId(userOrder.getId());
                        StringBuffer userOrderProductIdListStrBuffer = new StringBuffer();
                        for (UserOrderProduct userOrderProduct : userOrderProductList) {
                            userOrderProductIdListStrBuffer.append(userOrderProduct.getId() + ",");
                        }
                        String userOrderProductIdListStr = userOrderProductIdListStrBuffer.toString();
                        userOrderProductIdListStr = userOrderProductIdListStr.substring(0, userOrderProductIdListStr.length() - 1);
                        //将订单中每个商品的物流状态置为已签收
                        userOrderLogisticsMapper.updateStatusByUserOrderProductIdListStr(userOrderProductIdListStr, Constants.LOGISTICS_HAS_SIGNED_STATUS);
                        //然后将订单状态置为已经完成
                        userOrderMapper.sendProduct(userOrder.getId(), Constants.USER_ORDER_HAS_FINISHED_STATUS);
                    }
                }
            } else if (userOrder.getStatus() == Constants.USER_ORDER_WAIT_PAY_STATUS) {
                //待付款的订单，3分钟后订单取消
                if (DateUtil.getHourDiff(userOrder.getCreateDate()) > 0 || DateUtil.getDayDiff(userOrder.getCreateDate()) > 0 || DateUtil.getMiniteDiff(userOrder.getCreateDate()) > Constants.CANCELLED_ORDER_AFTER_CREATE) {
                    //然后将订单状态置为已取消
                    userOrderMapper.sendProduct(userOrder.getId(), Constants.USER_ORDER_HAS_CANCELLED_STATUS);
                }

            }
        }
    }
}
