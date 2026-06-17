package com.micewine.emu.steam.models;

import com.google.gson.annotations.SerializedName;

public class SteamGame {
    @SerializedName("appid")
    private int appId;

    @SerializedName("name")
    private String name;

    @SerializedName("playtime_forever")
    private int playtimeForever;

    @SerializedName("img_icon_url")
    private String imgIconUrl;

    @SerializedName("img_logo_url")
    private String imgLogoUrl;

    @SerializedName("has_community_visible_stats")
    private boolean hasCommunityVisibleStats;

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlaytimeForever() {
        return playtimeForever;
    }

    public void setPlaytimeForever(int playtimeForever) {
        this.playtimeForever = playtimeForever;
    }

    public String getImgIconUrl() {
        return imgIconUrl;
    }

    public void setImgIconUrl(String imgIconUrl) {
        this.imgIconUrl = imgIconUrl;
    }

    public String getImgLogoUrl() {
        return imgLogoUrl;
    }

    public void setImgLogoUrl(String imgLogoUrl) {
        this.imgLogoUrl = imgLogoUrl;
    }

    public boolean isHasCommunityVisibleStats() {
        return hasCommunityVisibleStats;
    }

    public void setHasCommunityVisibleStats(boolean hasCommunityVisibleStats) {
        this.hasCommunityVisibleStats = hasCommunityVisibleStats;
    }

    public String getIconUrl() {
        if (imgIconUrl != null && !imgIconUrl.isEmpty()) {
            return "http://media.steampowered.com/steamcommunity/public/images/apps/" + appId + "/" + imgIconUrl + ".jpg";
        }
        return null;
    }

    public String getLogoUrl() {
        if (imgLogoUrl != null && !imgLogoUrl.isEmpty()) {
            return "http://media.steampowered.com/steamcommunity/public/images/apps/" + appId + "/" + imgLogoUrl + ".jpg";
        }
        return null;
    }
}
