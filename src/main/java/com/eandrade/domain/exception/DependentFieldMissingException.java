package com.eandrade.domain.exception;

public class DependentFieldMissingException extends RuntimeException {

    public DependentFieldMissingException(String message) {
        super(message);
    }

    public DependentFieldMissingException(String message, Throwable cause) {
        super(message, cause);
    }

    public static DependentFieldMissingException forFields(String field1, String field2) {
        return new DependentFieldMissingException(
                String.format("Si se proporciona '%s', '%s' también es obligatorio", field1, field2)
        );
    }
}
