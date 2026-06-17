package com.micewine.emu.steam.models;

import com.google.gson.annotations.SerializedName;

public class SteamProfile {
    @SerializedName("steamid")
    private String steamId;

    @SerializedName("personaname")
    private String personaName;

    @SerializedName("profileurl")
    private String profileUrl;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("avatarmedium")
    private String avatarMedium;

    @SerializedName("avatarfull")
    private String avatarFull;

    @SerializedName("personastate")
    private int personaState;

    @SerializedName("realname")
    private String realName;

    @SerializedName("primaryclanid")
    private String primaryClanId;

    @SerializedName("timecreated")
    private long timeCreated;

    @SerializedName("loccountrycode")
    private String locCountryCode;

    public String getSteamId() {
        return steamId;
    }

    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }

    public String getPersonaName() {
        return personaName;
    }

    public void setPersonaName(String personaName) {
        this.personaName = personaName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatarMedium() {
        return avatarMedium;
    }

    public void setAvatarMedium(String avatarMedium) {
        this.avatarMedium = avatarMedium;
    }

    public String getAvatarFull() {
        return avatarFull;
    }

    public void setAvatarFull(String avatarFull) {
        this.avatarFull = avatarFull;
    }

    public int getPersonaState() {
        return personaState;
    }

    public void setPersonaState(int personaState) {
        this.personaState = personaState;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPrimaryClanId() {
        return primaryClanId;
    }

    public void setPrimaryClanId(String primaryClanId) {
        this.primaryClanId = primaryClanId;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getLocCountryCode() {
        return locCountryCode;
    }

    public void setLocCountryCode(String locCountryCode) {
        this.locCountryCode = locCountryCode;
    }
}
