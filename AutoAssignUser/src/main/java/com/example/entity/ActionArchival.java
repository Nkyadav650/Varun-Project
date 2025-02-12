package com.example.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ActionArchival {

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

