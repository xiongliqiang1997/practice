package com.qiang.practice.utils.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: CLQ
 * @Date: 2019/7/5
 * @Description: 接口返回值
 */
@Data
@AllArgsConstructor
public class R {
    private boolean res;
    private String msg;
    private Object data;

    public static R ok() {
        return new R(true, null, null);
    }
    public static R okDefault() {
        return new R(true, "操作成功！", null);
    }
    public static R ok(String msg) {
        return new R(true, msg, null);
    }
    public static R ok(String msg, Object obj) {
        return new R(true, msg, obj);
    }
    public static R ok(Object obj) {
        return new R(true, null, obj);
    }
    public static R error(String msg) {
        return new R(false, msg, null);
    }
    public static R error(String msg, Object data) {
        return new R(false, msg, data);
    }
    public static R errorCatch() {
        return new R(false, "系统出现故障，请稍后重试...", null);
    }
    public static R errorCatch(Object data) {
        return new R(false, "系统出现故障，请稍后重试...", data);
    }
    public static R errorCreate() {
        return new R(false, "创建失败，请稍后重试...", null);
    }
    public static R errorEdit() {
        return new R(false, "修改失败，请稍后重试...", null);
    }
    public static R errorRemove() {
        return new R(false, "删除失败，请稍后重试...", null);
    }

    public static R errorDefault() {return new R(false, "操作失败，请稍后重试...", null); }

}
