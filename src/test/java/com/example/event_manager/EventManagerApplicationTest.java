package com.example.event_manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "spring.liquibase.enabled=false")
public class EventManagerApplicationTest {

    @Test
    void contextLoads() {

    }
}
