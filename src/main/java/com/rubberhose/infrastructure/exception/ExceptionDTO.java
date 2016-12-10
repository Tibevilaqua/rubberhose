package com.rubberhose.infrastructure.exception;

/**
 * Created by root on 05/12/16.
 */
public class ExceptionDTO {

    private String message;

    public ExceptionDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
