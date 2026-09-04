package com.example.springdummyapp.controller;

import com.example.springdummyapp.model.dao.Thread;
import com.example.springdummyapp.model.dao.ThreadReference;
import com.example.springdummyapp.model.request.CreateThreadRequest;
import com.example.springdummyapp.model.response.CreateThreadSuccessResponse;
import com.example.springdummyapp.service.ThreadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "v1/dummy")
@RequiredArgsConstructor
public class DummyController {

    private final ThreadService threadService;

//    @GetMapping("/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public String dummyGet(@PathVariable Long id) {
//        Thread thread = threadService.getThreadById(id);
//        return thread.getThreadName();
//    }

    @GetMapping("/{threadReference}")
    @ResponseStatus(HttpStatus.OK)
    public ThreadReference getThreadReference(@PathVariable String threadReference) {
        return threadService.getThreadReferenceByThreadReference(threadReference);
    }

    @GetMapping("workerName/{threadName}")
    @ResponseStatus(HttpStatus.OK)
    public String getWorkerName(@PathVariable String threadName) {
        return threadService.getThreadWorkerNameByThreadName(threadName);
    }

    @GetMapping("workerName2/{threadName}")
    @ResponseStatus(HttpStatus.OK)
    public String getWorkerName2(@PathVariable String threadName) {
        return threadService.getThreadWorkerNameByThreadName2(threadName);
    }

    @PostMapping("/create-thread")
    @ResponseStatus(HttpStatus.CREATED)
    public String createThread(@RequestBody CreateThreadRequest request) {
        LocalDateTime createdTimeStamp = LocalDateTime.now();
        LocalDateTime threadExpiryDate = createdTimeStamp.plusDays(30);
        ThreadReference createThreadReference = new ThreadReference();
        createThreadReference.setThreadReference(request.threadReference());
        createThreadReference.setStatus(request.status());
        createThreadReference.setCreatedTimeStamp(createdTimeStamp);
        createThreadReference.setLastUpdatedTimeStamp(createdTimeStamp);
        createThreadReference.setThreadExpiryDate(threadExpiryDate);
        createThreadReference.setAssociatedCaseReference(request.associatedCaseReference());

        return threadService.saveThreadSuccessResponse(createThreadReference);


    }
    @PutMapping("/UpdateThread/{threadReference}")
    @ResponseStatus(HttpStatus.OK)
    public String updateThread(@PathVariable String threadReference,@RequestBody CreateThreadRequest request){

        ThreadReference toUpdate = threadService.getThreadReferenceByThreadReference(threadReference);
        toUpdate.setLastUpdatedTimeStamp(LocalDateTime.now());
        //Handle Partial Update
        if(request.threadReference()!=null){
            toUpdate.setThreadReference(request.threadReference());
        }
        if(request.status()!=null){
            toUpdate.setStatus(request.status());
        }
        if(request.associatedCaseReference()!=null) {
            toUpdate.setAssociatedCaseReference(request.associatedCaseReference());
        }
        return threadService.saveThreadSuccessResponse(toUpdate)+"  updated";
    }




//
//    private static void validateCorrelationId(String correlationId) {
//        Pattern UUID_PATTERN = Pattern.compile(
//                "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$",
//                Pattern.CASE_INSENSITIVE
//        );
//        if (correlationId.isBlank() || !UUID_PATTERN.matcher(correlationId).matches()) {
//            throw new IllegalArgumentException();
//        }
//    }
}

