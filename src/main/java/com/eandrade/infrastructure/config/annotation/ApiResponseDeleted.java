package com.eandrade.infrastructure.config.annotation;

import org.springframework.http.HttpStatus;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiResponseDeleted {
    String message() default "Recurso eliminado exitosamente";
    HttpStatus status() default HttpStatus.NO_CONTENT; // Código 204
}

