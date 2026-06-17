package com.micewine.emu.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.micewine.emu.R;
import com.micewine.emu.steam.SteamAuthManager;
import com.micewine.emu.steam.SteamPrefs;

public class SteamLoginActivity extends AppCompatActivity {
    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;
    private Button cancelButton;
    private ProgressBar progressBar;
    private TextView statusText;
    private View steamGuardSection;

    private SteamAuthManager authManager;
    private SteamPrefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steam_login);

        authManager = new SteamAuthManager(this);
        prefs = new SteamPrefs(this);

        initViews();
        setupListeners();
    }

    private void initViews() {
        usernameInput = findViewById(R.id.steamUsernameInput);
        passwordInput = findViewById(R.id.steamPasswordInput);
        loginButton = findViewById(R.id.steamLoginButton);
        cancelButton = findViewById(R.id.steamCancelButton);
        progressBar = findViewById(R.id.steamProgressBar);
        statusText = findViewById(R.id.steamStatusText);
        steamGuardSection = findViewById(R.id.steamGuardSection);
        steamGuardSection.setVisibility(View.GONE); // Esconder seção Steam Guard por enquanto
    }

    private void setupListeners() {
        loginButton.setOnClickListener(v -> attemptLogin());
        cancelButton.setOnClickListener(v -> finish());
    }

    private void attemptLogin() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);
        statusText.setText("Connecting to Steam...");

        authManager.loginWithCredentials(username, password, new SteamAuthManager.AuthCallback() {
            @Override
            public void onAuthSuccess(String steamId) {
                runOnUiThread(() -> {
                    Intent resultIntent = new Intent();
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                });
            }

            @Override
            public void onAuthFailed(String error) {
                runOnUiThread(() -> {
                    showLoading(false);
                    statusText.setText("Login failed: " + error);
                    Toast.makeText(SteamLoginActivity.this, error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void showLoading(boolean loading) {
        if (loading) {
            progressBar.setVisibility(View.VISIBLE);
            loginButton.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            loginButton.setEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (authManager != null) {
            authManager.shutdown();
        }
    }
}
