package com.example.springdummyapp;

import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SpringDummyAppApplication {

    private static final JwtManualSecretKeyHandling keyHandling = new JwtManualSecretKeyHandling();

    static void main(String[] args) {
        verifyTokenUsingIssuerExample();
    }

    static void verifyTokenUsingIssuerExample() {
        var v = new ValidateJWTWithIssuerPrototype();
        v.demonstrate();
    }

    static void mockMDTPAndHCPExample() {
        keyHandling.mdtp_hcp();
    }

    static void basicExample() {
        keyHandling.JWTTest();
    }
}