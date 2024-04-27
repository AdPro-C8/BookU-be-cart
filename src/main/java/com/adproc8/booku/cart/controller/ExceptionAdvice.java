package com.adproc8.booku.cart.controller;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class ExceptionAdvice {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponse handleNoSuchElementException(NoSuchElementException ex) {
        ErrorResponse errorResponse = ErrorResponse.create(ex,
            HttpStatus.NOT_FOUND, ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handleException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.create(ex,
            HttpStatus.BAD_REQUEST, ex.getMessage());
        return errorResponse;
    }
}
