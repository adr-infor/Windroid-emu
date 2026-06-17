package com.micewine.emu.steam.models;

import com.google.gson.annotations.SerializedName;

public class SteamAchievement {
    @SerializedName("apiname")
    private String apiName;

    @SerializedName("achieved")
    private int achieved;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public boolean isAchieved() {
        return achieved == 1;
    }

    public void setAchieved(int achieved) {
        this.achieved = achieved;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
