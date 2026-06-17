package com.micewine.emu.steam;

public class SteamEvent {
    public static class LoginSuccess extends SteamEvent {
        public final String steamId;
        public final String username;

        public LoginSuccess(String steamId, String username) {
            this.steamId = steamId;
            this.username = username;
        }
    }

    public static class LoginFailed extends SteamEvent {
        public final String error;

        public LoginFailed(String error) {
            this.error = error;
        }
    }

    public static class Logout extends SteamEvent {}

    public static class LibrarySyncStart extends SteamEvent {}

    public static class LibrarySyncProgress extends SteamEvent {
        public final int current;
        public final int total;

        public LibrarySyncProgress(int current, int total) {
            this.current = current;
            this.total = total;
        }
    }

    public static class LibrarySyncComplete extends SteamEvent {
        public final int gameCount;

        public LibrarySyncComplete(int gameCount) {
            this.gameCount = gameCount;
        }
    }

    public static class LibrarySyncFailed extends SteamEvent {
        public final String error;

        public LibrarySyncFailed(String error) {
            this.error = error;
        }
    }

    public static class ConnectionLost extends SteamEvent {
        public final String reason;

        public ConnectionLost(String reason) {
            this.reason = reason;
        }
    }

    public static class ConnectionRestored extends SteamEvent {}
}
