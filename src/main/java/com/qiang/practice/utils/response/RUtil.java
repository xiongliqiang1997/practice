package com.qiang.practice.utils.response;

import java.util.List;
import java.util.Map;

/**
 * @Author: CLQ
 * @Date: 2019/7/10
 * @Description: TODO
 */
public class RUtil {
    /**
     * 操作是否成功（通用）
     * @param flag
     * @return
     */
    public static R isOptionOk(int flag) {
        if (1 == flag) {
            return R.okDefault();
        } else {
            return R.errorDefault();
        }
    }

    /**
     * 单个操作是否成功（通用）
     * @param flag
     * @return
     */
    public static R isSingleOk(int flag, String okMsg, String errorMsg, Object data) {
        if (1 == flag) {
            return R.ok(okMsg, data);
        } else {
            return R.error(errorMsg);
        }
    }

    /**
     * 批量操作是否成功
     * @param rows 实际删除的个数
     * @param size 要删除的个数
     * @return
     */
    public static R isBatchOperationOk(int rows, int size, String okMsg, String errorMsg, Object data) {
        if (size == rows) {
            return R.ok(okMsg, data);
        } else {
            return R.error(errorMsg);
        }
    }

    /**
     * 新建操作是否成功
     * @param rows 实际新建的个数
     * @return
     */
    public static R isAddOk(int rows) {
        if (1 == rows) {
            return R.okDefault();
        } else {
            return R.errorCreate();
        }
    }

    /**
     * 批量操作是否成功
     * @param rows 实际删除的个数
     * @param size 要删除的个数
     * @return
     */
    public static R isBatchOk(int rows, int size) {
        if (size == rows) {
            return R.okDefault();
        } else {
            return R.errorDefault();
        }
    }

    public static boolean isPageParamAbsent(Map<String, Object> paramMap) {
        if (null == paramMap) {
            return true;
        } else if (paramMap.isEmpty()) {
            return true;
        } else {
            return null == paramMap.get("pageSize");
        }
    }

    public static Validator isNameExist(int result) {
        if (result > 0) {
            return validateFailure("该名称已存在");
        } else {
            return validateOK();
        }
    }

    public static ImageResult uploadImageOk(String[] imagePathArray, List<ImageInfo> resImageInfoList, String fullUrl) {
        return new ImageResult(0,true, imagePathArray, "上传成功", resImageInfoList, fullUrl);
    }

    public static ImageResult uploadImageError() {
        return new ImageResult(1, false, null, "上传失败", null, null);
    }

    public static ImageResult uploadImageEmpty() {
        return new ImageResult(1, true, null, "您没有选择任何图片哦", null, null);
    }

    public static Validator validateOK() {
        return new Validator(true, "可以使用", null, true);
    }

    public static Validator validateFailure(String msg) {
        return new Validator(false, msg, null, false);
    }
}
