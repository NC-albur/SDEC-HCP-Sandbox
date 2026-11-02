package com.example.springdummyapp;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.*;

public class JwtManualSecretKeyHandling {
    Algorithm entraAlgorithm = Algorithm.HMAC256("entra");
    JWTVerifier entraVerifier = JWT.require(entraAlgorithm)
            .build();

    // MDTP + HCP example
    void mdtp_hcp() {
        var entraJWT = jwtFromEntra();
        var mdtpJWT = generateNewJWTUsingEntra(entraJWT);
        validateMDTPJWTInHCP(mdtpJWT);
    }

    // get key from MDTP vault
    Algorithm getAlgorithmFromVault() {
        return Algorithm.HMAC256("secret");
    }

    // get verifier for HCP side
    JWTVerifier getVerifier() {
        return JWT.require(getAlgorithmFromVault()).build();
    }

    // get jwt from Entra ID
    String jwtFromEntra() {
        return JWT.create()
                .withIssuer("Entra")
                .withClaim("name", "a name")
                .sign(entraAlgorithm);
    }

    String generateNewJWTUsingEntra(String entraToken) {
        try {
            // validate entra JWT
            DecodedJWT decodedEntra = entraVerifier.verify(entraToken);
            var entraClaims = decodedEntra.getClaims();

            // generate new JWT
            var jwt = JWT.create()
                    .withIssuer("self");

            // populate with claims from original jwt
            for (String key: entraClaims.keySet()) {
                // using asString for now.
                jwt.withClaim(key, entraClaims.get(key).asString());
            }

            // get key from mdtp vault
            return jwt.sign(getAlgorithmFromVault());
        } catch (JWTVerificationException e) {
            return e.getMessage();
        }
    }

    void validateMDTPJWTInHCP(String token) {
        try {
            var decoded = getVerifier().verify(token);
            var name = decoded.getClaim("name");
            System.out.println(Objects.equals(name.asString(), "a name"));
        } catch (JWTVerificationException e) {
            System.out.println(e.getMessage());
        }
    }
}
