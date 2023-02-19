package com.unigrad.funiverseappservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnexpectedEnumValueException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UnexpectedEnumValueException() {
        super(String.format("Unexpected Enum Value"));
    }

    public UnexpectedEnumValueException(String propertyName, String value) {
        super(String.format("Unexpected Enum Value: %s - %s", propertyName, value));
    }

}
