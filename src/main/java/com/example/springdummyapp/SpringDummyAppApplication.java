package com.example.springdummyapp;

import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SpringDummyAppApplication {

    static void main(String[] args) {
        var keyHandling = new JwtManualSecretKeyHandling();
        keyHandling.JWTTest();
    }
}