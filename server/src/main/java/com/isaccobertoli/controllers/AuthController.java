package com.isaccobertoli.controllers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.isaccobertoli.dto.LoginUserDTO;
import com.isaccobertoli.dto.RegisterUserDTO;
import com.isaccobertoli.exceptions.tokenExceptions.InvalidTokenException;
import com.isaccobertoli.exceptions.tokenExceptions.TokenNonFoundException;
import com.isaccobertoli.exceptions.userExceptions.UnverifiedUserException;
import com.isaccobertoli.exceptions.userExceptions.UserAlreadyExistException;
import com.isaccobertoli.exceptions.userExceptions.UserNotFoundException;
import com.isaccobertoli.exceptions.userExceptions.UserValidationException;
import com.isaccobertoli.schemas.AuthValidationSchema;
import com.isaccobertoli.services.AuthService;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;

public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public void register(Context ctx) {
        try {
            RegisterUserDTO dto = ctx.bodyAsClass(RegisterUserDTO.class);
            AuthValidationSchema.RegisterSchema(dto);
            authService.register(dto);
            Map<String, Object> response = Map.of(
                    "status", "success",
                    "message", "We've sent you an email to verify your account, please check your inbox");
            ctx.json(response);
        } catch (UserValidationException e) {
            Map<String, Object> response = Map.of(
                    "status", "error",
                    "message", Map.of("error", e.getMessage().split(";")),
                    "code", 400);
            ctx.json(response).status(400);
        } catch (UserAlreadyExistException e) {
            throw new BadRequestResponse(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorResponse(e.getMessage());
        }
    }

    public void login(Context ctx) {
        try {
            LoginUserDTO dto = ctx.bodyAsClass(LoginUserDTO.class);
            AuthValidationSchema.LoginSchema(dto);
            String accessToken = authService.login(dto);
            Map<String, Object> response = Map.of(
                    "status", "success",
                    "message", accessToken);
            ctx.json(response);
        } catch (UserValidationException | UserNotFoundException | UnverifiedUserException e) {
            throw new BadRequestResponse(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorResponse(e.getMessage());
        }
    }

    public void newVerification(Context ctx) {
        try {
            String token = ctx.pathParam("token");
            authService.newVerification(token);
            ctx.redirect(
                    "/templates/success_page.html?title=Verification success!&message=You can now login into your account.");
        } catch (TokenNonFoundException | InvalidTokenException | UserNotFoundException e) {
            ctx.redirect("/templates/error_page.html?error="
                    + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8) + "&title=Verification error");
        } catch (Exception e) {
            ctx.redirect("/templates/error_page.html?error="
                    + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8) + "&title=Verification error");
        }
    }

    public void resetPasswordRequest(Context ctx) {
        try {
            String email = ctx.pathParam("email");
            authService.resetPasswordRequest(email);
            Map<String, Object> response = Map.of(
                    "status", "success",
                    "message", "We've sent you an email to reset your password, please check your inbox");
            ctx.json(response);
        } catch (UserValidationException | UserNotFoundException e) {
            throw new BadRequestResponse(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorResponse(e.getMessage());
        }
    }

    public void resetPasswordConfirm(Context ctx) {
        try {
            String token = ctx.pathParam("token");
            authService.resetPasswordConfirm(token);
            ctx.redirect("/templates/new_password.html?token=" + token);
        } catch (TokenNonFoundException | InvalidTokenException e) {
            ctx.redirect("/templates/error_page.html?error="
                    + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8) + "&title=Something went wrong");
        } catch (Exception e) {
            ctx.redirect("/templates/error_page.html?error="
                    + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8) + "&title=Something went wrong");
        }
    }

    public void newPassword(Context ctx) {
        try {
            String token = ctx.pathParam("token");
            RegisterUserDTO dto = ctx.bodyAsClass(RegisterUserDTO.class);
            AuthValidationSchema.NewPasswordSchema(dto);
            authService.newPassword(token, dto.getPassword());
            Map<String, Object> response = Map.of(
                    "status", "success",
                    "message", "Password changed successfully, you can now login into your account");
            ctx.json(response);
        } catch (TokenNonFoundException | InvalidTokenException | UserNotFoundException e) {
            throw new BadRequestResponse(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerErrorResponse(e.getMessage());
        }
    }
}
