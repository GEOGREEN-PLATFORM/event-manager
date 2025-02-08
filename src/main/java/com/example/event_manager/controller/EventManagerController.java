package com.example.event_manager.controller;

import com.example.event_manager.entity.EventEntity;
import com.example.event_manager.model.CreateEventDTO;
import com.example.event_manager.service.EventManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventManagerController {

    @Autowired
    private EventManagerService eventManagerService;

    @PostMapping
    public EventEntity saveNewEvent(@RequestBody CreateEventDTO createEventDTO) {
        return eventManagerService.createNewEvent(createEventDTO);
    }
}
