package com.unigrad.funiverseappservice.advice;

import com.unigrad.funiverseappservice.exception.MissingRequiredPropertyException;
import com.unigrad.funiverseappservice.exception.UnexpectedEnumValueException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class TokenControllerAdvice {

    @ExceptionHandler(value = MissingRequiredPropertyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleTokenRefreshException(MissingRequiredPropertyException ex, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
    }

    @ExceptionHandler(value = UnexpectedEnumValueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleUnexpectedEnumValueException(UnexpectedEnumValueException ex, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
    }
}