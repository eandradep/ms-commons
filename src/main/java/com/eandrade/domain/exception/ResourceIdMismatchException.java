package com.eandrade.domain.exception;

public class ResourceIdMismatchException extends RuntimeException {

    public ResourceIdMismatchException(String message) {
        super(message);
    }

    public ResourceIdMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceIdMismatchException(Long expectedId, Long actualId) {
        super(String.format("El ID proporcionado no coincide: esperado %d, recibido %d", expectedId, actualId));
    }

    public ResourceIdMismatchException(String resourceName, Long expectedId, Long actualId) {
        super(String.format("El ID de %s no coincide: esperado %d, recibido %d", resourceName, expectedId, actualId));
    }
}

