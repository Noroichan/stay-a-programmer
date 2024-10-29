package com.stay_a_programmer.controller;

import com.stay_a_programmer.exception.JokeException;
import com.stay_a_programmer.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException notFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundException.getMessage());
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<String> handleMissingCookie(MissingRequestCookieException missingRequestCookieException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(missingRequestCookieException.getCookieName() + " cookie is required!");
    }

    @ExceptionHandler(JokeException.class)
    public ResponseEntity<String> handleJokeException(JokeException jokeException) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(jokeException.getMessage());
    }
}
