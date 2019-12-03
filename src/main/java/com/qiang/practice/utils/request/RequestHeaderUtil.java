package com.qiang.practice.utils.request;

import com.qiang.practice.base.Constants;
import com.qiang.practice.utils.HttpContextUtils;

/**
 * @Author: CLQ
 * @Date: 2019/8/16
 * @Description: TODO
 */
public class RequestHeaderUtil {

    /**
     * 尝试获取当前请求的HttpServletResponse实例
     *
     * @return HttpServletResponse
     */
    public static String getToken() {
        return HttpContextUtils.getHttpServletRequest().getHeader(Constants.TOKEN_HEADER_PARAM);
    }

    /**
     * 尝试获取当前请求的HttpServletResponse实例
     *
     * @return HttpServletResponse
     */
    public static String getType() {
        return HttpContextUtils.getHttpServletRequest().getHeader(Constants.TYPE_HEADER_PARAM);
    }
}
