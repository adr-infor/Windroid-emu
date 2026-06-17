package com.micewine.emu.steam.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SteamGamesResponse {
    @SerializedName("response")
    private SteamGamesData response;

    public SteamGamesData getResponse() {
        return response;
    }

    public void setResponse(SteamGamesData response) {
        this.response = response;
    }

    public static class SteamGamesData {
        @SerializedName("game_count")
        private int gameCount;

        @SerializedName("games")
        private List<SteamGame> games;

        public int getGameCount() {
            return gameCount;
        }

        public void setGameCount(int gameCount) {
            this.gameCount = gameCount;
        }

        public List<SteamGame> getGames() {
            return games;
        }

        public void setGames(List<SteamGame> games) {
            this.games = games;
        }
    }
}
