package com.isaccobertoli.repository;

import java.util.Date;
import java.util.Optional;

import org.bson.types.ObjectId;

import com.isaccobertoli.models.UserModel;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;

public class UserRepository {
    private final MongoCollection<UserModel> userCollection;

    public UserRepository(MongoDatabase database) {
        userCollection = database.getCollection("users", UserModel.class);
        userCollection.createIndex(Indexes.ascending("email"),
                new IndexOptions().unique(true));
    }

    public UserModel save(UserModel user) {
        userCollection.insertOne(user);
        return user;
    }

    public Optional<UserModel> getById(String userId) {
        ObjectId id = new ObjectId(userId);
        return Optional.ofNullable(userCollection.find(Filters.eq("_id", id)).first());
    }

    public Optional<UserModel> getByEmail(String email) {
        return Optional.ofNullable(userCollection.find(Filters.eq("email", email)).first());
    }

    public void changePassword(String userId, String password) {
        ObjectId id = new ObjectId(userId);
        userCollection.updateOne(
                Filters.eq("_id", id),
                Updates.combine(
                        Updates.set("password", password),
                        Updates.set("updated_at", new Date())));
    }

    public void setVerified(String userId) {
        ObjectId id = new ObjectId(userId);
        userCollection.updateOne(
                Filters.eq("_id", id),
                Updates.combine(
                        Updates.set("account_verified", new Date()),
                        Updates.set("updated_at", new Date())));
    }

    public DeleteResult delete(String userId) {
        ObjectId id = new ObjectId(userId);
        DeleteResult result = userCollection.deleteOne(Filters.eq("_id", id));
        return result;
    }
}
