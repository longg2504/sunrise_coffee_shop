package com.cg.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");

//        registry.addMapping("/api/**")
//                .allowedOrigins(
//                    "http://localhost:3000",
//                    "http://localhost:5500",
//                    "http://localhost:8483",
//                    "http://127.0.0.1:5500",
//                    "http://localhost:11727"
//                )
//                .allowedOriginPatterns("*")
//                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
//                .allowedHeaders("*")
//                .allowCredentials(true)
//                .maxAge(3600);
    }
}
