package com.chat_app.Chat_App.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.util.Date;

public class jwtProvider {


    private static SecretKey SECRET_KEY = Keys.hmacShaKeyFor(jwtConstant.SECRET_KEY.getBytes());

    public static String generateToken(Authentication auth) {
        String jwt = Jwts.builder().issuer("ChatApp")
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + 86400000))
                .claim("email", auth.getName())
                .signWith(SECRET_KEY)
                .compact();
        return jwt;
    }

//    public static String getEmailFromJwtToken(String jwt) {
//        // token generated is in the form of (barer token) extract token from this
//        jwt = jwt.substring(7);
//        System.out.println("token: " + jwt);
//        Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseUnsecuredClaims(jwt).getPayload();
//
//        String email = String.valueOf(claims.get("email"));
//        System.out.println("email in jwtProvider: " + email);
//        return email;
//    }

    public static String getEmailFromJwtToken(String jwt) {
        // Ensure the token starts with "Bearer " before removing it
        if (jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
        }

        JwtParser parser = Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build();

        Claims claims = parser.parseSignedClaims(jwt).getPayload();

        String email = claims.get("email", String.class);
        return email;
    }
}
