package com.example.springdummyapp.model.request;
import java.time.LocalDateTime;

public record CreateThreadRequest(String threadName,
                                  String id,
                                  String threadReference,
                                  String status,
                                  String associatedCaseReference) {

}
