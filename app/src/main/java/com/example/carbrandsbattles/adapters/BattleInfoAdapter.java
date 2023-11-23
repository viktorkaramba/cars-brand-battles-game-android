package com.example.carbrandsbattles.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.carbrandsbattles.R;
import com.example.carbrandsbattles.entities.UserInterfaceBattleData;

import java.util.List;

public class BattleInfoAdapter extends RecyclerView.Adapter<BattleInfoHolder>{

    private SelectListener selectListener;
    private List<UserInterfaceBattleData> battleInfos;

    public BattleInfoAdapter(List<UserInterfaceBattleData> battleInfos, SelectListener selectListener) {
        this.battleInfos = battleInfos;
        this.selectListener = selectListener;
    }

    @NonNull
    @Override
    public BattleInfoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.battle_info_item, viewGroup, false);
        return new BattleInfoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BattleInfoHolder battleInfoHolder,
                                 @SuppressLint("RecyclerView") int i) {
        UserInterfaceBattleData battleInfo = battleInfos.get(i);
        String player1Username = battleInfo.getPlayer1Username();
        String player2Username = battleInfo.getPlayer2Username();
        int playerScore1 =  battleInfo.getPlayerScore1();
        int playerScore2 =  battleInfo.getPlayerScore2();
        battleInfoHolder.userName1.setText(player1Username);
        battleInfoHolder.userName2.setText(player2Username);
        battleInfoHolder.userScore1.setText(String.valueOf(playerScore1));
        battleInfoHolder.userScore2.setText(String.valueOf(playerScore2));
        battleInfoHolder.cardView.setOnClickListener(view -> selectListener.onItemClicked(battleInfos.get(i)));
    }

    @Override
    public int getItemCount() {
        return battleInfos.size();
    }
}
