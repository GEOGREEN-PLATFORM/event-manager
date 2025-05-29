package com.example.event_manager.exception;

import com.example.event_manager.exception.custom.*;
import com.example.event_manager.model.ResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("Должен вернуть 404 для EventNotFoundException")
    void catchEventNotFoundException_shouldReturn404() {
        UUID id = UUID.randomUUID();

        EventNotFoundException ex = new EventNotFoundException(id);
        ResponseEntity<ResponseDTO> response = handler.catchEventNotFoundException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Event with id " + id + " not found!");
    }

    @Test
    @DisplayName("Должен вернуть 404 для HistoryNotFoundException")
    void catchHistoryNotFoundException_shouldReturn404() {
        UUID id = UUID.randomUUID();

        HistoryNotFoundException ex = new HistoryNotFoundException(id);
        ResponseEntity<ResponseDTO> response = handler.catchHistoryNotFoundException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getMessage()).isEqualTo("History with id " + id + " not found!");
    }

    @Test
    @DisplayName("Должен вернуть 404 для StatusNotFoundException")
    void catchStatusNotFoundException_shouldReturn404() {
        StatusNotFoundException ex = new StatusNotFoundException("statusCode");
        ResponseEntity<ResponseDTO> response = handler.catchStatusNotFoundException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getMessage()).isEqualTo("Status " + "statusCode" + " not found!");
    }

    @Test
    @DisplayName("Должен вернуть 404 для ProblemNotFoundException")
    void catchProblemNotFoundException_shouldReturn404() {
        ProblemNotFoundException ex = new ProblemNotFoundException("problemCode");
        ResponseEntity<ResponseDTO> response = handler.catchProblemNotFoundException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getMessage()).isEqualTo("Problem " + "problemCode" + " not found!");
    }

    @Test
    @DisplayName("Должен вернуть 404 для EventTypeNotFoundException")
    void catchEventTypeNotFoundException_shouldReturn404() {
        EventTypeNotFoundException ex = new EventTypeNotFoundException("problemCode");
        ResponseEntity<ResponseDTO> response = handler.catchEventTypeNotFoundException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getMessage()).isEqualTo("Event type " + "problemCode" + " not found!");
    }

    @Test
    @DisplayName("Должен вернуть 400 для ImageLimitExceededException")
    void catchImageLimitExceededException_shouldReturn400() {
        ImageLimitExceededException ex = new ImageLimitExceededException();
        ResponseEntity<ResponseDTO> response = handler.catchImageLimitExceededException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getMessage()).isEqualTo("Превышен лимит количества изображений!");
    }
}
