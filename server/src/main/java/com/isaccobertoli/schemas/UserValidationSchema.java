package com.isaccobertoli.schemas;

import java.util.ArrayList;
import java.util.List;

import com.isaccobertoli.models.UserModel;

public class UserValidationSchema {
    public static List<String> UpdateSchema(UserModel userModel) {
        List<String> validationErrors = new ArrayList<>();

        if (userModel.getEmail() == null || userModel.getEmail().trim().isEmpty()) {
            validationErrors.add("Email is required");
        } else {
            if (!userModel.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                validationErrors.add("Invalid email format");
            }
        }
        return validationErrors;
    }
}
