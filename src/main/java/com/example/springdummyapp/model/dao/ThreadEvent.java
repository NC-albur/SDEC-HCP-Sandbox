package com.example.springdummyapp.model.dao;

import java.time.LocalDateTime;


public class ThreadEvent {
    public String message;
    public LocalDateTime createdTimeStamp;
    public String toAdd;
    public ThreadEvent(boolean created, String threadId, String threadData){
        message = threadId + (created?" Created":" Updated");
        createdTimeStamp = LocalDateTime.now();
        toAdd = threadData;
    }
}
