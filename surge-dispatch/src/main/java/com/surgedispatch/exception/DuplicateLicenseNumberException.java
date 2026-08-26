package com.surgedispatch.exception;

public class DuplicateLicenseNumberException extends RuntimeException {

    public DuplicateLicenseNumberException(String message){
        super(message);
    }
}
