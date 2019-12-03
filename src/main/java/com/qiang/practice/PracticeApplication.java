package com.qiang.practice;

import org.apache.catalina.connector.Connector;
import org.apache.tomcat.websocket.server.WsSci;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.File;

@EnableTransactionManagement
@MapperScan(value = "com.qiang.practice.mapper")
@EnableRedisHttpSession
@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
public class PracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PracticeApplication.class, args);
    }

    /**
     * 识别请求url中的特殊字符
     *
     * @return
     */
    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        //设置80端口重定向到https所在端口(部署时解开注释)
        //tomcat.addAdditionalTomcatConnectors(createHTTPConnector());
        tomcat.addConnectorCustomizers(connector -> connector.setProperty("relaxedQueryChars", "|{}[]" + File.separator));
        tomcat.addContextCustomizers(context -> context.addServletContainerInitializer(new WsSci(), null));
        return tomcat;
    }

    @Value("${server.port}")
    private int httpsPort;

    /**
     * 80端口重定向到本项目端口,http -> https
     * @return
     */
    private Connector createHTTPConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        //同时启用http(80)、https两个端口
        connector.setScheme("http");
        connector.setSecure(false);
        connector.setPort(80);
        connector.setRedirectPort(httpsPort);
        return connector;
    }
}
