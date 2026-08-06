package com.eandrade.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "api.response")
public class ApiResponseProperties {

    /**
     * Allows an application to opt out of the HTTP response convention.
     */
    private boolean enabled = true;

    private List<String> basePackages = new ArrayList<>();

    public List<String> getBasePackages() {
        return basePackages;
    }

}
