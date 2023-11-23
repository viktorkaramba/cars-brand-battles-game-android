package com.example.carbrandsbattles.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.carbrandsbattles.R;
import com.example.carbrandsbattles.entities.UserStatistics;

import java.util.List;

public class UserTopAdapter extends RecyclerView.Adapter<UserTopHolder>{
    private SelectListener selectListener;
    private List<UserStatistics> userRank;

    public UserTopAdapter(List<UserStatistics> userRank, SelectListener selectListener) {
        this.userRank = userRank;
        this.selectListener = selectListener;
    }

    @NonNull
    @Override
    public UserTopHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rank_item, viewGroup, false);
        return new UserTopHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserTopHolder userTopHolder,
                                 @SuppressLint("RecyclerView") int i) {
        UserStatistics userStatistics = userRank.get(i);
        String username = userStatistics.getUsername();
        int topScorer =  userStatistics.getTotalScore();
        userTopHolder.userRankTextView.setText(String.valueOf(i+1));
        userTopHolder.userScoreTextView.setText(String.valueOf(topScorer));
        userTopHolder.usernameTextView.setText(String.valueOf(username));
        userTopHolder.constraintLayout.setOnClickListener(view -> selectListener.onItemClicked(userRank.get(i)));

    }

    @Override
    public int getItemCount() {
        return userRank.size();
    }
}
