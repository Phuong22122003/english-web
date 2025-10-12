package com.english.learning_service.configuration;

import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    /**
     * Bật mức log FULL để hiển thị toàn bộ request/response của Feign
     * Bao gồm: URL, method, headers, body, status code, v.v.
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * Cấu hình timeout (tùy chọn)
     * connectTimeoutMillis = 5000ms
     * readTimeoutMillis = 10000ms
     */
    @Bean
    public Request.Options options() {
        return new Request.Options(5000, 10000);
    }

    /**
     * Thêm interceptor để gắn header Authorization (nếu cần)
     * Có thể bỏ nếu chưa dùng Keycloak hoặc JWT
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // Ví dụ: thêm Authorization header nếu có token
            // requestTemplate.header("Authorization", "Bearer " + token);
        };
    }
}
