package com.isaccobertoli.utils;

import java.util.Date;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtUtil {
    private static final String SECRET = EnvUtil.getEnv("JWT_SECRET");
    private static final Algorithm algorithms = Algorithm.HMAC256(SECRET);

    public static String generateToken(String email, String userId) {
        String token = JWT.create()
                .withClaim("email", email)
                .withClaim("userId", userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + 14 * 24 * 60 * 60
                        * 1000))
                .sign(algorithms);
        return token;
    }

    public static Map<String, String> validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithms).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            String email = decodedJWT.getClaim("email").asString();
            String userId = decodedJWT.getClaim("userId").asString();

            return Map.of(
                    "email", email,
                    "userId", userId);

        } catch (JWTVerificationException e) {
            return null;
        }
    }
}
