package com.example.event_manager.exception.custom;

public class EventTypeNotFoundException extends RuntimeException {
    public EventTypeNotFoundException(String problemCode) {
        super("Event type " + problemCode + " not found!");
    }
}
