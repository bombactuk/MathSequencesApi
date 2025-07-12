package com.task.mathsequences.exception;

public class FactorialException extends RuntimeException {

    public FactorialException(String message) {
        super(message);
    }

    public FactorialException(Throwable e) {
        super(e);
    }

    public FactorialException(String message, Throwable e) {
        super(message, e);
    }

}