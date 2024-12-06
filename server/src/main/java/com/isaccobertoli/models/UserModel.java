package com.isaccobertoli.models;

import java.util.Date;

import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserModel {
    @JsonIgnore
    private ObjectId id;

    private String email;

    @JsonIgnore
    private String password;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @BsonProperty(value = "account_verified")
    private Date accountVerified;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @BsonProperty(value = "created_at")
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @BsonProperty(value = "updated_at")
    private Date updatedAt;

    public UserModel() {
    }

    public UserModel(String email, String password) {
        this.email = email;
        this.password = password;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    @BsonIgnore
    public String getUserId() {
        return id != null ? id.toHexString() : null;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getAccountVerified() {
        return accountVerified;
    }

    public void setAccountVerified(Date accountVerified) {
        this.accountVerified = accountVerified;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", accountVerified=" + accountVerified +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
