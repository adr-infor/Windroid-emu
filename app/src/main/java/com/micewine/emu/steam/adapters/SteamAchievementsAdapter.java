package com.micewine.emu.steam.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.micewine.emu.R;
import com.micewine.emu.steam.models.SteamAchievement;

import java.util.List;

public class SteamAchievementsAdapter extends RecyclerView.Adapter<SteamAchievementsAdapter.AchievementViewHolder> {
    private List<SteamAchievement> achievements;

    public SteamAchievementsAdapter(List<SteamAchievement> achievements) {
        this.achievements = achievements;
    }

    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_steam_achievement, parent, false);
        return new AchievementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
        SteamAchievement achievement = achievements.get(position);
        holder.bind(achievement);
    }

    @Override
    public int getItemCount() {
        return achievements.size();
    }

    public void updateAchievements(List<SteamAchievement> newAchievements) {
        this.achievements = newAchievements;
        notifyDataSetChanged();
    }

    static class AchievementViewHolder extends RecyclerView.ViewHolder {
        private TextView achievementName;
        private TextView achievementDescription;
        private TextView achievementStatus;

        public AchievementViewHolder(@NonNull View itemView) {
            super(itemView);
            achievementName = itemView.findViewById(R.id.achievementName);
            achievementDescription = itemView.findViewById(R.id.achievementDescription);
            achievementStatus = itemView.findViewById(R.id.achievementStatus);
        }

        public void bind(SteamAchievement achievement) {
            achievementName.setText(achievement.getName());
            achievementDescription.setText(achievement.getDescription());
            
            if (achievement.isAchieved()) {
                achievementStatus.setText("✓ Unlocked");
                achievementStatus.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_green_light));
            } else {
                achievementStatus.setText("Locked");
                achievementStatus.setTextColor(itemView.getContext().getResources().getColor(android.R.color.darker_gray));
            }
        }
    }
}
