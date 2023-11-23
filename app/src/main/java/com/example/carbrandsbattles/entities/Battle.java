package com.example.carbrandsbattles.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Battle {
    private int id;
    private int player1Id;
    private int player2Id;
    private String punishment;
    private boolean isFinished;
    private int currentBrandId;
}
