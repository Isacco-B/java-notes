package com.isaccobertoli.repository;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.bson.types.ObjectId;

import com.isaccobertoli.models.PasswordResetTokenModel;
import com.isaccobertoli.models.VerificationTokenModel;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;

public class AuthRepository {
    private final MongoCollection<PasswordResetTokenModel> passwordResetTokenCollection;
    private final MongoCollection<VerificationTokenModel> verificationTokenCollection;

    public AuthRepository(MongoDatabase database) {
        this.passwordResetTokenCollection = database.getCollection("password_resets_tokens",
                PasswordResetTokenModel.class);
        this.passwordResetTokenCollection.createIndex(
                Indexes.ascending("created_at"),
                new IndexOptions()
                        .expireAfter(10L, TimeUnit.MINUTES));

        this.verificationTokenCollection = database.getCollection("verification_tokens",
                VerificationTokenModel.class);
        this.verificationTokenCollection.createIndex(
                Indexes.ascending("created_at"),
                new IndexOptions()
                        .expireAfter(10L, TimeUnit.MINUTES));
    }

    public PasswordResetTokenModel createPasswordResetToken(String token, String email, Date expires) {
        PasswordResetTokenModel passwordResetToken = new PasswordResetTokenModel(token, email, expires);
        passwordResetTokenCollection.insertOne(passwordResetToken);
        return passwordResetToken;
    }

    public Optional<PasswordResetTokenModel> findPasswordResetTokenByEamil(String email) {
        return Optional.ofNullable(passwordResetTokenCollection.find(Filters.eq("email", email)).first());
    }

    public Optional<PasswordResetTokenModel> findPasswordResetTokenByToken(String token) {
        return Optional.ofNullable(passwordResetTokenCollection.find(Filters.eq("token", token)).first());
    }

    public void deletePasswordResetTokenById(ObjectId id) {
        passwordResetTokenCollection.deleteOne(Filters.eq("_id", id));
    }

    public VerificationTokenModel createVerificationToken(String token, String email, Date expires) {
        VerificationTokenModel verificationToken = new VerificationTokenModel(token.toString(), email, expires);
        verificationTokenCollection.insertOne(verificationToken);
        return verificationToken;
    }

    public Optional<VerificationTokenModel> findVerificationTokenByEmail(String email) {
        return Optional.ofNullable(verificationTokenCollection.find(Filters.eq("email", email)).first());
    }

    public Optional<VerificationTokenModel> findVerificationTokenByToken(String token) {
        return Optional.ofNullable(verificationTokenCollection.find(Filters.eq("token", token)).first());
    }

    public void deleteVerificationTokenById(ObjectId id) {
        verificationTokenCollection.deleteOne(Filters.eq("_id", id));
    }

}
