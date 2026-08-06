package com.eandrade.application.dto;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ApiResponseTest {

    @Test
    void exposesStructuredErrorDetailsThroughThePublicContract() {
        Map<String, String> details = Map.of("email", "debe tener un formato válido");

        ApiResponse<Void> response = ApiResponse.errorWithDetails(
                "VALIDATION_ERROR", "Datos de entrada inválidos", details);

        assertThat(response.getError().getMessage()).isEqualTo("Datos de entrada inválidos");
        assertThat(response.getError().getDetails()).isEqualTo(details);
    }
}
