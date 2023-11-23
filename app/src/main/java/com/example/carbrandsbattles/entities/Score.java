package com.example.carbrandsbattles.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Score {
    private int userId;
    private int battleId;
    private int playerScore;
}
