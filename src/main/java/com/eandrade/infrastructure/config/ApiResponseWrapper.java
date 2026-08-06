package com.eandrade.infrastructure.config;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.eandrade.application.dto.ApiResponse;
import com.eandrade.infrastructure.config.annotation.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;


@RestControllerAdvice
@ConditionalOnProperty(prefix = "api.response", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ApiResponseWrapper implements ResponseBodyAdvice<Object> {

    private final ApiResponseProperties properties;
    private final ObjectMapper objectMapper;

    public ApiResponseWrapper(ApiResponseProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    /**
     * @deprecated Prefer constructor injection with the application's ObjectMapper.
     */
    @Deprecated(since = "1.1.0", forRemoval = true)
    public ApiResponseWrapper(ApiResponseProperties properties) {
        this(properties, new ObjectMapper());
    }

    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class converterType) {
        if (!isControllerInConfiguredPackage(returnType)) {
            return false;
        }
        return !isExcludedFromWrapping(returnType);
    }

    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class selectedConverterType,
                                  @NonNull ServerHttpRequest request,
                                  @NonNull ServerHttpResponse response) {

        if (body instanceof ApiResponse || body instanceof ResponseEntity) {
            return body;
        }

        Method method = returnType.getMethod();
        Object wrappedBody;
        if (method == null) {
            wrappedBody = ApiResponse.success("Operación exitosa", body);
        } else if (method.isAnnotationPresent(ApiResponseCreated.class)) {
            ApiResponseCreated annotation = method.getAnnotation(ApiResponseCreated.class);
            response.setStatusCode(annotation.status());
            wrappedBody = ApiResponse.success(annotation.message(), body);
        } else if (method.isAnnotationPresent(ApiResponseSuccess.class)) {
            ApiResponseSuccess annotation = method.getAnnotation(ApiResponseSuccess.class);
            response.setStatusCode(annotation.status());
            wrappedBody = ApiResponse.success(annotation.message(), body);
        } else if (method.isAnnotationPresent(ApiResponseFound.class)) {
            ApiResponseFound annotation = method.getAnnotation(ApiResponseFound.class);
            response.setStatusCode(annotation.status());
            wrappedBody = ApiResponse.success(annotation.message(), body);
        } else if (method.isAnnotationPresent(ApiResponseDeleted.class)) {
            ApiResponseDeleted annotation = method.getAnnotation(ApiResponseDeleted.class);
            response.setStatusCode(annotation.status());
            if (annotation.status().is2xxSuccessful() && annotation.status().value() == 204) {
                return null;
            }
            wrappedBody = ApiResponse.success(annotation.message(), body);
        } else if (method.isAnnotationPresent(ApiResponseUpdated.class)) {
            ApiResponseUpdated annotation = method.getAnnotation(ApiResponseUpdated.class);
            response.setStatusCode(annotation.status());
            wrappedBody = ApiResponse.success(annotation.message(), body);
        } else {
            wrappedBody = ApiResponse.success("Operación exitosa", body);
        }

        if (body instanceof String && StringHttpMessageConverter.class.isAssignableFrom(selectedConverterType)) {
            try {
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return objectMapper.writeValueAsString(wrappedBody);
            } catch (JsonProcessingException ex) {
                throw new IllegalStateException("No se pudo serializar la respuesta de la API", ex);
            }
        }

        return wrappedBody;
    }

    private boolean isControllerInConfiguredPackage(MethodParameter returnType) {
        if (properties.getBasePackages().isEmpty()) {
            return true;
        }
        String controllerPackage = returnType.getContainingClass().getPackageName();
        return properties.getBasePackages().stream()
                .anyMatch(controllerPackage::startsWith);
    }

    private boolean isExcludedFromWrapping(MethodParameter returnType) {
        boolean isMethodExcluded = returnType.hasMethodAnnotation(NoApiResponseWrapper.class);
        boolean isClassExcluded = returnType.getContainingClass().isAnnotationPresent(NoApiResponseWrapper.class);
        return isMethodExcluded || isClassExcluded;
    }
}
