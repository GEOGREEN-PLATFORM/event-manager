package com.example.event_manager.producer;

import com.example.event_manager.producer.dto.UpdateElementDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class KafkaProducerServiceTest {

    private KafkaTemplate<String, UpdateElementDTO> kafkaTemplate;
    private KafkaProducerService kafkaProducerService;

    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        kafkaProducerService = new KafkaProducerService(kafkaTemplate);

        // Вручную подставляем значение topic через reflection, так как @Value не срабатывает вне Spring
        var topicField = KafkaProducerService.class.getDeclaredFields()[1]; // поле topic
        topicField.setAccessible(true);
        try {
            topicField.set(kafkaProducerService, "test-topic");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("sendUpdate должен вызвать kafkaTemplate.send с правильными аргументами")
    void sendUpdate_shouldSendMessageToKafka() {
        UpdateElementDTO dto = new UpdateElementDTO();
        dto.setElementId(UUID.randomUUID());
        dto.setType("EVENT");
        dto.setStatus("NEW");

        kafkaProducerService.sendUpdate(dto);

        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<UpdateElementDTO> dtoCaptor = ArgumentCaptor.forClass(UpdateElementDTO.class);

        verify(kafkaTemplate).send(topicCaptor.capture(), dtoCaptor.capture());

        assertThat(topicCaptor.getValue()).isEqualTo("test-topic");
        assertThat(dtoCaptor.getValue()).isEqualTo(dto);
    }
}
