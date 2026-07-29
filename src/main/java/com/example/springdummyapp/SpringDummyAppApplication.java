package com.example.springdummyapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class SpringDummyAppApplication {

    static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(SpringDummyAppApplication.class, args);
    }
}
