package com.eandrade.infrastructure.config;
import com.eandrade.application.dto.ApiResponse;
import com.eandrade.infrastructure.config.annotation.*;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import java.lang.reflect.Method;


@RestControllerAdvice
public class ApiResponseWrapper implements ResponseBodyAdvice<Object> {

    private final ApiResponseProperties properties;

    @Autowired
    public ApiResponseWrapper(ApiResponseProperties properties) {
        this.properties = properties;
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
        if (method == null) {
            return ApiResponse.success("Operación exitosa", body);
        }

        if (method.isAnnotationPresent(ApiResponseCreated.class)) {
            ApiResponseCreated annotation = method.getAnnotation(ApiResponseCreated.class);
            response.setStatusCode(annotation.status());
            return ApiResponse.success(annotation.message(), body);
        }

        if (method.isAnnotationPresent(ApiResponseSuccess.class)) {
            ApiResponseSuccess annotation = method.getAnnotation(ApiResponseSuccess.class);
            response.setStatusCode(annotation.status());
            return ApiResponse.success(annotation.message(), body);
        }

        if (method.isAnnotationPresent(ApiResponseFound.class)) {
            ApiResponseFound annotation = method.getAnnotation(ApiResponseFound.class);
            response.setStatusCode(annotation.status());
            return ApiResponse.success(annotation.message(), body);
        }

        if (method.isAnnotationPresent(ApiResponseDeleted.class)) {
            ApiResponseDeleted annotation = method.getAnnotation(ApiResponseDeleted.class);
            response.setStatusCode(annotation.status());
            return ApiResponse.success(annotation.message(), body);
        }

        if (method.isAnnotationPresent(ApiResponseUpdated.class)) {
            ApiResponseUpdated annotation = method.getAnnotation(ApiResponseUpdated.class);
            response.setStatusCode(annotation.status());
            return ApiResponse.success(annotation.message(), body);
        }

        return ApiResponse.success("Operación exitosa", body);
    }

    private boolean isControllerInConfiguredPackage(MethodParameter returnType) {
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