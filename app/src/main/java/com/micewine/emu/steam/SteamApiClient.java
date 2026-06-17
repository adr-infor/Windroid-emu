package com.micewine.emu.steam;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.micewine.emu.steam.models.SteamGame;
import com.micewine.emu.steam.models.SteamProfile;
import com.micewine.emu.steam.models.SteamProfileResponse;
import com.micewine.emu.steam.models.SteamGamesResponse;
import com.micewine.emu.steam.models.SteamAchievement;
import com.micewine.emu.steam.models.SteamAchievementsResponse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SteamApiClient {
    private static final String BASE_URL = "https://api.steampowered.com/";
    private static final String TAG = "SteamApiClient";
    
    private final OkHttpClient client;
    private final Gson gson;
    private final Context context;

    public SteamApiClient(Context context) {
        this.context = context;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        this.gson = new GsonBuilder().create();
    }

    public SteamProfile getSteamProfile(String apiKey, String steamId) throws IOException {
        String url = BASE_URL + "ISteamUser/GetPlayerSummaries/v0002/?key=" + apiKey + "&steamids=" + steamId;
        
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();
            Log.d(TAG, "Profile response: " + responseBody);

            SteamProfileResponse profileResponse = gson.fromJson(responseBody, SteamProfileResponse.class);
            
            if (profileResponse != null && profileResponse.getResponse() != null 
                    && !profileResponse.getResponse().getPlayers().isEmpty()) {
                return profileResponse.getResponse().getPlayers().get(0);
            }
            
            return null;
        }
    }

    public List<SteamGame> getSteamGames(String apiKey, String steamId) throws IOException {
        String url = BASE_URL + "IPlayerService/GetOwnedGames/v0001/?key=" + apiKey
                + "&steamid=" + steamId
                + "&include_appinfo=true"
                + "&include_played_free_games=true";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();
            Log.d(TAG, "Games response: " + responseBody);

            SteamGamesResponse gamesResponse = gson.fromJson(responseBody, SteamGamesResponse.class);

            if (gamesResponse != null && gamesResponse.getResponse() != null
                    && gamesResponse.getResponse().getGames() != null) {
                return gamesResponse.getResponse().getGames();
            }

            return new ArrayList<>();
        }
    }

    public List<SteamAchievement> getSteamAchievements(String apiKey, String steamId, String appId) throws IOException {
        String url = BASE_URL + "ISteamUserStats/GetPlayerAchievements/v0001/?key=" + apiKey
                + "&steamid=" + steamId
                + "&appid=" + appId
                + "&l=english";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();
            Log.d(TAG, "Achievements response: " + responseBody);

            SteamAchievementsResponse achievementsResponse = gson.fromJson(responseBody, SteamAchievementsResponse.class);

            if (achievementsResponse != null && achievementsResponse.getPlayerstats() != null
                    && achievementsResponse.getPlayerstats().getAchievements() != null) {
                return achievementsResponse.getPlayerstats().getAchievements();
            }

            return new ArrayList<>();
        }
    }
}
