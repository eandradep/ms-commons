package com.eandrade.config.annotation;

import org.springframework.http.HttpStatus;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiResponseFound {
    String message() default "Datos encontrados exitosamente";
    HttpStatus status() default HttpStatus.OK;
}
