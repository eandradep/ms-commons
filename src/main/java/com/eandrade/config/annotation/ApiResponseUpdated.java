package com.eandrade.config.annotation;

import org.springframework.http.HttpStatus;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiResponseUpdated {
    String message() default "Recurso actualizado exitosamente";
    HttpStatus status() default HttpStatus.OK; // Código 200
}

