package com.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String instanceId;
    private String elementInstanceKey;
    private String activityType;
    @Lob
    private String variables;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
