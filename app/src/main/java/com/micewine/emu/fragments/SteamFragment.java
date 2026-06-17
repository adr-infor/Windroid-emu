package com.micewine.emu.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.micewine.emu.R;
import com.micewine.emu.activities.SteamLoginActivity;
import com.micewine.emu.adapters.AdapterSteamGame;
import com.micewine.emu.steam.SteamDatabase;
import com.micewine.emu.steam.SteamGame;
import com.micewine.emu.steam.SteamPrefs;

import java.util.ArrayList;
import java.util.List;

public class SteamFragment extends Fragment {
    private Button steamLoginButton;
    private Button steamDisconnectButton;
    private View steamLoginSection;
    private View steamProfileSection;
    private ImageView steamAvatar;
    private TextView steamPersonaName;
    private TextView steamIdDisplay;
    private TextView steamGamesTitle;
    private TextView steamStatusText;
    private ProgressBar steamProgressBar;
    private RecyclerView steamGamesRecyclerView;

    private SteamPrefs prefs;
    private SteamDatabase database;
    private AdapterSteamGame gamesAdapter;
    private List<SteamGame> gamesList = new ArrayList<>();

    private static final int STEAM_LOGIN_REQUEST_CODE = 1001;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_steam, container, false);

        prefs = new SteamPrefs(requireContext());
        database = new SteamDatabase(requireContext());

        initViews(rootView);
        setupListeners();
        checkExistingConnection();

        return rootView;
    }

    private void initViews(View rootView) {
        steamLoginButton = rootView.findViewById(R.id.steamLoginButton);
        steamDisconnectButton = rootView.findViewById(R.id.steamDisconnectButton);
        steamLoginSection = rootView.findViewById(R.id.steamLoginSection);
        steamProfileSection = rootView.findViewById(R.id.steamProfileSection);
        steamAvatar = rootView.findViewById(R.id.steamAvatar);
        steamPersonaName = rootView.findViewById(R.id.steamPersonaName);
        steamIdDisplay = rootView.findViewById(R.id.steamIdDisplay);
        steamGamesTitle = rootView.findViewById(R.id.steamGamesTitle);
        steamStatusText = rootView.findViewById(R.id.steamStatusText);
        steamProgressBar = rootView.findViewById(R.id.steamProgressBar);
        steamGamesRecyclerView = rootView.findViewById(R.id.steamGamesRecyclerView);

        gamesAdapter = new AdapterSteamGame(gamesList, requireActivity());
        steamGamesRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        steamGamesRecyclerView.setAdapter(gamesAdapter);
    }

    private void setupListeners() {
        steamLoginButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SteamLoginActivity.class);
            startActivityForResult(intent, STEAM_LOGIN_REQUEST_CODE);
        });
        steamDisconnectButton.setOnClickListener(v -> disconnectFromSteam());
    }

    private void checkExistingConnection() {
        if (prefs.getUsername() != null) {
            loadSteamData();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == STEAM_LOGIN_REQUEST_CODE && resultCode == android.app.Activity.RESULT_OK) {
            loadSteamData();
        }
    }

    private void disconnectFromSteam() {
        prefs.clear();
        database.clearAllGames();

        steamLoginSection.setVisibility(View.VISIBLE);
        steamProfileSection.setVisibility(View.GONE);
        steamGamesTitle.setVisibility(View.GONE);
        steamGamesRecyclerView.setVisibility(View.GONE);
        steamStatusText.setText(getString(R.string.steam_not_connected));
        steamStatusText.setVisibility(View.VISIBLE);

        gamesList.clear();
        gamesAdapter.notifyDataSetChanged();
    }

    private void loadSteamData() {
        showLoading(true);

        new Thread(() -> {
            try {
                List<SteamGame> games = database.getAllGames();
                
                requireActivity().runOnUiThread(() -> {
                    if (games != null && !games.isEmpty()) {
                        showGames(games);
                        showProfile();
                    } else {
                        showNoGames();
                    }
                    showLoading(false);
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    showError(e.getMessage());
                    showLoading(false);
                });
            }
        }).start();
    }

    private void showProfile() {
        steamLoginSection.setVisibility(View.GONE);
        steamProfileSection.setVisibility(View.VISIBLE);

        String username = prefs.getUsername();
        String steamId = prefs.getSteamId();

        steamPersonaName.setText(username != null ? username : "Steam User");
        steamIdDisplay.setText("Steam ID: " + (steamId != null ? steamId : "Unknown"));

        // Placeholder avatar - pode ser atualizado com avatar real da Steam
        steamAvatar.setImageResource(R.drawable.ic_steam);
    }

    private void showGames(List<SteamGame> games) {
        gamesList.clear();
        gamesList.addAll(games);
        gamesAdapter.notifyDataSetChanged();

        steamGamesTitle.setVisibility(View.VISIBLE);
        steamGamesRecyclerView.setVisibility(View.VISIBLE);
        steamStatusText.setVisibility(View.GONE);
    }

    private void showNoGames() {
        steamGamesTitle.setVisibility(View.GONE);
        steamGamesRecyclerView.setVisibility(View.GONE);
        steamStatusText.setText(getString(R.string.steam_no_games));
        steamStatusText.setVisibility(View.VISIBLE);
    }

    private void showError(String error) {
        steamStatusText.setText(getString(R.string.steam_error) + ": " + error);
        steamStatusText.setVisibility(View.VISIBLE);
    }

    private void showLoading(boolean loading) {
        if (loading) {
            steamProgressBar.setVisibility(View.VISIBLE);
            steamStatusText.setText(getString(R.string.steam_loading));
            steamStatusText.setVisibility(View.VISIBLE);
        } else {
            steamProgressBar.setVisibility(View.GONE);
        }
    }
}
