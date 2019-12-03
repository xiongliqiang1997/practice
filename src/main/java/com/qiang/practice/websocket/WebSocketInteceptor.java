package com.qiang.practice.websocket;

import com.qiang.practice.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: CLQ
 * @Date: 2019/9/2
 * @Description: WebSocket拦截器
 */
public class WebSocketInteceptor implements HandshakeInterceptor {
    @Autowired
    private RedisService redisService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        ServletServerHttpRequest req = (ServletServerHttpRequest) request;
        //先拿token
        String token = req.getServletRequest().getParameter("token");
        if (token == null) {
            //token为空则判断是否是游客
            if (req.getServletRequest().getParameter("tourist") == null) {
                //是游客但是还没有生成唯一标识
                attributes.put("needCreateUUID", true);
            } else { //是游客并带来了唯一标识
                attributes.put("touristId", req.getServletRequest().getParameter("tourist"));
                attributes.put("anotherUserId", req.getServletRequest().getParameter("anotherUserId"));
            }
        } else {
            //然后根据token获取用户信息
            Principal user = parseToken(token);
            if(Objects.isNull(user)){  //如果token认证失败user为null，返回false拒绝握手
                System.out.println("ip地址:" + req.getRemoteAddress() + " -> token认证失败");
                return false;
            }
            //保存用户的唯一标识
            attributes.put("sysUserId", user.getName());
            attributes.put("anotherUserId", req.getServletRequest().getParameter("anotherUserId"));
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

    /**
     * 根据token认证授权
     * @param token
     */
    private Principal parseToken(String token){
        //根据token获取认证用户信息
        return redisService.findByToken(token);
    }
}
