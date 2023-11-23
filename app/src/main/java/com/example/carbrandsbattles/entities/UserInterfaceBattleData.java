package com.example.carbrandsbattles.entities;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInterfaceBattleData {
    @SerializedName("battleId")
    private int battleId;
    @SerializedName("player1Username")
    private String player1Username;
    @SerializedName("player2Username")
    private String player2Username;
    @SerializedName("playerScore1")
    private int playerScore1;
    @SerializedName("playerScore2")
    private int playerScore2;
    @SerializedName("score1Id")
    private int score1Id;
    @SerializedName("score2Id")
    private int score2Id;
    @SerializedName("punishment")
    private String punishment;
    @SerializedName("brandName")
    private String brandName;
}
