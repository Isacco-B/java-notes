package com.isaccobertoli.middlewares;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import io.javalin.http.Context;
import io.javalin.http.TooManyRequestsResponse;

public class RateLimiterMiddleware {
    private static final long MAX_ATTEMPTS = 5;
    private static final long TIME_WINDOW = 60000;
    private static final Map<String, LoginAttempt> attemptsMap = new HashMap<>();

    private static class LoginAttempt {
        long attempts;
        long firstAttemptTime;

        public LoginAttempt(long attempts, long firstAttemptTime) {
            this.attempts = attempts;
            this.firstAttemptTime = firstAttemptTime;
        }
    }

    public static void rateLimitLogin(Context ctx) {
        String ipAddress = ctx.ip();
        LoginAttempt loginAttempt = attemptsMap.getOrDefault(ipAddress,
                new LoginAttempt(0, Instant.now().toEpochMilli()));

        if (Instant.now().toEpochMilli() - loginAttempt.firstAttemptTime > TIME_WINDOW) {
            loginAttempt = new LoginAttempt(0, Instant.now().toEpochMilli());
        }

        if (loginAttempt.attempts >= MAX_ATTEMPTS) {
            throw new TooManyRequestsResponse("Too many login attempts. Please try again later.");
        }
        loginAttempt.attempts++;
        attemptsMap.put(ipAddress, loginAttempt);
    };
}
