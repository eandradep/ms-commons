package com.eandrade.infrastructure.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.eandrade.infrastructure.config.annotation.ApiResponseDeleted;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ApiResponseWrapperTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void serializesWrappedStringResponsesAsJson() throws Exception {
        ApiResponseWrapper wrapper = wrapper();
        MethodParameter returnType = returnType("text");
        ServerHttpResponse response = response();

        Object result = wrapper.beforeBodyWrite("ok", returnType, MediaType.TEXT_PLAIN,
                StringHttpMessageConverter.class, mock(ServerHttpRequest.class), response);

        assertThat(result).isInstanceOf(String.class);
        JsonNode json = objectMapper.readTree((String) result);
        assertThat(json.path("code").asText()).isEqualTo("SUCCESS");
        assertThat(json.path("data").asText()).isEqualTo("ok");
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }

    @Test
    void returnsNoBodyForNoContentDeleteResponses() throws Exception {
        ApiResponseWrapper wrapper = wrapper();
        ServerHttpResponse response = response();

        Object result = wrapper.beforeBodyWrite(null, returnType("delete"), MediaType.APPLICATION_JSON,
                StringHttpMessageConverter.class, mock(ServerHttpRequest.class), response);

        assertThat(result).isNull();
        verify(response).setStatusCode(HttpStatus.NO_CONTENT);
    }

    @Test
    void supportsControllersInAnyPackageWithoutConfiguration() throws Exception {
        ApiResponseWrapper wrapper = new ApiResponseWrapper(new ApiResponseProperties(), objectMapper);

        assertThat(wrapper.supports(returnType("text"), StringHttpMessageConverter.class)).isTrue();
    }

    private ApiResponseWrapper wrapper() {
        ApiResponseProperties properties = new ApiResponseProperties();
        properties.setBasePackages(List.of("com.eandrade"));
        return new ApiResponseWrapper(properties, objectMapper);
    }

    private MethodParameter returnType(String methodName) throws NoSuchMethodException {
        Method method = TestController.class.getDeclaredMethod(methodName);
        return new MethodParameter(method, -1);
    }

    private ServerHttpResponse response() {
        ServerHttpResponse response = mock(ServerHttpResponse.class);
        when(response.getHeaders()).thenReturn(new HttpHeaders());
        return response;
    }

    private static class TestController {
        String text() {
            return "ok";
        }

        @ApiResponseDeleted
        Void delete() {
            return null;
        }
    }
}
