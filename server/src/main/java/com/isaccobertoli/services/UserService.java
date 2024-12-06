package com.isaccobertoli.services;

import com.isaccobertoli.exceptions.userExceptions.UserNotFoundException;
import com.isaccobertoli.exceptions.userExceptions.UserValidationException;
import com.isaccobertoli.models.UserModel;
import com.isaccobertoli.repository.UserRepository;
import com.mongodb.client.result.DeleteResult;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserModel getUserById(String id) throws UserNotFoundException, UserValidationException {
        if (id == null || id.trim().isEmpty()) {
            throw new UserValidationException("User ID is required");
        }
        UserModel user = userRepository.getById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        return user;
    }

    public UserModel getUserByEmail(String email) throws UserNotFoundException, UserValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw new UserValidationException("Email is required");
        }
        UserModel user = userRepository.getByEmail(
                email).orElseThrow(() -> new UserNotFoundException("User not found"));
        return user;
    }

    public void deleteUser(String id) throws UserValidationException, UserNotFoundException {
        if (id == null || id.trim().isEmpty()) {
            throw new UserValidationException("User ID is required");
        }
        DeleteResult result = userRepository.delete(id);

        if (result.getDeletedCount() == 0) {
            throw new UserNotFoundException("User not found");
        }
    }
}
