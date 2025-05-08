package com.example.event_manager.exception.custom;

public class ImageLimitExceededException extends RuntimeException {
    public ImageLimitExceededException() {
        super("Превышен лимит количества изображений!");
    }
}
