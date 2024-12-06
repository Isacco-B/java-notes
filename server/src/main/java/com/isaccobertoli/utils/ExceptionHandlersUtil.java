package com.isaccobertoli.utils;

import io.javalin.http.*;
import io.javalin.Javalin;

import java.util.Map;

public class ExceptionHandlersUtil {

    public static void register(Javalin app) {
        app.exception(BadRequestResponse.class, (e, ctx) -> handleException(ctx, "error", e.getMessage(), 400));
        app.exception(UnauthorizedResponse.class, (e, ctx) -> handleException(ctx, "error", e.getMessage(), 401));
        app.exception(ForbiddenResponse.class, (e, ctx) -> handleException(ctx, "error", e.getMessage(), 403));
        app.exception(NotFoundResponse.class, (e, ctx) -> handleException(ctx, "error", e.getMessage(), 404));
        app.exception(TooManyRequestsResponse.class, (e, ctx) -> handleException(ctx, "error", e.getMessage(), 429));
        app.exception(InternalServerErrorResponse.class,
                (e, ctx) -> handleException(ctx, "error", e.getMessage(), 500));
        app.exception(Exception.class, (e, ctx) -> handleException(ctx, "error", "An unexpected error occurred.", 500));
    }

    private static void handleException(Context ctx, String status, String message, int code) {
        ctx.status(code).json(Map.of(
                "status", status,
                "message", message,
                "code", code));
    }
}
