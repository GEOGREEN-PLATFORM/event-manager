package com.example.event_manager.entity.spec;

import com.example.event_manager.entity.EventEntity;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class EntitySpecifications {
    public static Specification<EventEntity> hasStatusValue(String fieldValue) {
        return (root, query, criteriaBuilder) -> {
            if (fieldValue == null || fieldValue.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("statusCode"), fieldValue);
        };
    }

    public static Specification<EventEntity> hasOperatorIdValue(UUID fieldValue) {
        return (root, query, criteriaBuilder) -> {
            if (fieldValue == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("operatorId"), fieldValue);
        };
    }

    public static Specification<EventEntity> hasStartDateBetween(Instant startDate, Instant endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null || endDate == null) {
                return criteriaBuilder.conjunction();
            }

            Instant adjustedEndDate = endDate;
            if (startDate.equals(adjustedEndDate)) {
                adjustedEndDate = adjustedEndDate.plus(24, ChronoUnit.HOURS);
            }

            return criteriaBuilder.between(root.get("startDate"), startDate, adjustedEndDate);
        };
    }

    public static Specification<EventEntity> hasEndDateBetween(Instant startDate, Instant endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null || endDate == null) {
                return criteriaBuilder.conjunction();
            }

            Instant adjustedEndDate = endDate;
            if (startDate.equals(adjustedEndDate)) {
                adjustedEndDate = adjustedEndDate.plus(24, ChronoUnit.HOURS);
            }

            return criteriaBuilder.between(root.get("endDate"), startDate, adjustedEndDate);
        };
    }

    public static Specification<EventEntity> hasUpdateDateBetween(Instant startDate, Instant endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null || endDate == null) {
                return criteriaBuilder.conjunction();
            }

            Instant adjustedEndDate = endDate;
            if (startDate.equals(adjustedEndDate)) {
                adjustedEndDate = adjustedEndDate.plus(24, ChronoUnit.HOURS);
            }

            return criteriaBuilder.between(root.get("lastUpdateDate"), startDate, adjustedEndDate);
        };
    }

    public static Specification<EventEntity> nameContains(String searchTerm) {
        return (root, query, cb) -> {
            if (searchTerm == null || searchTerm.isEmpty()) {
                return null;
            }
            return cb.like(
                    cb.lower(root.get("name")),
                    "%" + searchTerm.toLowerCase() + "%"
            );
        };
    }

    public static Specification<EventEntity> operaotrNameContains(String searchTerm) {
        return (root, query, cb) -> {
            if (StringUtils.isEmpty(searchTerm)) {
                return cb.conjunction();
            }

            return cb.like(
                    cb.lower(root.get("operatorFullText")),
                    "%" + searchTerm.toLowerCase() + "%"
            );
        };
    }

    public static Specification<EventEntity> hasEventType(String fieldValue) {
        return (root, query, criteriaBuilder) -> {
            if (fieldValue == null || fieldValue.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("eventType"), fieldValue);
        };
    }

    public static Specification<EventEntity> hasProblemAreaType(String fieldValue) {
        return (root, query, criteriaBuilder) -> {
            if (fieldValue == null || fieldValue.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("problemAreaType"), fieldValue);
        };
    }

    public static Specification<EventEntity> hasGeoPointId(UUID fieldValue) {
        return (root, query, criteriaBuilder) -> {
            if (fieldValue == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("geoPointId"), fieldValue);
        };
    }
}