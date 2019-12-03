package com.qiang.practice.config;

import com.qiang.practice.websocket.WebSocketInteceptor;
import com.qiang.practice.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

/**
 * WebSocket配置
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    WebSocketServer webSocketServer;

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(1024 * 1024);
        container.setMaxBinaryMessageBufferSize(100 * 1024 * 1024);
        return container;
    }

    @Bean
    public WebSocketInteceptor webSocketInteceptor() {
        return new WebSocketInteceptor();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketServer,"/webSocket").addInterceptors(webSocketInteceptor()).setAllowedOrigins("*");
        registry.addHandler(webSocketServer,"/sockJs").addInterceptors(webSocketInteceptor()).withSockJS();
    }
}
