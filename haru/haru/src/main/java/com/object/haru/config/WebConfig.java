package com.object.haru.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 *    프로젝트의 MvcWeb 설정을 담당하는 Config 파일
 *
 *   @version          1.00    2023.02.07
 *   @author           한승완
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) { //외부 IP 접근 허용
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST","DELETE","PUT");
    }

}