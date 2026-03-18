package com.eandrade.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Setter
@Configuration
@ConfigurationProperties(prefix = "api.response")
public class ApiResponseProperties {

    private List<String> basePackages = new ArrayList<>();

    public List<String> getBasePackages() {
        if (basePackages.isEmpty()) {
            return List.of("com.sofka.ms.clients.infrastructure.web");
        }
        return basePackages;
    }

}
