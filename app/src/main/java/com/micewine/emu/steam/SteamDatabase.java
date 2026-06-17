package com.micewine.emu.steam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SteamDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "steam.db";
    private static final int DATABASE_VERSION = 1;

    // Tabela games
    private static final String TABLE_GAMES = "games";
    private static final String COLUMN_GAME_ID = "game_id";
    private static final String COLUMN_APP_ID = "app_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SIZE = "size";
    private static final String COLUMN_INSTALLED = "installed";
    private static final String COLUMN_INSTALL_PATH = "install_path";
    private static final String COLUMN_COVER_URL = "cover_url";
    private static final String COLUMN_LAST_PLAYED = "last_played";
    private static final String COLUMN_PLAYTIME = "playtime";

    public SteamDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GAMES_TABLE = "CREATE TABLE " + TABLE_GAMES + "("
                + COLUMN_GAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_APP_ID + " INTEGER UNIQUE,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_SIZE + " INTEGER DEFAULT 0,"
                + COLUMN_INSTALLED + " INTEGER DEFAULT 0,"
                + COLUMN_INSTALL_PATH + " TEXT,"
                + COLUMN_COVER_URL + " TEXT,"
                + COLUMN_LAST_PLAYED + " INTEGER DEFAULT 0,"
                + COLUMN_PLAYTIME + " INTEGER DEFAULT 0"
                + ")";
        db.execSQL(CREATE_GAMES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
        onCreate(db);
    }

    public void addGame(SteamGame game) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_APP_ID, game.getAppId());
        values.put(COLUMN_NAME, game.getName());
        values.put(COLUMN_SIZE, game.getSize());
        values.put(COLUMN_INSTALLED, game.isInstalled() ? 1 : 0);
        values.put(COLUMN_INSTALL_PATH, game.getInstallPath());
        values.put(COLUMN_COVER_URL, game.getCoverUrl());
        values.put(COLUMN_LAST_PLAYED, game.getLastPlayed());
        values.put(COLUMN_PLAYTIME, game.getPlaytime());

        db.insertWithOnConflict(TABLE_GAMES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public SteamGame getGame(int appId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_GAMES,
                new String[]{COLUMN_APP_ID, COLUMN_NAME, COLUMN_SIZE, COLUMN_INSTALLED, 
                           COLUMN_INSTALL_PATH, COLUMN_COVER_URL, COLUMN_LAST_PLAYED, COLUMN_PLAYTIME},
                COLUMN_APP_ID + " = ?",
                new String[]{String.valueOf(appId)},
                null, null, null);

        SteamGame game = null;
        if (cursor != null && cursor.moveToFirst()) {
            game = new SteamGame(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_APP_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_SIZE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_INSTALLED)) == 1,
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INSTALL_PATH)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COVER_URL)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_LAST_PLAYED)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_PLAYTIME))
            );
            cursor.close();
        }
        db.close();
        return game;
    }

    public List<SteamGame> getAllGames() {
        List<SteamGame> games = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_GAMES,
                new String[]{COLUMN_APP_ID, COLUMN_NAME, COLUMN_SIZE, COLUMN_INSTALLED, 
                           COLUMN_INSTALL_PATH, COLUMN_COVER_URL, COLUMN_LAST_PLAYED, COLUMN_PLAYTIME},
                null, null, null, null, COLUMN_NAME + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                SteamGame game = new SteamGame(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_APP_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_SIZE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_INSTALLED)) == 1,
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INSTALL_PATH)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COVER_URL)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_LAST_PLAYED)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_PLAYTIME))
                );
                games.add(game);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return games;
    }

    public void updateGame(SteamGame game) {
        addGame(game); // Usa CONFLICT_REPLACE
    }

    public void deleteGame(int appId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GAMES, COLUMN_APP_ID + " = ?", new String[]{String.valueOf(appId)});
        db.close();
    }

    public void clearAllGames() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GAMES, null, null);
        db.close();
    }

    public int getGameCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_GAMES, null);
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        db.close();
        return count;
    }
}
