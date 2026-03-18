package com.eandrade.domain.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s no encontrado con ID: %d", resourceName, id));
    }

    public ResourceNotFoundException(String resourceName, String field, String value) {
        super(String.format("%s no encontrado con %s: %s", resourceName, field, value));
    }

    public static ResourceNotFoundException emptyList(String resourceName) {
        return new ResourceNotFoundException(String.format("No se encontraron registros de %s", resourceName));
    }

    public static ResourceNotFoundException emptyList(String resourceName, String criteria) {
        return new ResourceNotFoundException(
                String.format("No se encontraron registros de %s con criterio: %s", resourceName, criteria));
    }
}
