package com.isaccobertoli.utils;

import java.util.Base64;
import java.util.HashMap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TokenUtils {

    public static HashMap<String, String> readTokenPayload() {
        try {

            String token = TokenStorage.loadToken();
            if (token != null) {
                String[] parts = token.split("\\.");
                if (parts.length != 3) {
                    throw new IllegalArgumentException("Invalid token format");
                }

                String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));

                JsonElement jsonElement = new JsonParser().parse(payloadJson);
                JsonObject payload = jsonElement.getAsJsonObject();

                String email = payload.get("email").getAsString();
                String userId = payload.get("userId").getAsString();

                HashMap<String, String> userInfo = new HashMap<>();
                userInfo.put("email", email);
                userInfo.put("userId", userId);

                return userInfo;
            }
        } catch (Exception e) {
            System.out.println("Error reading token payload: " + e.getMessage());
        }
        return null;
    }
}
