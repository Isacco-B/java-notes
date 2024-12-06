package com.isaccobertoli.routes;

import static io.javalin.apibuilder.ApiBuilder.*;

import com.isaccobertoli.controllers.AuthController;
import com.isaccobertoli.middlewares.RateLimiterMiddleware;

public class AuthRoutes {

    public static void configure(AuthController authController) {
        path("/auth", () -> {
            before(ctx -> RateLimiterMiddleware.rateLimitLogin(ctx));
            path("/sign-up", () -> {
                post(authController::register);
            });
            path("/sign-in", () -> {
                post(authController::login);
            });
            path("/new-verification/{token}", () -> {
                get(authController::newVerification);
            });
            path("/reset-password/{email}", () -> {
                post(authController::resetPasswordRequest);
            });
            path("/reset-password-confirm/{token}", () -> {
                get(authController::resetPasswordConfirm);
            });
            path("/new-password/{token}", () -> {
                post(authController::newPassword);
            });
        });
    }
}
