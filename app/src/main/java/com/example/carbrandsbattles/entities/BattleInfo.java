package com.example.carbrandsbattles.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BattleInfo {
    private int battleId;
    private String player1Username;
    private String player2Username;
    private int playerScore1;
    private int playerScore2;
    private int score1Id;
    private int score2Id;
}
