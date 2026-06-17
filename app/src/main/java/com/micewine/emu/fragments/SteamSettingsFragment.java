package com.micewine.emu.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.micewine.emu.R;

public class SteamSettingsFragment extends Fragment {
    private static final String STEAM_API_KEY_PREF = "steam_api_key";
    private EditText apiKeyInput;
    private Button saveButton;
    private SharedPreferences prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_steam_settings, container, false);
        
        apiKeyInput = rootView.findViewById(R.id.steamApiKeyInput);
        saveButton = rootView.findViewById(R.id.steamApiKeySave);
        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());

        // Load existing API key
        String existingKey = prefs.getString(STEAM_API_KEY_PREF, "");
        apiKeyInput.setText(existingKey);

        saveButton.setOnClickListener(v -> {
            String apiKey = apiKeyInput.getText().toString().trim();
            if (apiKey.isEmpty()) {
                Toast.makeText(requireContext(), "API key cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            prefs.edit().putString(STEAM_API_KEY_PREF, apiKey).apply();
            Toast.makeText(requireContext(), "API key saved", Toast.LENGTH_SHORT).show();
        });

        return rootView;
    }

    public static String getSteamApiKey(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(STEAM_API_KEY_PREF, "");
    }
}
