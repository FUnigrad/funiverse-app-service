package com.unigrad.funiverseappservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MissingRequiredPropertyException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public MissingRequiredPropertyException() {
        super(String.format("Missing required property!"));
    }

    public MissingRequiredPropertyException(String propertyName) {
        super(String.format("Missing required property: %s", propertyName));
    }

}
