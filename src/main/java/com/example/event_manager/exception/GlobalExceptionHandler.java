package com.example.event_manager.exception;

import com.example.event_manager.exception.custom.EventNotFoundException;
import com.example.event_manager.exception.custom.HistoryNotFoundException;
import com.example.event_manager.model.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({ EventNotFoundException.class })
    public ResponseDTO catchEventNotFoundException(EventNotFoundException e) {
        logger.error("Произошла ошибка: {}", e.getMessage(), e);
        return new ResponseDTO(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );
    }

    @ExceptionHandler({ HistoryNotFoundException.class })
    public ResponseDTO catchHistoryNotFoundException(HistoryNotFoundException e) {
        logger.error("Произошла ошибка: {}", e.getMessage(), e);
        return new ResponseDTO(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );
    }

}
