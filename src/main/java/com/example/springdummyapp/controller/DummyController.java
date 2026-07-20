package com.example.springdummyapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "v1/dummy")
@RequiredArgsConstructor
public class DummyController {

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String dummyGet(@RequestHeader(name = "correlationId") String correlationId) {
//        validateCorrelationId(correlationId);
        return "Success!";
    }

    private static void validateCorrelationId(String correlationId) {
        Pattern UUID_PATTERN = Pattern.compile(
                "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$",
                Pattern.CASE_INSENSITIVE
        );
        if (correlationId.isBlank() || !UUID_PATTERN.matcher(correlationId).matches()) {
            throw new IllegalArgumentException();
        }
    }
}

