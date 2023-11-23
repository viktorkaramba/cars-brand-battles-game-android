package com.example.carbrandsbattles.adapters;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.carbrandsbattles.R;

public class UserTopHolder extends RecyclerView.ViewHolder{
    public TextView userRankTextView, usernameTextView, userScoreTextView;
    public ConstraintLayout constraintLayout;

    public UserTopHolder(@NonNull View itemView) {
        super(itemView);
        userRankTextView = itemView.findViewById(R.id.userRankTextView);
        usernameTextView = itemView.findViewById(R.id.usernameTextView);
        userScoreTextView = itemView.findViewById(R.id.userScoreTextView);
        constraintLayout = itemView.findViewById(R.id.UserRankRecycleView);
    }

}
