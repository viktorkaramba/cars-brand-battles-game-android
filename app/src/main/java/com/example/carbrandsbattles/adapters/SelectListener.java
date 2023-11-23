package com.example.carbrandsbattles.adapters;

import com.example.carbrandsbattles.entities.UserInterfaceBattleData;
import com.example.carbrandsbattles.entities.UserStatistics;

public interface SelectListener {
    void onItemClicked(UserInterfaceBattleData battleInfo);
    void onItemClicked(UserStatistics userStatistics);
}
