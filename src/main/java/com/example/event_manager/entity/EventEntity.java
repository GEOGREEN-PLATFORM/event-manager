package com.example.event_manager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "event")
@Data
public class EventEntity {

    @Id
    @GeneratedValue(generator = "uuid-generator")
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    @NotNull
    private UUID id;

    @NotNull
    @Column(name = "geo_point_id")
    private UUID geoPointId;

    @NotNull
    @Column(name = "start_date")
    @PastOrPresent
    private LocalDate startDate;

    @Column(name = "end_date")
    @PastOrPresent
    private LocalDate endDate;

    @NotNull
    @Column(name = "last_update_date")
    @PastOrPresent
    private LocalDate lastUpdateDate;

    @NotNull
    @Column(name = "status")
    @Pattern(regexp = "ПЛАНИРУЕТСЯ|В РАБОТЕ|ПРИОСТАНОВЛЕНО|ЗАВЕРШЕНО", message = "Status must be one of: ПЛАНИРУЕТСЯ, В РАБОТЕ, ПРИОСТАНОВЛЕНО, ЗАВЕРШЕНО")
    private String status;

    @NotNull
    @Column(name = "event_type")
    @Pattern(regexp = "УСТРАНЕНИЕ БОРЩЕВИКА|УСТРЕНЕНИЕ ПОСЛЕДСТВИЙ ПОЖАРА|СВАЛКА", message = "Event type must be one of: УСТРАНЕНИЕ БОРЩЕВИКА, УСТРЕНЕНИЕ ПОСЛЕДСТВИЙ ПОЖАРА, СВАЛКА")
    private String eventType;

    @NotNull
    @Column(name = "description")
    @Size(max = 256)
    private String description;

    @NotNull
    @Column(name = "name")
    @Size(max = 256)
    private String name;

    @Column(name = "operator_name")
    @Size(max = 50)
    private String operatorName;

    @Column(name = "operator_id")
    private UUID operatorId;

    @NotNull
    @Column(name = "author_name")
    @Size(max = 50)
    private String authorName;

    @NotNull
    @Column(name = "author_id")
    private UUID authorId;
}
