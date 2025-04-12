package com.example.event_manager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;
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
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @NotNull
    @Column(name = "last_update_date")
    private Instant lastUpdateDate;

    @JoinColumn(name = "status_code", referencedColumnName = "code")
    private String statusCode;

    @NotNull
    @Column(name = "event_type")
    @JoinColumn(name = "event_type", referencedColumnName = "code")
    private String eventType;

    @NotNull
    @Column(name = "problem_area_type")
    @JoinColumn(name = "problem_type", referencedColumnName = "code")
    private String problemAreaType;

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
