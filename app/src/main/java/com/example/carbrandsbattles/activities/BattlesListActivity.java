package com.example.carbrandsbattles.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.carbrandsbattles.R;
import com.example.carbrandsbattles.adapters.BattleInfoAdapter;
import com.example.carbrandsbattles.adapters.SelectListener;
import com.example.carbrandsbattles.entities.BattleInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class BattlesListActivity extends AppCompatActivity implements SelectListener {

    private RecyclerView recyclerView;
    private FloatingActionButton addBattleFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView = findViewById(R.id.battlesInfoList);
        addBattleFloatingActionButton = findViewById(R.id.addBattleFloatingActionButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setContentView(R.layout.activity_battles_list);
        getBattlesInfo();
    }

    private void getBattlesInfo() {
        //TODO implement get request
    }

    private void populateListView(List<BattleInfo> body) {
        BattleInfoAdapter battleInfoAdapter = new BattleInfoAdapter(body, this, this);
        recyclerView.setAdapter(battleInfoAdapter);
    }

    @Override
    public void onItemClicked(BattleInfo battleInfo) {

    }
}