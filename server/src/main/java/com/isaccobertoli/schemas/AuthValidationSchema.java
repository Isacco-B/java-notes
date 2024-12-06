package com.isaccobertoli.schemas;

import java.util.ArrayList;
import java.util.List;

import com.isaccobertoli.dto.LoginUserDTO;
import com.isaccobertoli.dto.RegisterUserDTO;
import com.isaccobertoli.exceptions.userExceptions.UserValidationException;

public class AuthValidationSchema {

    public static void RegisterSchema(RegisterUserDTO dto) throws UserValidationException {
        List<String> errors = new ArrayList<>();

        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            errors.add("Email is required.");
        } else if (!dto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.add("Invalid email format.");
        }
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            errors.add("Password is required.");
        } else if (!dto.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%]).{8,24}$")) {
            errors.add(
                    "Your password must be between 8 and 24 characters long, and contain at least one lowercase letter, one uppercase letter, one number, and one special character (!@#$%).");
        }
        if (dto.getConfirmPassword() == null || dto.getConfirmPassword().trim().isEmpty()) {
            errors.add("Confirm password is required.");
        } else if (!dto.getConfirmPassword().equals(dto.getPassword())) {
            errors.add("Passwords do not match.");
        }

        if (!errors.isEmpty()) {
            throw new UserValidationException(String.join(";", errors));
        }
    }

    public static void LoginSchema(LoginUserDTO dto) throws UserValidationException {
        List<String> errors = new ArrayList<>();

        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            errors.add("Email is required.");
        } else if (!dto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.add("Invalid email format.");
        }
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            errors.add("Password is required.");
        }

        if (!errors.isEmpty()) {
            throw new UserValidationException(String.join(" ", errors));
        }
    }

    public static void NewPasswordSchema(RegisterUserDTO dto) throws UserValidationException {
        List<String> errors = new ArrayList<>();

        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            errors.add("Password is required.");
        } else if (!dto.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%]).{8,24}$")) {
            errors.add(
                    "Your password must be between 8 and 24 characters long, and contain at least one lowercase letter, one uppercase letter, one number, and one special character (!@#$%).");
        }
        if (dto.getConfirmPassword() == null || dto.getConfirmPassword().trim().isEmpty()) {
            errors.add("Confirm password is required.");
        } else if (!dto.getConfirmPassword().equals(dto.getPassword())) {
            errors.add("Passwords do not match.");
        }

        if (!errors.isEmpty()) {
            throw new UserValidationException(String.join(";", errors));
        }
    }
}
