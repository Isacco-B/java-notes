package com.isaccobertoli.controllers;

import java.util.Map;

import com.isaccobertoli.exceptions.userExceptions.UserNotFoundException;
import com.isaccobertoli.exceptions.userExceptions.UserValidationException;
import com.isaccobertoli.models.UserModel;
import com.isaccobertoli.services.UserService;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;
import io.javalin.http.NotFoundResponse;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void getUserById(Context ctx) {
        try {
            String userId = ctx.pathParam("userId");
            UserModel user = userService.getUserById(userId);
            Map<String, Object> response = Map.of(
                    "status", "success",
                    "data", Map.of("user", user));
            ctx.json(response);
        } catch (UserValidationException e) {
            throw new BadRequestResponse(e.getMessage());
        } catch (UserNotFoundException e) {
            throw new NotFoundResponse(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorResponse(e.getMessage());
        }
    }

    public void deleteUser(Context ctx) {
        try {
            String userId = ctx.pathParam("userId");
            userService.deleteUser(userId);
            Map<String, Object> response = Map.of(
                    "status", "success",
                    "message", "User deleted successfully");
            ctx.json(response);
        } catch (UserValidationException e) {
            throw new BadRequestResponse(e.getMessage());
        } catch (UserNotFoundException e) {
            throw new NotFoundResponse(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorResponse(e.getMessage());
        }
    }
}
