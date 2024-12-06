package com.isaccobertoli.routes;

import static io.javalin.apibuilder.ApiBuilder.*;

import com.isaccobertoli.controllers.UserController;
import com.isaccobertoli.middlewares.AuthMiddleware;

public class UserRoutes {
    public static void configure(UserController userController) {
        path("/users", () -> {
            before(ctx -> AuthMiddleware.verifyToken(ctx));
            path("/{userId}", () -> {
                get(ctx -> {
                    AuthMiddleware.verifyIsOwner(ctx);
                    userController.getUserById(ctx);
                });
                delete(ctx -> {
                    AuthMiddleware.verifyIsOwner(ctx);
                    userController.deleteUser(ctx);
                });
            });
        });
    }
}
