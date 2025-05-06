package com.example.event_manager.configuration.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic updateElementTopic() {
        return TopicBuilder.name("update-element")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
