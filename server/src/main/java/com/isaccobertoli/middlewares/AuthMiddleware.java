package com.isaccobertoli.middlewares;

import java.util.Map;

import com.isaccobertoli.utils.JwtUtil;

import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;

public class AuthMiddleware {

    public static void verifyToken(Context ctx) {
        String authHeader = ctx.header("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedResponse("You are not authorized");
        }

        String token = authHeader.replace("Bearer ", "");
        if (token == null || token.trim().isEmpty()) {
            throw new UnauthorizedResponse("You are not authorized");
        }

        Map<String, String> decodedToken = JwtUtil.validateToken(token);
        if (decodedToken == null) {
            throw new UnauthorizedResponse("You are not authorized");
        }

        ctx.attribute("email", decodedToken.get("email"));
        ctx.attribute("userId", decodedToken.get("userId"));
    };

    public static void verifyIsOwner(Context ctx) {
        String tokenUserId = ctx.attribute("userId");
        String userId = ctx.pathParam("userId");

        if (tokenUserId == null) {
            throw new ForbiddenResponse("You are not allowed");
        }

        if (!tokenUserId.equals(userId)) {
            throw new ForbiddenResponse("You are not allowed");
        }
    }
}
