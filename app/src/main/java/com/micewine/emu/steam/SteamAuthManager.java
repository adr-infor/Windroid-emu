package com.micewine.emu.steam;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SteamAuthManager {
    private static final String TAG = "SteamAuthManager";
    
    private final Context context;
    private final SteamPrefs prefs;
    private final ExecutorService executor;
    private AuthCallback callback;

    public interface AuthCallback {
        void onAuthSuccess(String steamId);
        void onAuthFailed(String error);
    }

    public SteamAuthManager(Context context) {
        this.context = context;
        this.prefs = new SteamPrefs(context);
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void loginWithCredentials(String username, String password, AuthCallback callback) {
        this.callback = callback;
        executor.execute(() -> {
            try {
                // Simulação de login - na prática, usaria WebView para autenticação Steam
                // Por enquanto, salvamos as credenciais para uso futuro
                prefs.setUsername(username);
                prefs.setPassword(password);
                
                // Simular Steam ID (na prática, seria obtido via WebView login)
                String fakeSteamId = "76561198000000000";
                prefs.setSteamId(fakeSteamId);
                
                callback.onAuthSuccess(fakeSteamId);
            } catch (Exception e) {
                Log.e(TAG, "Login error", e);
                callback.onAuthFailed(e.getMessage());
            }
        });
    }

    public void logout() {
        executor.execute(() -> {
            try {
                prefs.clear();
            } catch (Exception e) {
                Log.e(TAG, "Logout error", e);
            }
        });
    }

    public void shutdown() {
        executor.shutdown();
    }
}
