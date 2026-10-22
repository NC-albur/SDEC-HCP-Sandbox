package com.example.springdummyapp;

import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;


@SpringBootApplication
public class SpringDummyAppApplication {

    static void main(String[] args) {
        // ApplicationContext ctx = SpringApplication.run(SpringDummyAppApplication.class, args);
        var jwt = setup();
        decoder(jwt);
    }

    static void jwtWork() {
        JwtSample sample = new JwtSample();

        // generate token
        String token = sample.createToken();
        System.out.println(token);

        // verifier
        var verifier = sample.createVerifier();
        System.out.println(verifier.toString());

        // decoded token
        DecodedJWT decodedJWT = sample.decodeToken(token);

        // parsing decoded token
        var header = decodedJWT.getHeader();
        var payload = decodedJWT.getPayload();
        var signature = decodedJWT.getSignature();

        System.out.println(header);
        System.out.println(payload);
        System.out.println(signature);
    }

    static void jwtWithBase64() {
        var sample = new JwtWithBase64();
        sample.showcase();
        sample.verification();
    }

    static String setup() {

        var secretKey = Keys.hmacShaKeyFor("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=".getBytes());
        String jws = Jwts.builder().issuer("job").subject("person")
                .claim("name", "A Name")
                .claim("role", "admin")
                .claim("password", "abcde")
                .issuedAt(Date.from(Instant.ofEpochSecond(1466796822L)))
                .expiration(Date.from(Instant.ofEpochSecond(4622470422L)))
                .signWith(
                        secretKey,
                        Jwts.SIG.HS256
                )
                .compact();

        System.out.println(jws);
        return jws;
    }

    static void decoder(String token) {
        // split into parts
        String[] chunks = token.split("\\.");
        // use Base64 Decoder
        Base64.Decoder decoder = Base64.getUrlDecoder();
        // decode
        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));
        System.out.println(header);
        System.out.println(payload);
        if (chunks.length == 3) {
            String signature = chunks[2];
            System.out.println(signature);
        }
    }
}