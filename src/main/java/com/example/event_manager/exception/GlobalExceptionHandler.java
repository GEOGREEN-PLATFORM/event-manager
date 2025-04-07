package com.example.event_manager.exception;

import com.example.event_manager.exception.custom.EventNotFoundException;
import com.example.event_manager.exception.custom.HistoryNotFoundException;
import com.example.event_manager.exception.custom.StatusNotFoundException;
import com.example.event_manager.model.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({ EventNotFoundException.class })
    public ResponseEntity<ResponseDTO> catchEventNotFoundException(EventNotFoundException e) {
        logger.error("Произошла ошибка: {}", e.getMessage(), e);
        return new ResponseEntity<>(new ResponseDTO(
                HttpStatus.NOT_FOUND,
                e.getMessage()), HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler({ HistoryNotFoundException.class })
    public ResponseEntity<ResponseDTO> catchHistoryNotFoundException(HistoryNotFoundException e) {
        logger.error("Произошла ошибка: {}", e.getMessage(), e);
        return new ResponseEntity<>(new ResponseDTO(
                HttpStatus.NOT_FOUND,
                e.getMessage()), HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler({ StatusNotFoundException.class })
    public ResponseEntity<ResponseDTO> catchStatusNotFoundException(StatusNotFoundException e) {
        logger.error("Произошла ошибка: {}", e.getMessage(), e);
        return new ResponseEntity<>(new ResponseDTO(
                HttpStatus.NOT_FOUND,
                e.getMessage()), HttpStatus.NOT_FOUND
        );
    }

}
