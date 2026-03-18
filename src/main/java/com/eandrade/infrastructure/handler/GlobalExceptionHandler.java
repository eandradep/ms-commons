package com.eandrade.infrastructure.handler;



import com.eandrade.application.dto.ApiResponse;
import com.eandrade.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles exceptions of type {@code DuplicateIdentificationException} and provides
     * a structured {@code ApiResponse} containing the error details.
     *
     * @param ex the {@code DuplicateIdentificationException} thrown during the
     *           application's execution
     * @return a {@code ResponseEntity} containing an {@code ApiResponse} with an
     *         error code "DUPLICATE_IDENTIFICATION", a relevant error message,
     *         and HTTP status {@code CONFLICT} (409)
     */
    @ExceptionHandler(DuplicateIdentificationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateIdentificationException(
            DuplicateIdentificationException ex) {

        ApiResponse<Void> response = ApiResponse.error(
                "DUPLICATE_IDENTIFICATION",
                "Error de identificación duplicada",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Handles exceptions of type {@code InsufficientBalanceException} and provides
     * a structured {@code ApiResponse} containing the error details.
     *
     * @param ex the {@code InsufficientBalanceException} that was thrown during the
     *           application's execution
     * @return a {@code ResponseEntity} containing an {@code ApiResponse} with error code
     *         "INSUFFICIENT_BALANCE", a descriptive error message, and an HTTP status
     *         of {@code BAD_REQUEST} (400)
     */
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ApiResponse<Void>> handleInsufficientBalanceException(
            InsufficientBalanceException ex) {

        ApiResponse<Void> response = ApiResponse.error(
                "INSUFFICIENT_BALANCE",
                "Saldo no disponible para realizar la operación",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    /**
     * Handles the {@code ResourceNotFoundException} and provides a standardized error response.
     *
     * @param ex the {@code ResourceNotFoundException} instance that was thrown
     * @return a {@code ResponseEntity} containing an {@code ApiResponse} object
     *         with error details and an HTTP status of {@code NOT_FOUND}
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(
            ResourceNotFoundException ex) {

        ApiResponse<Void> response = ApiResponse.error(
                "RESOURCE_NOT_FOUND",
                "Recurso no encontrado",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Handles {@code ResourceIdMismatchException} exceptions and provides a structured {@code ApiResponse}
     * object with error details. This method is triggered when there is a mismatch between an expected
     * and actual resource ID provided in the application.
     *
     * @param ex the {@code ResourceIdMismatchException} instance that encapsulates details about the ID mismatch
     * @return a {@code ResponseEntity} containing an {@code ApiResponse} with error code "RESOURCE_ID_MISMATCH",
     *         a descriptive error message, and an HTTP status of {@code BAD_REQUEST} (400)
     */
    @ExceptionHandler(ResourceIdMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceIdMismatchException(
            ResourceIdMismatchException ex) {

        ApiResponse<Void> response = ApiResponse.error(
                "RESOURCE_ID_MISMATCH",
                "El ID proporcionado no coincide",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles the {@code ResourceIdIncompatibleException} and provides a structured error response.
     *
     * @param ex the {@code ResourceIdIncompatibleException} instance containing error details
     * @return a {@code ResponseEntity} containing an {@code ApiResponse} with an error code
     *         "RESOURCE_ID_INCOMPATIBLE", a descriptive error message, and an HTTP status of BAD_REQUEST (400)
     */
    @ExceptionHandler(ResourceIdIncompatibleException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceIdIncompatibleException(
            ResourceIdIncompatibleException ex) {

        ApiResponse<Void> response = ApiResponse.error(
                "RESOURCE_ID_INCOMPATIBLE",
                "Los IDs proporcionados no son compatibles",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles exceptions of type {@code DependentFieldMissingException} and provides
     * a structured {@code ApiResponse} with error details.
     *
     * @param ex the {@code DependentFieldMissingException} thrown due to a missing dependent field
     * @return a {@code ResponseEntity} containing an {@code ApiResponse} with an error code
     *         "BAD_REQUEST", a descriptive error message, and HTTP status {@code BAD_REQUEST} (400)
     */
    @ExceptionHandler(DependentFieldMissingException.class)
    public ResponseEntity<ApiResponse<Void>> handleDependentFieldMissingException(
            DependentFieldMissingException ex) {

        ApiResponse<Void> response = ApiResponse.error(
                "BAD_REQUEST",
                "Campos dependientes faltantes",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    /**
     * Handles exceptions of type {@link MethodArgumentNotValidException} and returns a structured
     * response containing validation error details. The method processes field errors,
     * including the rejected value, the error message, and the associated validation code.
     *
     * @param ex the exception containing validation error details
     * @return a {@link ResponseEntity} containing an error response with the validation issue details,
     *         along with a BAD_REQUEST HTTP status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, Object> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> {
                            Map<String, Object> errorDetails = new HashMap<>();
                            errorDetails.put("message", fieldError.getDefaultMessage());
                            errorDetails.put("rejectedValue", fieldError.getRejectedValue());
                            errorDetails.put("code", fieldError.getCode());
                            return errorDetails;
                        }
                ));

        Map<String, Object> response = new HashMap<>();
        response.put("code", "VALIDATION_ERROR");
        response.put("message", "Datos de entrada inválidos");
        response.put("type", "BAD_REQUEST");
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles general exceptions that occur within the application and returns
     * an appropriate error response to the client.
     *
     * @param ex the exception that was thrown
     * @return a ResponseEntity containing an ApiResponse with error details and an INTERNAL_SERVER_ERROR status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
        ApiResponse<Void> response = ApiResponse.error(
                "INTERNAL_ERROR",
                "Error interno del servidor",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

