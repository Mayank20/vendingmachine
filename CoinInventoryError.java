package com.mycode.vendingmachine.exceptions;


public class CoinInventoryError extends Exception {
     
    public CoinInventoryError(String message) {
        super(message);
    }
    
    public CoinInventoryError(String message, Throwable cause) {
        super(message, cause);
    }
    
}
