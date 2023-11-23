package com.example.carbrandsbattles.adapters;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.carbrandsbattles.R;

public class BattleInfoHolder extends RecyclerView.ViewHolder{
    public TextView userScore1, userScore2, userName1, userName2;
    public CardView cardView;

    public BattleInfoHolder(@NonNull View itemView) {
        super(itemView);
        userScore1 = itemView.findViewById(R.id.userScore1);
        userScore2 = itemView.findViewById(R.id.userScore2);
        userName1 = itemView.findViewById(R.id.userName1);
        userName2 = itemView.findViewById(R.id.userName2);
        cardView = itemView.findViewById(R.id.battleItemCardView);
    }
}
