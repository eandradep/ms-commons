package com.eandrade.config.annotation;

import org.springframework.http.HttpStatus;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiResponseSuccess {
    String message() default "Operación exitosa";
    HttpStatus status() default HttpStatus.OK;
}

