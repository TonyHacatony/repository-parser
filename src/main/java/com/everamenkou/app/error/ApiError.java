package com.everamenkou.app.error;

import org.springframework.http.HttpStatusCode;

public class ApiError {

    private final int status;
    private final String message;

    public ApiError(String message, HttpStatusCode status) {
        this.message = message;
        this.status = status.value();
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
