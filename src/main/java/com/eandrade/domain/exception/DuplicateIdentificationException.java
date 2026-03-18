package com.eandrade.domain.exception;

public class DuplicateIdentificationException extends RuntimeException {

    public DuplicateIdentificationException(String message) {
        super(message);
    }

    public DuplicateIdentificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateIdentificationException(String identification, String entityName) {
        super(String.format("Ya existe un %s con la identificación: %s", entityName, identification));
    }
}
