package com.isaccobertoli.models;

import java.util.Date;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class PasswordResetTokenModel {
    private ObjectId id;
    private String email;
    private String token;
    @BsonProperty(value = "created_at")
    private Date createdAt;
    @BsonProperty(value = "expires_at")
    private Date expiresAt;

    public PasswordResetTokenModel() {
    }

    public PasswordResetTokenModel(String token, String email, Date expiresAt) {
        this.email = email;
        this.token = token;
        this.createdAt = new Date();
        this.expiresAt = expiresAt;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }
}
