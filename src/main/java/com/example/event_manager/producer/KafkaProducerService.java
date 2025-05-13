package com.example.event_manager.producer;

import com.example.event_manager.producer.dto.UpdateElementDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, UpdateElementDTO> kafkaUpdateTemplate;

    @Value("${kafka.update.topic}")
    private String topic;

    public KafkaProducerService(KafkaTemplate<String, UpdateElementDTO> kafkaUpdateTemplate) {
        this.kafkaUpdateTemplate = kafkaUpdateTemplate;
    }

    public void sendUpdate(UpdateElementDTO update) {
        kafkaUpdateTemplate.send(topic, update);
    }
}
