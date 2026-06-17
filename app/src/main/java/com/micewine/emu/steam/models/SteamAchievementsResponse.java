package com.micewine.emu.steam.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SteamAchievementsResponse {
    @SerializedName("playerstats")
    private PlayerStats playerStats;

    public PlayerStats getPlayerstats() {
        return playerStats;
    }

    public void setPlayerstats(PlayerStats playerStats) {
        this.playerStats = playerStats;
    }

    public static class PlayerStats {
        @SerializedName("steamID")
        private String steamId;

        @SerializedName("gameName")
        private String gameName;

        @SerializedName("achievements")
        private List<SteamAchievement> achievements;

        @SerializedName("success")
        private boolean success;

        public String getSteamId() {
            return steamId;
        }

        public void setSteamId(String steamId) {
            this.steamId = steamId;
        }

        public String getGameName() {
            return gameName;
        }

        public void setGameName(String gameName) {
            this.gameName = gameName;
        }

        public List<SteamAchievement> getAchievements() {
            return achievements;
        }

        public void setAchievements(List<SteamAchievement> achievements) {
            this.achievements = achievements;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }
}
