package com.isaccobertoli.utils;

import java.util.prefs.Preferences;

public class TokenStorage {
    private static final Preferences PREFS = Preferences.userNodeForPackage(TokenStorage.class);
    private static final String TOKEN_KEY = "access_token";

    public static void saveToken(String token) {
        PREFS.put(TOKEN_KEY, token);
    }

    public static String loadToken() {
        return PREFS.get(TOKEN_KEY, null);
    }

    public static void clearToken() {
        PREFS.remove(TOKEN_KEY);
    }
}
