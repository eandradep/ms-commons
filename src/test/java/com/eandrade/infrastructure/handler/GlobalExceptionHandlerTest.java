package com.eandrade.infrastructure.handler;

import com.eandrade.application.dto.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    @Test
    void returnsTheStandardContractForValidationErrorsWithoutRejectedValues() throws NoSuchMethodException {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new TestRequest(), "request");
        bindingResult.rejectValue("password", "NotBlank", "no debe estar vacía");
        Method method = TestController.class.getDeclaredMethod("create", String.class);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(
                new MethodParameter(method, 0), bindingResult);

        ResponseEntity<ApiResponse<Void>> result = new GlobalExceptionHandler()
                .handleValidationExceptions(exception);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody().getCode()).isEqualTo("VALIDATION_ERROR");
        assertThat(result.getBody().getError().getDetails()).isEqualTo(Map.of("password", "no debe estar vacía"));
    }

    @Test
    void doesNotExposeInternalExceptionMessages() {
        ResponseEntity<ApiResponse<Void>> result = new GlobalExceptionHandler()
                .handleGeneralException(new IllegalStateException("database password leaked"));

        assertThat(result.getBody().getError().getDetails()).isNull();
    }

    private static class TestController {
        void create(String value) {
        }
    }

    private static class TestRequest {
        private String password;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
