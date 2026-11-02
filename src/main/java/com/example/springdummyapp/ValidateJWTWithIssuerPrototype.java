package com.example.springdummyapp;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ValidateJWTWithIssuerPrototype {
    void demonstrate() {
        String token = jwtFromEntra();
        String issuer = decodeJWTOnly(token);
        try {
            Algorithm key = getPublicKey(issuer);
            boolean result = verifyToken(token, key);
            System.out.println(result ? "Success!" : "Failure!");
        } catch (FileNotFoundException e) {
            System.out.println("Key Not Found!");
        }

    }

    Algorithm entraAlgorithm = Algorithm.HMAC256("entra");

    // sample jwt from EntraID
    String jwtFromEntra() {
        return JWT.create()
                .withIssuer("Entra")
                .withClaim("name", "a name")
                .sign(entraAlgorithm);
    }

    // 1. extract issuer link from token
    String decodeJWTOnly(String token) {
        String[] chunks = token.split("\\.");

        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));

        // serialize payload to dict
        var gson = new Gson();
        Map<String, String> payloadMap = gson.fromJson(payload, new TypeToken<HashMap<String, String>>() {}.getType());

        // get issuer
        return payloadMap.get("iss");
    }

    // 2. get public key using issuer
    Algorithm getPublicKey(String issuer) throws FileNotFoundException {
        // this would be an API call in practice
        if (issuer.equals("Entra")) return entraAlgorithm;
        else throw new FileNotFoundException();
    }

    // 3. use public key to verify token
    boolean verifyToken(String token, Algorithm key) {
        JWTVerifier verifier = JWT.require(key)
                .build();
        try {
            DecodedJWT decoded = verifier.verify(token);
            String issuer = decoded.getIssuer();
            String output = String.format("Issuer is %s.", issuer);
            System.out.println(output);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }
}
