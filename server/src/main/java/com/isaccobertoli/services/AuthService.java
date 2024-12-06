package com.isaccobertoli.services;

import java.util.Date;

import org.apache.commons.mail.EmailException;

import com.isaccobertoli.dto.LoginUserDTO;
import com.isaccobertoli.dto.RegisterUserDTO;
import com.isaccobertoli.exceptions.tokenExceptions.InvalidTokenException;
import com.isaccobertoli.exceptions.tokenExceptions.TokenNonFoundException;
import com.isaccobertoli.exceptions.userExceptions.UnverifiedUserException;
import com.isaccobertoli.exceptions.userExceptions.UserAlreadyExistException;
import com.isaccobertoli.exceptions.userExceptions.UserNotFoundException;
import com.isaccobertoli.exceptions.userExceptions.UserValidationException;
import com.isaccobertoli.models.PasswordResetTokenModel;
import com.isaccobertoli.models.UserModel;
import com.isaccobertoli.models.VerificationTokenModel;
import com.isaccobertoli.repository.AuthRepository;
import com.isaccobertoli.repository.UserRepository;
import com.isaccobertoli.utils.EmailUtil;
import com.isaccobertoli.utils.JwtUtil;
import com.isaccobertoli.utils.PasswordUtils;
import com.isaccobertoli.utils.TokensUtil;
import com.mongodb.MongoException;

public class AuthService {
    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    public AuthService(AuthRepository authRepository, UserRepository userRepository) {
        this.authRepository = authRepository;
        this.userRepository = userRepository;
    }

    public void register(RegisterUserDTO dto)
            throws UserValidationException, UserAlreadyExistException, EmailException, MongoException {

        String normalizedEmail = dto.getEmail().toLowerCase();

        if (userRepository.getByEmail(normalizedEmail).isPresent()) {
            throw new UserAlreadyExistException("Account already exists.");
        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new UserValidationException("Passwords do not match.");
        }

        byte[] salt = PasswordUtils.generateSalt();
        String hashPassword = PasswordUtils.hashPassword(dto.getPassword(), salt);

        UserModel user = new UserModel(dto.getEmail(), hashPassword);

        userRepository.save(user);

        try {
            String token = TokensUtil.createVerificationToken(authRepository, normalizedEmail);
            EmailUtil.sendVerificationEmail(normalizedEmail, token);
        } catch (EmailException e) {
            System.err.println("Failed to send verification email: " + e.getMessage());
        }
    }

    public String login(LoginUserDTO dto)
            throws UserValidationException, UserNotFoundException,
            UnverifiedUserException {

        UserModel user = userRepository.getByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Invalid email or password."));

        if (!PasswordUtils.verifyPassword(dto.getPassword(), user.getPassword())) {
            throw new UserValidationException("Invalid email or password.");
        }

        if (user.getAccountVerified() == null) {
            try {
                String token = TokensUtil.createVerificationToken(authRepository, user.getEmail());
                EmailUtil.sendVerificationEmail(user.getEmail(), token);
            } catch (EmailException e) {
                System.err.println("Failed to send verification email: " + e.getMessage());
            }

            throw new UnverifiedUserException(
                    "Your account is not verified. Please check your email.");
        }

        String accessToken = JwtUtil.generateToken(user.getEmail(),
                user.getId().toString());
        return accessToken;
    }

    public void newVerification(String token) throws TokenNonFoundException, InvalidTokenException,
            UserNotFoundException {
        VerificationTokenModel verificationToken = authRepository.findVerificationTokenByToken(token)
                .orElseThrow(() -> new TokenNonFoundException("Invalid token"));

        if (verificationToken.getExpiresAt().before(new Date())) {
            authRepository.deleteVerificationTokenById(verificationToken.getId());
            throw new InvalidTokenException("Token has expired!");
        }

        UserModel user = userRepository.getByEmail(verificationToken.getEmail())
                .orElseThrow(() -> new UserNotFoundException(
                        "Couldn't find your account"));

        if (user.getAccountVerified() != null) {
            throw new InvalidTokenException("Your account is already verified");
        }

        userRepository.setVerified(user.getUserId());
        authRepository.deleteVerificationTokenById(verificationToken.getId());
    }

    public void resetPasswordRequest(String email) throws UserNotFoundException, UserValidationException {
        String normalizedEmail = email.toLowerCase();

        if (normalizedEmail == null || normalizedEmail.trim().isEmpty()) {
            throw new UserValidationException("Email is required");
        }

        if (!userRepository.getByEmail(normalizedEmail).isPresent()) {
            throw new UserNotFoundException("Email does not exist!");
        }

        try {
            String token = TokensUtil.createPasswordResetToken(authRepository, normalizedEmail);
            EmailUtil.sendPasswordResetEmail(normalizedEmail, token);
        } catch (EmailException e) {
            System.err.println("Failed to send password reset email: " + e.getMessage());
        }
    }

    public void resetPasswordConfirm(String token) throws TokenNonFoundException, InvalidTokenException {
        if (token == null || token.trim().isEmpty()) {
            throw new TokenNonFoundException("Invalid token");
        }

        PasswordResetTokenModel passwordResetToken = authRepository.findPasswordResetTokenByToken(token)
                .orElseThrow(() -> new TokenNonFoundException("Invalid token"));

        if (passwordResetToken.getExpiresAt().before(new Date())) {
            authRepository.deleteVerificationTokenById(passwordResetToken.getId());
            throw new InvalidTokenException("Token has expired!");
        }
    }

    public void newPassword(String token, String newPassword) throws TokenNonFoundException, InvalidTokenException,
            UserNotFoundException {
        if (token == null || token.trim().isEmpty()) {
            throw new TokenNonFoundException("Invalid token");
        }

        PasswordResetTokenModel passwordResetToken = authRepository.findPasswordResetTokenByToken(token)
                .orElseThrow(() -> new TokenNonFoundException("Invalid token"));

        if (passwordResetToken.getExpiresAt().before(new Date())) {
            authRepository.deleteVerificationTokenById(passwordResetToken.getId());
            throw new InvalidTokenException("Token has expired!");
        }

        UserModel validUser = userRepository.getByEmail(passwordResetToken.getEmail())
                .orElseThrow(() -> new UserNotFoundException(
                        "Couldn't find your account"));

        byte[] salt = PasswordUtils.generateSalt();
        String hashPassword = PasswordUtils.hashPassword(newPassword, salt);

        authRepository.deletePasswordResetTokenById(passwordResetToken.getId());
        userRepository.changePassword(validUser.getUserId(), hashPassword);
    }
}
