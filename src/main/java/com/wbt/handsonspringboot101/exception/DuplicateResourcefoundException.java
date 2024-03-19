package com.wbt.handsonspringboot101.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicateResourcefoundException extends RuntimeException {
    public DuplicateResourcefoundException(String message) {
        super(message);
    }
}
