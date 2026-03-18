package com.eandrade.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiResponseWrapperConfig {

    @Bean
    public ApiResponseProperties apiResponseProperties() {
        return new ApiResponseProperties();
    }

    @Bean
    public ApiResponseWrapper apiResponseWrapper(ApiResponseProperties properties) {
        return new ApiResponseWrapper(properties);
    }
}
