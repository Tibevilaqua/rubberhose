package com.rubberhose.infrastructure.exception;

import org.springframework.http.HttpStatus;

/**
 * Enum holding all possible exceptions in the Web Service.
 */
public enum ExceptionEnum {

    INNACURATE_CROSS_PATTERN(HttpStatus.BAD_REQUEST, "Unaccepted cross value. Accepted values are composed of 1 letter and up to 10 numbers e.g A123456 ");


    private HttpStatus httpStatus;
    private String description;


    ExceptionEnum(HttpStatus httpStatus, String description) {
        this.httpStatus = httpStatus;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}