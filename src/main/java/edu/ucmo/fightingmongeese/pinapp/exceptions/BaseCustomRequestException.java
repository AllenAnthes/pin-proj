package edu.ucmo.fightingmongeese.pinapp.exceptions;

import org.springframework.http.HttpStatus;

public abstract class BaseCustomRequestException extends RuntimeException {

    public HttpStatus status;

    BaseCustomRequestException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
