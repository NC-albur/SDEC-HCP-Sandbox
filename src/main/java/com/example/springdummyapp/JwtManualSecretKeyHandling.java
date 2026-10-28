package com.example.springdummyapp;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtManualSecretKeyHandling {
    Algorithm algorithm = Algorithm.HMAC256("secret");
    JWTVerifier verifier = JWT.require(algorithm)
            .withIssuer("my name")
            .build();

    void JWTTest() {
        String id = verifyJWT(createJWT());
        System.out.println(id);
    }

    String createJWT() {
        return JWT.create()
                .withIssuer("my name")
                .withSubject("a subject")
                .withClaim("id", "1234")
                .withIssuedAt(new Date())
                .sign(algorithm);
    }

    String verifyJWT(String token) {
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            Claim idClaim = decodedJWT.getClaim("id");
            return idClaim.asString();
        } catch (JWTVerificationException e) {
            return e.getMessage();
        }
    }


/*
    void showcase() {
        validator_secretKeyInHeader(setup());
    }

    String setup() {
        String secret = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

        // header with secret key included
        Map<String, String> header = new HashMap<>();
        header.put("alg","HS256");
        header.put("typ","JWT");
        header.put("secretKey", secret);

        // payload
        Map<String, String> payload = new HashMap<>();
        payload.put("user", "John");
        payload.put("password", "abcde");
        payload.put("message", "hello world");

        // set and return jwt
        return Jwts.builder()
                // add header
                .header().empty().add(header).and()
                // add payload
                .claims(payload)
                // sign with secret key
                .signWith(
                        Keys.hmacShaKeyFor(secret.getBytes()),
                        Jwts.SIG.HS256
                )
                .compact();
    }

    static void validator_secretKeyInHeader(String token) {
        var head = Jwts.parser()
                .build()
                .parseSignedClaims(token)
                .getHeader();

        String secretKey = (String) head.get("secretKey");

        // decode payload
        Claims payload = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        var message = payload.get("message");
        System.out.println(message != null ? (String) message : "no message found");

    }
*/

}
