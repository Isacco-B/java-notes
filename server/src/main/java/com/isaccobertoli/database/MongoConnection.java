package com.isaccobertoli.database;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.isaccobertoli.utils.EnvUtil;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {
    private static CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    private static MongoClient mongoClient;
    private static MongoDatabase database;

    public static void initialize() {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(EnvUtil.getEnv("DB_HOST")))
                .codecRegistry(pojoCodecRegistry)
                .build();

        mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase(EnvUtil.getEnv("DB_NAME"));
    }

    public static boolean testConnection() {
        try {
            initialize();
            database.listCollections().first();
            return true;
        } catch (Exception e) {
            System.err.println("Database connection failed");
            return false;
        }
    }

    public static MongoDatabase getDatabase() {
        if (database == null) {
            throw new IllegalStateException("Database connection has not been initialized.");
        }
        return database;
    }

    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
