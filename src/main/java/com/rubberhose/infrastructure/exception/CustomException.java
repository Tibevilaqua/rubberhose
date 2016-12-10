package com.rubberhose.infrastructure.exception;

import org.springframework.http.HttpStatus;

/**
 * Major exception for all possible systems exceptions that can occur...
 * GlobalControllerExceptionHandler is holding all these exceptions.
 */
public class CustomException extends RuntimeException{

    private final HttpStatus httpStatus;
    private final String description;

    public CustomException(ExceptionEnum exceptionEnum) {
        this.description = exceptionEnum.getDescription();
        this.httpStatus = exceptionEnum.getHttpStatus();
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getDescription() {
        return description;
    }
}
