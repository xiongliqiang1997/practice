package com.qiang.practice.config;

import com.qiang.practice.oauth.OauthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: CLQ
 * @Date: 2019/7/5
 * @Description: 拦截器配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final OauthInterceptor oauthInterceptor;

    public WebConfig(OauthInterceptor oauthInterceptor) {
        this.oauthInterceptor = oauthInterceptor;
    }

    /**
     * 允许Cors跨域
     * @return
     */
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // 1
        corsConfiguration.addAllowedHeader("*"); // 2
        corsConfiguration.addAllowedMethod("*"); // 3
        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig()); // 4
        return new CorsFilter(source);
    }

    /**
     * 设置登录拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        // 登陆拦截器，添加拦截路径和排除拦截路径
        registry.addInterceptor(oauthInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/META-INF/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**", "/swagger-resources/**",
                        "/api/login",
                        "/webSocket/**"
                );
    }

}
