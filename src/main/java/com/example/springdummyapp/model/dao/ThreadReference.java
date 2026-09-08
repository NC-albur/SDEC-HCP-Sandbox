package com.example.springdummyapp.model.dao;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@RequiredArgsConstructor
@Data
public class ThreadReference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String threadReference;
    private String status;
    private LocalDateTime createdTimeStamp;
    private LocalDateTime lastUpdatedTimeStamp;
    private LocalDateTime threadExpiryDate;
    private String associatedCaseReference;
}
