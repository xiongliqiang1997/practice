package com.qiang.practice.utils.request;

import com.qiang.practice.model.*;
import com.qiang.practice.model.vo.ProductImgVO;
import com.qiang.practice.model.vo.ProductInboundRecordAndDetailVO;
import com.qiang.practice.model.vo.UserOrderAndOrderProductVO;

import java.util.Date;

/**
 * @Author: CLQ
 * @Date: 2019/8/1
 * @Description: TODO
 */
@SuppressWarnings("all")
public class ReqParamUtil {

    /**
     * 将信息同步到企业印象
     *
     * @param plateInfo
     * @return
     */
    public static OrgImpression copyPlateInfoToOrgImpression(PlateInfo plateInfo, Long sysUser) {
        OrgImpression orgImpression = new OrgImpression();
        orgImpression.setTitle(plateInfo.getTitle());
        orgImpression.setSubTitle(plateInfo.getSubTitle());
        orgImpression.setEventDate(plateInfo.getEventDate());
        orgImpression.setIsPublish(plateInfo.getIsPublish());
        orgImpression.setImgPath(plateInfo.getImgPath());
        orgImpression.setContent(plateInfo.getContent());
        orgImpression.setCreateDate(new Date());
        orgImpression.setIsValid((byte) 1);
        orgImpression.setSysUser(sysUser);
        return orgImpression;
    }

    public static boolean isOrgAchievementAbsent(OrgAchievement orgAchievement) {
        return (orgAchievement.getTitle() == null || orgAchievement.getSubTitle() == null || orgAchievement.getEventDate() == null
                || orgAchievement.getImgPath() == null || orgAchievement.getIsPublish() == null);
    }

    /**
     * 将信息同步到企业成果
     *
     * @param plateInfo
     * @param sysUser
     * @return
     */
    public static OrgAchievement copyPlateInfoToOrgAchievement(PlateInfo plateInfo, Long sysUser) {
        OrgAchievement orgAchievement = new OrgAchievement();
        orgAchievement.setTitle(plateInfo.getTitle());
        orgAchievement.setSubTitle(plateInfo.getSubTitle());
        orgAchievement.setEventDate(plateInfo.getEventDate());
        orgAchievement.setIsPublish(plateInfo.getIsPublish());
        orgAchievement.setImgPath(plateInfo.getImgPath());
        orgAchievement.setContent(plateInfo.getContent());
        orgAchievement.setCreateDate(new Date());
        orgAchievement.setIsValid((byte) 1);
        orgAchievement.setSysUser(sysUser);
        return orgAchievement;
    }

    /**
     * 将参数其中的商品信息抽取到商品实体对象中
     *
     * @param productImgVO
     * @return
     */
    public static Product copyProductImgVOToProduct(ProductImgVO productImgVO) {
        Product product = new Product();
        product.setId(productImgVO.getId());
        product.setIsPublish(productImgVO.getIsPublish());
        product.setProductName(productImgVO.getProductName());
        product.setProductPrice(productImgVO.getProductPrice());
        product.setProductStock(productImgVO.getProductStock());
        product.setProductType(productImgVO.getProductType());
        product.setRemark(productImgVO.getRemark());
        product.setSysUser(productImgVO.getSysUser());
        if (productImgVO.getIsPublish() == 1) {
            product.setPublishDate(new Date());
        }
        if (productImgVO.getId() == null) {
            product.setViewNum(0);
        }
        return product;
    }

    /**
     * 将订单信息拷贝到订单对象
     * @param orderAndOrderProductVO
     * @return
     */
    public static UserOrder copyOrderAndOrderProductVOToOrder(UserOrderAndOrderProductVO orderAndOrderProductVO) {
        UserOrder userOrder = new UserOrder();
        userOrder.setAddressId(orderAndOrderProductVO.getAddressId());
        userOrder.setCreateDate(new Date());
        userOrder.setIsValid((byte)1);
        userOrder.setStatus((byte)0);
        return userOrder;
    }

    /**
     * 将入库记录信息拷贝到入库记录类
     * @param productInboundRecordAndDetailVO
     * @return
     */
    public static ProductInboundRecord copyParamToProductInboundRecord(ProductInboundRecordAndDetailVO productInboundRecordAndDetailVO) {
        ProductInboundRecord productInboundRecord = new ProductInboundRecord();
        productInboundRecord.setProductSource(productInboundRecordAndDetailVO.getProductSource());
        productInboundRecord.setRemark(productInboundRecordAndDetailVO.getRemark());
        return productInboundRecord;
    }
}
