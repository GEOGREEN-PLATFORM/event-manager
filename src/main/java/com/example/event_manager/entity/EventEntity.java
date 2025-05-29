package com.example.event_manager.entity;

import com.example.event_manager.model.UserDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    @Column(name = "operator", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private UserDTO operator;

    @Column(name = "operator_id", insertable = false, updatable = false)
    private UUID operatorId;

    @Column(name = "author")
    @JdbcTypeCode(SqlTypes.JSON)
    private UserDTO author;

    @Column(name = "operator_full_text", insertable = false, updatable = false)
    private String operatorFullText;

}
