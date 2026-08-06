package com.eandrade.infrastructure.config;

import com.eandrade.infrastructure.handler.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(ApiResponseProperties.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ApiResponseWrapperConfig {

    @Bean
    @ConditionalOnProperty(prefix = "api.response", name = "enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    public ApiResponseWrapper apiResponseWrapper(ApiResponseProperties properties, ObjectMapper objectMapper) {
        return new ApiResponseWrapper(properties, objectMapper);
    }

    @Bean
    @ConditionalOnProperty(prefix = "api.response", name = "enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

}
