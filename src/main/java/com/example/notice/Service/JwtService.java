package com.example.notice.Service;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

@Service
public class JwtService {

    private final Dotenv dotenv = Dotenv.load();
    private final String SECRET_KEY = dotenv.get("SECRET_KEY");


    public String generateToken(String username) {

        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());


        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(keySpec)
                .compact();
    }
}
