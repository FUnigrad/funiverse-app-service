package com.unigrad.funiverseappservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ServiceCommunicateException extends RuntimeException{

    public ServiceCommunicateException(String message) {
        super(message);
    }
}