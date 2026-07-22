package com.example.springdummyapp.controller;

import com.example.springdummyapp.model.dao.Thread;
import com.example.springdummyapp.model.request.CreateThreadRequest;
import com.example.springdummyapp.service.ThreadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "v1/dummy")
@RequiredArgsConstructor
public class DummyController {

    private final ThreadService threadService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String dummyGet(@PathVariable Long id) {
        Thread thread = threadService.getThreadById(id);
        return thread.getThreadName();
    }

    @GetMapping("workerName/{threadName}")
    @ResponseStatus(HttpStatus.OK)
    public String getWorkerName(@PathVariable String threadName) {
        return threadService.getThreadWorkerNameByThreadName(threadName);
    }

    @GetMapping("workerName2/{threadName}")
    @ResponseStatus(HttpStatus.OK)
    public String getWorkerName2(@PathVariable String threadName) {
        return threadService.getThreadWorkerNameByThreadName(threadName);
    }

    @PostMapping("/create-thread")
    @ResponseStatus(HttpStatus.CREATED)
    public String createThread(@RequestBody CreateThreadRequest request) {

        return "success!";
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

