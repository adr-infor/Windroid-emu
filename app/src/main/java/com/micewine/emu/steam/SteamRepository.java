package com.micewine.emu.steam;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SteamRepository {
    private static final String TAG = "SteamRepository";
    private static SteamRepository instance;
    
    private final Context context;
    private final SteamPrefs prefs;
    private final ExecutorService executor;
    private boolean isConnected;

    private SteamRepository(Context context) {
        this.context = context.getApplicationContext();
        this.prefs = new SteamPrefs(context);
        this.executor = Executors.newSingleThreadExecutor();
    }

    public static synchronized SteamRepository getInstance(Context context) {
        if (instance == null) {
            instance = new SteamRepository(context);
        }
        return instance;
    }

    public void connect(ConnectionCallback callback) {
        executor.execute(() -> {
            try {
                // Simulação de conexão - na prática, usaria API Web
                isConnected = true;
                callback.onConnected();
            } catch (Exception e) {
                Log.e(TAG, "Connection error", e);
                callback.onConnectionFailed(e.getMessage());
            }
        });
    }

    public void disconnect() {
        executor.execute(() -> {
            try {
                isConnected = false;
            } catch (Exception e) {
                Log.e(TAG, "Disconnect error", e);
            }
        });
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void shutdown() {
        executor.shutdown();
    }

    public interface ConnectionCallback {
        void onConnected();
        void onConnectionFailed(String error);
    }
}
