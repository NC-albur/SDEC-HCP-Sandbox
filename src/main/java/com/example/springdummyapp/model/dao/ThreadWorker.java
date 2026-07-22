package com.example.springdummyapp.model.dao;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@Entity
@RequiredArgsConstructor
@Data
@Table(name = "THREAD_WORKER")
public class ThreadWorker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thread_id", nullable = false)
    private Thread thread;
    private String caseWorkerName;
    private Long caseWorkerId;
    private Timestamp lastLoggedIn;
}
