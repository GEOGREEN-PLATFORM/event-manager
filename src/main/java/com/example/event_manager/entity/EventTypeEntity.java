package com.example.event_manager.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "event_type")
@Data
public class EventTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault = false;
}
