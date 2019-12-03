package com.qiang.practice.oauth;

import com.qiang.practice.base.Constants;
import com.qiang.practice.base.TipsConstants;
import com.qiang.practice.model.SysUser;
import com.qiang.practice.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: OanthInterceptor
 * @Author: CLQ
 * @Date: 2019/8/8
 * @Description: 登陆验证
 */
@Component
public class OauthInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        if (StringUtils.equals(request.getMethod(), "GET")) {
            if (!StringUtils.containsAny(requestURI,
                    "/loginUser", "/userAddresss",
                    "/userOrders", "/userShoppingCarts", "/sysUsers", "/productInboundRecords",
                    "/publishCounts", "/readCounts", "/sysLogs")) {
                return true; //放行GET请求, 但需要登录权限的GET请求不放行
            }
        }

        String token = request.getHeader(Constants.TOKEN_HEADER_PARAM);

        if (null == token) { //未携带token
            response.sendError(HttpStatus.UNAUTHORIZED.value(), TipsConstants.HAVE_NOT_LOGIN);
            return false;
        }

        SysUser user = redisService.findByToken(token);
        if (null == user) { //token不对
            response.sendError(HttpStatus.UNAUTHORIZED.value(), TipsConstants.HAVE_NOT_LOGIN);
            return false;
        }

        //去往后台的请求
        if (!StringUtils.containsAny(requestURI, "/loginUser",
                "/userAddresss", "/userOrders", "/userShoppingCarts")) {
            if (user.getHaveAuthority() == 0) { //没有权限
                response.sendError(HttpStatus.FORBIDDEN.value(), TipsConstants.NOT_HAVE_AUTHORITY);
                return false;
            }
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
