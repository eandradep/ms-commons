package com.eandrade.infrastructure.config.annotation;


import org.springframework.http.HttpStatus;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiResponseCreated {
    String message() default "Recurso creado exitosamente";
    HttpStatus status() default HttpStatus.CREATED; // Código 201
}
