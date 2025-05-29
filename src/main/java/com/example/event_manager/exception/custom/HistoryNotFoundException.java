package com.example.event_manager.exception.custom;

import java.util.UUID;

public class HistoryNotFoundException extends RuntimeException {
    public HistoryNotFoundException(UUID id) {
        super("History with id " + id + " not found!");
    }
}
