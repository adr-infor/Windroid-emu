package com.micewine.emu.steam;

import android.content.Context;
import android.content.SharedPreferences;

public class SteamPrefs {
    private static final String PREFS_NAME = "steam_prefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_STEAM_ID = "steam_id";
    private static final String KEY_LOGIN_METHOD = "login_method"; // "credential" or "qr"
    private static final String KEY_AUTO_LOGIN = "auto_login";

    private final SharedPreferences prefs;

    public SteamPrefs(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setUsername(String username) {
        prefs.edit().putString(KEY_USERNAME, username).apply();
    }

    public String getUsername() {
        return prefs.getString(KEY_USERNAME, null);
    }

    public void setPassword(String password) {
        prefs.edit().putString(KEY_PASSWORD, password).apply();
    }

    public String getPassword() {
        return prefs.getString(KEY_PASSWORD, null);
    }

    public void setRefreshToken(String token) {
        prefs.edit().putString(KEY_REFRESH_TOKEN, token).apply();
    }

    public String getRefreshToken() {
        return prefs.getString(KEY_REFRESH_TOKEN, null);
    }

    public void setAccessToken(String token) {
        prefs.edit().putString(KEY_ACCESS_TOKEN, token).apply();
    }

    public String getAccessToken() {
        return prefs.getString(KEY_ACCESS_TOKEN, null);
    }

    public void setSteamId(String steamId) {
        prefs.edit().putString(KEY_STEAM_ID, steamId).apply();
    }

    public String getSteamId() {
        return prefs.getString(KEY_STEAM_ID, null);
    }

    public void setLoginMethod(String method) {
        prefs.edit().putString(KEY_LOGIN_METHOD, method).apply();
    }

    public String getLoginMethod() {
        return prefs.getString(KEY_LOGIN_METHOD, null);
    }

    public void setAutoLogin(boolean enabled) {
        prefs.edit().putBoolean(KEY_AUTO_LOGIN, enabled).apply();
    }

    public boolean isAutoLogin() {
        return prefs.getBoolean(KEY_AUTO_LOGIN, false);
    }

    public void clear() {
        prefs.edit().clear().apply();
    }

    public boolean hasCredentials() {
        return getUsername() != null && getPassword() != null;
    }

    public boolean hasTokens() {
        return getRefreshToken() != null && getAccessToken() != null;
    }
}
