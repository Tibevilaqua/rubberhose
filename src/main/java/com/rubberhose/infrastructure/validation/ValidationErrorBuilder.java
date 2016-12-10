package com.rubberhose.infrastructure.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

public class ValidationErrorBuilder {

    public static ValidationErrorDTO fromBindingErrors(Errors errors) {
        ValidationErrorDTO error = new ValidationErrorDTO("Validation failed. " + errors.getErrorCount() + " error(s)");
        for (ObjectError objectError : errors.getAllErrors()) {
            error.addValidationError(objectError.getDefaultMessage());
        }
        return error;
    }

    public static ValidationErrorDTO fromBindingErrors(String field, String value) {
        ValidationErrorDTO error = new ValidationErrorDTO("Validation failed, incompatible value. 1 error");
        error.addValidationError(String.format("%s%s", "Field:" , field));
        error.addValidationError(String.format("%s%s", "Value:" , value));
        return error;
    }
}