package com.example.event_manager.exception.custom;

public class ProblemNotFoundException extends RuntimeException {
    public ProblemNotFoundException(String problemCode) {
        super("Problem " + problemCode + " not found!");
    }
}
