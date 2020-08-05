package com.mycode.vendingmachine.exceptions;

public class NoSuchDenominationException extends Exception {

    public NoSuchDenominationException(String message) {
        super(message);
    }

    public NoSuchDenominationException(String message, Throwable cause) {
        super(message, cause);
    }
    
}


