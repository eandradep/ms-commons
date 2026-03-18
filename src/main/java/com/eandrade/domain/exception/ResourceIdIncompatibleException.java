package com.eandrade.domain.exception;

public class ResourceIdIncompatibleException extends RuntimeException {

    public ResourceIdIncompatibleException(String message) {
        super(message);
    }

    public ResourceIdIncompatibleException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceIdIncompatibleException(Long id1, Long id2) {
        super(String.format("Los IDs proporcionados no son compatibles: %d y %d", id1, id2));
    }

    public ResourceIdIncompatibleException(String resourceName, Long id1, Long id2) {
        super(String.format("Los IDs de %s no son compatibles: %d y %d", resourceName, id1, id2));
    }
}