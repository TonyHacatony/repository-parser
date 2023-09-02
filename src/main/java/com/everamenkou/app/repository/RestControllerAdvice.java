package com.everamenkou.app.repository;

import com.everamenkou.app.error.ApiError;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@ControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ApiError> handleRestTemplateErrors(HttpClientErrorException e) {
        HttpStatusCode statusCode = e.getStatusCode();
        if (statusCode == HttpStatus.NOT_FOUND) {
            return new ResponseEntity<>(new ApiError("The user does not exist in GitHub", statusCode), statusCode);
        }
        if (statusCode == HttpStatus.FORBIDDEN) {
            return new ResponseEntity<>(new ApiError(e.getStatusText(), statusCode), statusCode);
        }

        return new ResponseEntity<>(new ApiError("Something goes wrong", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Bean
    public RestTemplate createTemplate() {
        return new RestTemplate();
    }
}
