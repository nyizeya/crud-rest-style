package com.example.demo.controller;

import com.example.demo.model.entity.dto.DataTablesOutput;
import com.example.demo.security.exception.DuplicateResourceException;
import com.example.demo.security.exception.InvalidTokenException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<DataTablesOutput<String>> entityNotFoundException(EntityNotFoundException exception) {
        return createDataTablesOutput(NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<DataTablesOutput<String>> badCredentials(BadCredentialsException exception) {
        return createDataTablesOutput(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<DataTablesOutput<String>> signatureException(SignatureException exception) {
        return createDataTablesOutput(BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<DataTablesOutput<String>> duplicateResourceException(DuplicateResourceException exception) {
        return createDataTablesOutput(CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<DataTablesOutput<String>> invalidTokenException(InvalidTokenException exception) {
        return createDataTablesOutput(BAD_REQUEST, exception.getMessage());
    }

    private ResponseEntity<DataTablesOutput<String>> createDataTablesOutput(HttpStatus status, String error) {
        DataTablesOutput<String> dataTablesOutput = DataTablesOutput.<String>builder()
                .error(error)
                .build();

        return new ResponseEntity<>(dataTablesOutput, status);
    }
}
