package com.example.fooddiary.exception;

public class LoggingException extends RuntimeException {
    public LoggingException(String message) {
        super(message);
    }
}