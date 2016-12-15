package com.rubberhose.infrastructure.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.rubberhose.infrastructure.validation.ValidationErrorBuilder;
import com.rubberhose.infrastructure.validation.ValidationErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Created by root on 05/12/16.
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {


    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseEntity<ExceptionDTO> handleConflict(CustomException customException) {

        HttpStatus responseStatus = customException.getHttpStatus();
        return new ResponseEntity<>(new ExceptionDTO(customException.getDescription()), responseStatus);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ValidationErrorDTO handleConflict(MethodArgumentNotValidException e) {
        return ValidationErrorBuilder.fromBindingErrors(e.getBindingResult());
    }


    @ExceptionHandler(InvalidFormatException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ValidationErrorDTO handleConflict(InvalidFormatException e) {
        String wrongValue = e.getValue().toString();
        String targetedTypeName = e.getTargetType().getTypeName();
        return ValidationErrorBuilder.fromBindingErrors(targetedTypeName,wrongValue);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ValidationErrorDTO handleConflict(MethodArgumentTypeMismatchException e) {
        String wrongValue = e.getValue().toString();
        String targetedTypeName = e.getName();
        return ValidationErrorBuilder.fromBindingErrors(targetedTypeName,wrongValue);
    }


}
