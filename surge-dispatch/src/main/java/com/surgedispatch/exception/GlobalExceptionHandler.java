package com.surgedispatch.exception;

import org.apache.commons.lang3.builder.EqualsExclude;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<String> handleDuplicateEmailException(DuplicateEmailException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @ExceptionHandler(RiderNotFoundException.class)
        public ResponseEntity<String> handleRiderNotFoundException(RiderNotFoundException ex){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> methodArgumentNotValidException(MethodArgumentNotValidException ex){

            String message = ex.getBindingResult()
                    .getAllErrors()
                    .get(0)
                    .getDefaultMessage();
        return ResponseEntity
                .status((HttpStatus.BAD_REQUEST))
                .body(message);
        }
    }



