package com.example.event_manager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "event_history")
@Data
public class EventHistoryEntity {

    @Id
    @GeneratedValue(generator = "uuid-generator")
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    @NotNull
    private UUID id;

    @NotNull
    @Column(name = "event_id")
    private UUID eventId;

    @NotNull
    @Column(name = "record_date")
    @PastOrPresent
    private LocalDate recordDate;

    @NotNull
    @Column(name = "record_type")
    @Pattern(regexp = "ПРОВЕДЕННАЯ РАБОТА|СНЯТИЕ СОСТОЯНИЯ", message = "Record type must be one of: ПРОВЕДЕННАЯ РАБОТА, СНЯТИЕ СОСТОЯНИЯ")
    private String recordType;

    @NotNull
    @Column(name = "description")
    private String description;

    @Column(name = "photos")
    private List<UUID> photos;

    @Column(name = "operator_name")
    @Size(max = 50)
    private String operatorName;

    @Column(name = "operator_id")
    private UUID operatorId;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;


}
