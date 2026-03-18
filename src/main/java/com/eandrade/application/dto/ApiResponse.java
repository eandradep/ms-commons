package com.eandrade.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String code;
    private String message;
    private T data;
    private ErrorDetail error;

    // Métodos estáticos para éxito
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code("SUCCESS")
                .message("Operación exitosa")
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        // Si el mensaje es nulo o vacío, usar mensaje por defecto
        if (message == null || message.isBlank()) {
            message = "Operación exitosa";
        }

        return ApiResponse.<T>builder()
                .code("SUCCESS")
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> created(T data) {
        return ApiResponse.<T>builder()
                .code("CREATED")
                .message("Recurso creado exitosamente")
                .data(data)
                .build();
    }

    // Métodos estáticos para errores
    public static <T> ApiResponse<T> error(String code, String message) {
        return ApiResponse.<T>builder()
                .code(code)
                .error(ErrorDetail.builder()
                        .message(message)
                        .build())
                .build();
    }

    public static <T> ApiResponse<T> error(String code, String message, String details) {
        return ApiResponse.<T>builder()
                .code(code)
                .error(ErrorDetail.builder()
                        .message(message)
                        .details(details)
                        .build())
                .build();
    }
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
class ErrorDetail {
    private String message;
    private String details;
    private String suggestion;
}