package com.example.carbrandsbattles.entities;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinishBattleInput {
    @SerializedName("isFinished")
    private boolean isFinished;
}
