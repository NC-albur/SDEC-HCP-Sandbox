package com.example.springdummyapp;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import com.nimbusds.jose.*;
import com.nimbusds.jose.jwk.source.*;
import com.nimbusds.jose.proc.*;
import com.nimbusds.jwt.*;
import com.nimbusds.jwt.proc.*;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


@SpringBootApplication
public class SpringDummyAppApplication {

    static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(SpringDummyAppApplication.class, args);

        Environment env = ctx.getEnvironment();

        try {
            testJwt(
                    env.getRequiredProperty("jwt.key-source"),
                    env.getRequiredProperty("jwt.issuer"),
                    env.getRequiredProperty("jwt.access-token"));
        } catch (MalformedURLException | URISyntaxException e){
            throw new IllegalStateException("Invalid JWKS URL", e);
        }
    }

    static void testJwt(String keySourceUrl, String issuer, String accessToken)
            throws MalformedURLException, URISyntaxException {

        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();

        jwtProcessor.setJWSTypeVerifier(
                new DefaultJOSEObjectTypeVerifier<>(new JOSEObjectType("JWT")));

        JWKSource<SecurityContext> keySource = JWKSourceBuilder
                .create(new URI(keySourceUrl).toURL())
                .retrying(true)
                .build();

        JWSAlgorithm expectedJWSAlg = JWSAlgorithm.RS256;

        JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(
                expectedJWSAlg,
                keySource);
        jwtProcessor.setJWSKeySelector(keySelector);

        jwtProcessor.setJWTClaimsSetVerifier(new DefaultJWTClaimsVerifier<>(
                new JWTClaimsSet.Builder().issuer(issuer).build(),
                new HashSet<>(Arrays.asList(
                        JWTClaimNames.SUBJECT,
                        JWTClaimNames.ISSUED_AT,
                        JWTClaimNames.EXPIRATION_TIME,
                        JWTClaimNames.AUDIENCE))));

        try {
            SignedJWT jwt = SignedJWT.parse(accessToken);

            System.out.println(jwt.getHeader().toJSONObject());
            JWTClaimsSet claims = jwt.getJWTClaimsSet();
            System.out.println(claims.toJSONObject());

            String kid = jwt.getHeader().getKeyID();

            List<JWK> matches = keySource.get(
                    new JWKSelector(new JWKMatcher.Builder().keyID(kid).build()),
                    null);

            System.out.println(matches.isEmpty()
                    ? "kid " + kid + " NOT in Entra's JWKS"
                    : "kid " + kid + " found: " + matches.getFirst().toJSONString());

            System.out.println(keySource);
        } catch (ParseException | KeySourceException e) {
            System.err.println(e.getMessage());
            return;
        }

        // Note for review: once we have actual data, we can use 'jwtProcessor.process(accessToken, ctx);' which
        // checks the access token for its signature too, but for this test it will just fail

        System.out.println("done");
    }
}
