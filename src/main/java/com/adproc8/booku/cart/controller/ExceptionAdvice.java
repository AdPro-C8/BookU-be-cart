package com.adproc8.booku.cart.controller;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponse handleNoSuchElementException(NoSuchElementException ex) {
        logger.error(ex.getMessage(), ex);
        ErrorResponse errorResponse = ErrorResponse.create(ex,
            HttpStatus.NOT_FOUND, ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handleException(Exception ex) {
        logger.error(ex.getMessage(), ex);
        ErrorResponse errorResponse = ErrorResponse.create(ex,
            HttpStatus.BAD_REQUEST, ex.getMessage());
        return errorResponse;
    }
}
