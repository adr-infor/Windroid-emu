package com.micewine.emu.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.micewine.emu.R;
import com.micewine.emu.steam.SteamGame;

import java.util.List;

public class AdapterSteamGame extends RecyclerView.Adapter<AdapterSteamGame.GameViewHolder> {
    private List<SteamGame> gamesList;
    private Activity activity;

    public AdapterSteamGame(List<SteamGame> gamesList, Activity activity) {
        this.gamesList = gamesList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_steam_game, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        SteamGame game = gamesList.get(position);
        
        holder.gameName.setText(game.getName());
        
        String coverUrl = game.getCoverUrl();
        if (coverUrl != null && !coverUrl.isEmpty()) {
            Glide.with(activity)
                    .load(coverUrl)
                    .placeholder(R.drawable.ic_steam)
                    .error(R.drawable.ic_steam)
                    .into(holder.gameIcon);
        } else {
            holder.gameIcon.setImageResource(R.drawable.ic_steam);
        }
    }

    @Override
    public int getItemCount() {
        return gamesList.size();
    }

    static class GameViewHolder extends RecyclerView.ViewHolder {
        ImageView gameIcon;
        TextView gameName;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            gameIcon = itemView.findViewById(R.id.steamGameIcon);
            gameName = itemView.findViewById(R.id.steamGameName);
        }
    }
}
