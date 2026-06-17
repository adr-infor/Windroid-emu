package com.micewine.emu.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.micewine.emu.R;
import com.micewine.emu.steam.SteamApiClient;
import com.micewine.emu.steam.SteamImageLoader;
import com.micewine.emu.steam.SteamPrefs;
import com.micewine.emu.steam.adapters.SteamGamesAdapter;
import com.micewine.emu.steam.adapters.SteamAchievementsAdapter;
import com.micewine.emu.steam.models.SteamGame;
import com.micewine.emu.steam.models.SteamProfile;
import com.micewine.emu.steam.models.SteamAchievement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SteamProfileActivity extends AppCompatActivity {
    private static final String TAG = "SteamProfileActivity";
    private static final String STEAM_API_KEY = "YOUR_STEAM_API_KEY"; // TODO: Replace with actual API key

    private SteamPrefs steamPrefs;
    private SteamApiClient steamApiClient;
    private SteamImageLoader imageLoader;
    private ExecutorService executor;

    private ImageView profileAvatar;
    private TextView profileName;
    private TextView profileStatus;
    private TextView gamesCount;
    private RecyclerView gamesRecyclerView;
    private ProgressBar gamesProgressBar;
    private TextView gamesEmptyText;
    private RecyclerView achievementsRecyclerView;
    private ProgressBar achievementsProgressBar;
    private TextView achievementsTitle;

    private List<SteamGame> gamesList = new ArrayList<>();
    private List<SteamAchievement> achievementsList = new ArrayList<>();
    private SteamGamesAdapter gamesAdapter;
    private SteamAchievementsAdapter achievementsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steam_profile);

        steamPrefs = new SteamPrefs(this);
        steamApiClient = new SteamApiClient(this);
        imageLoader = new SteamImageLoader();
        executor = Executors.newSingleThreadExecutor();

        initViews();
        setupTabs();
        loadSteamProfile();
    }

    private void initViews() {
        profileAvatar = findViewById(R.id.steamProfileAvatar);
        profileName = findViewById(R.id.steamProfileName);
        profileStatus = findViewById(R.id.steamProfileStatus);
        gamesCount = findViewById(R.id.steamProfileGamesCount);
        gamesRecyclerView = findViewById(R.id.steamGamesRecyclerView);
        gamesProgressBar = findViewById(R.id.steamGamesProgressBar);
        gamesEmptyText = findViewById(R.id.steamGamesEmptyText);
        achievementsRecyclerView = findViewById(R.id.steamAchievementsRecyclerView);
        achievementsProgressBar = findViewById(R.id.steamAchievementsProgressBar);
        achievementsTitle = findViewById(R.id.steamAchievementsTitle);

        gamesAdapter = new SteamGamesAdapter(gamesList, this::onGameClicked);
        gamesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        gamesRecyclerView.setAdapter(gamesAdapter);

        achievementsAdapter = new SteamAchievementsAdapter(achievementsList);
        achievementsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        achievementsRecyclerView.setAdapter(achievementsAdapter);
    }

    private void setupTabs() {
        TabHost tabHost = findViewById(R.id.steamProfileTabHost);
        tabHost.setup();

        TabHost.TabSpec gamesTab = tabHost.newTabSpec("games");
        gamesTab.setContent(R.id.tab_games);
        gamesTab.setIndicator("Games");
        tabHost.addTab(gamesTab);

        TabHost.TabSpec achievementsTab = tabHost.newTabSpec("achievements");
        achievementsTab.setContent(R.id.tab_achievements);
        achievementsTab.setIndicator("Achievements");
        tabHost.addTab(achievementsTab);
    }

    private void loadSteamProfile() {
        if (!steamPrefs.isLoggedIn()) {
            Toast.makeText(this, "Not logged in to Steam", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String steamId = steamPrefs.getSteamId();
        loadProfileData(steamId);
        loadGamesData(steamId);
    }

    private void loadProfileData(String steamId) {
        executor.execute(() -> {
            try {
                SteamProfile profile = steamApiClient.getSteamProfile(STEAM_API_KEY, steamId);
                if (profile != null) {
                    runOnUiThread(() -> {
                        profileName.setText(profile.getPersonaName());
                        
                        String statusText = getStatusText(profile.getPersonaState());
                        profileStatus.setText(statusText);
                        
                        String avatarUrl = profile.getAvatarFull();
                        if (avatarUrl != null && !avatarUrl.isEmpty()) {
                            imageLoader.loadImage(avatarUrl, profileAvatar);
                        }

                        // Save avatar URL to prefs
                        steamPrefs.setAvatarUrl(avatarUrl);
                        steamPrefs.setDisplayName(profile.getPersonaName());
                    });
                }
            } catch (Exception e) {
                Log.e(TAG, "Failed to load profile", e);
                runOnUiThread(() -> 
                    Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private String getStatusText(int personaState) {
        switch (personaState) {
            case 0: return "Offline";
            case 1: return "Online";
            case 2: return "Busy";
            case 3: return "Away";
            case 4: return "Snooze";
            case 5: return "Looking to trade";
            case 6: return "Looking to play";
            default: return "Unknown";
        }
    }

    private void loadGamesData(String steamId) {
        gamesProgressBar.setVisibility(View.VISIBLE);
        gamesEmptyText.setVisibility(View.GONE);

        executor.execute(() -> {
            try {
                List<SteamGame> games = steamApiClient.getSteamGames(STEAM_API_KEY, steamId);
                gamesList.clear();
                gamesList.addAll(games);

                runOnUiThread(() -> {
                    gamesProgressBar.setVisibility(View.GONE);
                    gamesAdapter.notifyDataSetChanged();
                    
                    int count = games.size();
                    gamesCount.setText(count + " games");
                    
                    if (count == 0) {
                        gamesEmptyText.setVisibility(View.VISIBLE);
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Failed to load games", e);
                runOnUiThread(() -> {
                    gamesProgressBar.setVisibility(View.GONE);
                    gamesEmptyText.setVisibility(View.VISIBLE);
                    gamesEmptyText.setText("Failed to load games");
                });
            }
        });
    }

    private void onGameClicked(SteamGame game) {
        loadAchievementsData(game.getAppId());
    }

    private void loadAchievementsData(String appId) {
        achievementsProgressBar.setVisibility(View.VISIBLE);
        achievementsTitle.setText("Loading achievements...");

        String steamId = steamPrefs.getSteamId();
        executor.execute(() -> {
            try {
                List<SteamAchievement> achievements = steamApiClient.getSteamAchievements(STEAM_API_KEY, steamId, appId);
                achievementsList.clear();
                achievementsList.addAll(achievements);

                int achievedCount = 0;
                for (SteamAchievement achievement : achievements) {
                    if (achievement.isAchieved()) {
                        achievedCount++;
                    }
                }

                final int finalAchievedCount = achievedCount;
                runOnUiThread(() -> {
                    achievementsProgressBar.setVisibility(View.GONE);
                    achievementsAdapter.notifyDataSetChanged();
                    
                    if (achievements.isEmpty()) {
                        achievementsTitle.setText("No achievements available for this game");
                    } else {
                        achievementsTitle.setText("Achievements: " + finalAchievedCount + "/" + achievements.size());
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Failed to load achievements", e);
                runOnUiThread(() -> {
                    achievementsProgressBar.setVisibility(View.GONE);
                    achievementsTitle.setText("Failed to load achievements");
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null) {
            executor.shutdown();
        }
    }
}
