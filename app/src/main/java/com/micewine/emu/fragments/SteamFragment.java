package com.micewine.emu.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.micewine.emu.adapters.AdapterSteamGame;
import com.micewine.emu.steam.SteamApiClient;
import com.micewine.emu.steam.models.SteamGame;
import com.micewine.emu.steam.models.SteamProfile;

import java.util.ArrayList;
import java.util.List;

public class SteamFragment extends Fragment {
    private EditText steamApiKeyInput;
    private EditText steamIdInput;
    private Button steamConnectButton;
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

    private SharedPreferences preferences;
    private SteamApiClient steamApiClient;
    private AdapterSteamGame gamesAdapter;
    private List<SteamGame> gamesList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_steam, container, false);

        preferences = requireActivity().getSharedPreferences("steam_prefs", Context.MODE_PRIVATE);
        steamApiClient = new SteamApiClient(requireContext());

        initViews(rootView);
        setupListeners();
        checkExistingConnection();

        return rootView;
    }

    private void initViews(View rootView) {
        steamApiKeyInput = rootView.findViewById(R.id.steamApiKeyInput);
        steamIdInput = rootView.findViewById(R.id.steamIdInput);
        steamConnectButton = rootView.findViewById(R.id.steamConnectButton);
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
        steamConnectButton.setOnClickListener(v -> connectToSteam());
        steamDisconnectButton.setOnClickListener(v -> disconnectFromSteam());
    }

    private void checkExistingConnection() {
        String apiKey = preferences.getString("steam_api_key", null);
        String steamId = preferences.getString("steam_id", null);

        if (apiKey != null && steamId != null) {
            steamApiKeyInput.setText(apiKey);
            steamIdInput.setText(steamId);
            loadSteamData(apiKey, steamId);
        }
    }

    private void connectToSteam() {
        String apiKey = steamApiKeyInput.getText().toString().trim();
        String steamId = steamIdInput.getText().toString().trim();

        if (apiKey.isEmpty() || steamId.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter API Key and Steam ID", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("steam_api_key", apiKey);
        editor.putString("steam_id", steamId);
        editor.apply();

        loadSteamData(apiKey, steamId);
    }

    private void disconnectFromSteam() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        steamApiKeyInput.setText("");
        steamIdInput.setText("");

        steamLoginSection.setVisibility(View.VISIBLE);
        steamProfileSection.setVisibility(View.GONE);
        steamGamesTitle.setVisibility(View.GONE);
        steamGamesRecyclerView.setVisibility(View.GONE);
        steamStatusText.setText(getString(R.string.steam_not_connected));
        steamStatusText.setVisibility(View.VISIBLE);

        gamesList.clear();
        gamesAdapter.notifyDataSetChanged();
    }

    private void loadSteamData(String apiKey, String steamId) {
        showLoading(true);

        new Thread(() -> {
            try {
                SteamProfile profile = steamApiClient.getSteamProfile(apiKey, steamId);
                List<SteamGame> games = steamApiClient.getSteamGames(apiKey, steamId);

                requireActivity().runOnUiThread(() -> {
                    if (profile != null) {
                        showProfile(profile);
                    }
                    if (games != null && !games.isEmpty()) {
                        showGames(games);
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

    private void showProfile(SteamProfile profile) {
        steamLoginSection.setVisibility(View.GONE);
        steamProfileSection.setVisibility(View.VISIBLE);

        steamPersonaName.setText(profile.getPersonaName());
        steamIdDisplay.setText("Steam ID: " + profile.getSteamId());

        if (profile.getAvatarFull() != null && !profile.getAvatarFull().isEmpty()) {
            Glide.with(requireContext())
                    .load(profile.getAvatarFull())
                    .placeholder(R.drawable.ic_steam)
                    .error(R.drawable.ic_steam)
                    .into(steamAvatar);
        }
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
