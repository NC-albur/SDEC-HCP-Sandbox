package com.example.springdummyapp.model.response;
import java.time.LocalDateTime;

public record CreateThreadSuccessResponse(String id,
                                          String threadReference,
                                          String status,
                                          LocalDateTime createdTimeStamp,
                                          LocalDateTime lastUpdatedTimeStamp,
                                          LocalDateTime threadExpiryDate,
                                          String associatedCaseReference) {

}


