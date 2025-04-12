package com.example.event_manager.exception.custom;

public class StatusNotFoundException extends RuntimeException {
    public StatusNotFoundException(String statusCode) {
        super("Status " + statusCode + " not found!");
    }
}
