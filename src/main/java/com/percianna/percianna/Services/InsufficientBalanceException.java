package com.percianna.percianna.Services;

public class InsufficientBalanceException extends RuntimeException{

    public InsufficientBalanceException(String s) {
        super(s);
    }
}
