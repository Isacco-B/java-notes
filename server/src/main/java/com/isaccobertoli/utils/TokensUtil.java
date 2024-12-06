package com.isaccobertoli.utils;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import com.isaccobertoli.models.PasswordResetTokenModel;
import com.isaccobertoli.models.VerificationTokenModel;
import com.isaccobertoli.repository.AuthRepository;

public class TokensUtil {
    public static String createVerificationToken(AuthRepository authRepository, String email) {
        Date expires = new Date(System.currentTimeMillis() + 5 * 60 * 1000);
        UUID token = UUID.randomUUID();

        Optional<VerificationTokenModel> existingToken = authRepository.findVerificationTokenByEmail(email);
        if (existingToken.isPresent()) {
            authRepository.deleteVerificationTokenById(existingToken.get().getId());
        }

        VerificationTokenModel verificationToken = authRepository.createVerificationToken(token.toString(), email,
                expires);
        return verificationToken.getToken();
    }

    public static String createPasswordResetToken(AuthRepository authRepository, String email) {
        Date expires = new Date(System.currentTimeMillis() + 5 * 60 * 1000);
        UUID token = UUID.randomUUID();

        Optional<PasswordResetTokenModel> existingToken = authRepository.findPasswordResetTokenByEamil(email);
        if (existingToken.isPresent()) {
            authRepository.deletePasswordResetTokenById(existingToken.get().getId());
        }

        PasswordResetTokenModel passwordResetToken = authRepository.createPasswordResetToken(token.toString(), email,
                expires);
        return passwordResetToken.getToken();
    }
}
