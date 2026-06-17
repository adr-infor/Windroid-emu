package com.micewine.emu.steam.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.micewine.emu.R;
import com.micewine.emu.steam.SteamImageLoader;
import com.micewine.emu.steam.models.SteamGame;

import java.util.List;
import java.util.function.Consumer;

public class SteamGamesAdapter extends RecyclerView.Adapter<SteamGamesAdapter.GameViewHolder> {
    private List<SteamGame> games;
    private final Consumer<SteamGame> onGameClicked;
    private final SteamImageLoader imageLoader;

    public SteamGamesAdapter(List<SteamGame> games, Consumer<SteamGame> onGameClicked) {
        this.games = games;
        this.onGameClicked = onGameClicked;
        this.imageLoader = new SteamImageLoader();
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
        SteamGame game = games.get(position);
        holder.bind(game);
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public void updateGames(List<SteamGame> newGames) {
        this.games = newGames;
        notifyDataSetChanged();
    }

    class GameViewHolder extends RecyclerView.ViewHolder {
        private ImageView gameImage;
        private TextView gameName;
        private TextView gamePlaytime;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            gameImage = itemView.findViewById(R.id.steamGameImage);
            gameName = itemView.findViewById(R.id.steamGameName);
            gamePlaytime = itemView.findViewById(R.id.steamGamePlaytime);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onGameClicked.accept(games.get(position));
                }
            });
        }

        public void bind(SteamGame game) {
            gameName.setText(game.getName());
            
            String playtimeText = game.getFormattedPlaytime();
            gamePlaytime.setText(playtimeText);

            // Load game image if available
            String imageUrl = game.getImgIconUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                String fullImageUrl = "http://media.steampowered.com/steamcommunity/public/images/apps/" 
                        + game.getAppId() + "/" + imageUrl;
                imageLoader.loadImage(fullImageUrl, gameImage);
            }
        }
    }
}
