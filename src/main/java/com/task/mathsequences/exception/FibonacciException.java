package com.task.mathsequences.exception;

public class FibonacciException extends RuntimeException {

    public FibonacciException(String message) {
        super(message);
    }

    public FibonacciException(Throwable e) {
        super(e);
    }

    public FibonacciException(String message, Throwable e) {
        super(message, e);
    }

}
