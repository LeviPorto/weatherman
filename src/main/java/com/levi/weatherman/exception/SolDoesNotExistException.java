package com.levi.weatherman.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS)
public class SolDoesNotExistException extends RuntimeException {

    public SolDoesNotExistException(String message) {
        super(message);
    }

}
