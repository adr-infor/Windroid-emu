package com.micewine.emu.steam.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SteamProfileResponse {
    @SerializedName("response")
    private SteamProfileData response;

    public SteamProfileData getResponse() {
        return response;
    }

    public void setResponse(SteamProfileData response) {
        this.response = response;
    }

    public static class SteamProfileData {
        @SerializedName("players")
        private List<SteamProfile> players;

        public List<SteamProfile> getPlayers() {
            return players;
        }

        public void setPlayers(List<SteamProfile> players) {
            this.players = players;
        }
    }
}
