package com.example.carbrandsbattles.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.carbrandsbattles.R;
import com.example.carbrandsbattles.adapters.SelectListener;
import com.example.carbrandsbattles.adapters.UserTopAdapter;
import com.example.carbrandsbattles.api.CarBrandsBattlesApi;
import com.example.carbrandsbattles.api.RetrofitService;
import com.example.carbrandsbattles.entities.UserInterfaceBattleData;
import com.example.carbrandsbattles.entities.UserStatistics;
import com.example.carbrandsbattles.services.RefreshTokenService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

import static com.example.carbrandsbattles.constants.Constants.getAuthHeader;

public class RankTableActivity extends AppCompatActivity implements SelectListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageButton backImageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_table);
        recyclerView = findViewById(R.id.rankList);
        backImageButton = findViewById(R.id.backImageButton);
        backImageButton.setOnClickListener(view -> goBattleList());
        swipeRefreshLayout = findViewById(R.id.refreshLayoutRankTableActivity);
        swipeRefreshLayout.setOnRefreshListener(
                this::getUserStatistics
        );
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getUserStatistics();
    }

    public void getUserStatistics() {
        RetrofitService retrofitService = new RetrofitService();
        CarBrandsBattlesApi carBrandsBattlesApi = retrofitService.getRetrofit().create(CarBrandsBattlesApi.class);
        carBrandsBattlesApi.getUserStatistics(getAuthHeader()).enqueue(new Callback<List<UserStatistics>>() {
            @Override
            public void onResponse(Call<List<UserStatistics>> call, Response<List<UserStatistics>> response) {
                if(response.isSuccessful()){
                    swipeRefreshLayout.setRefreshing(false);
                    if(response.body() != null) {
                        populateListView(response.body());
                    }
                }else{
                    swipeRefreshLayout.setRefreshing(false);
                    RefreshTokenService<List<UserStatistics>> refreshTokenService =
                            new RefreshTokenService<>(RankTableActivity.this);
                    refreshTokenService.refreshToken(response);
                    refreshTokenService.setRefreshable(() -> getUserStatistics());
                    refreshTokenService.getRefreshable().refresh();
                }
            }

            @Override
            public void onFailure(Call<List<UserStatistics>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                RankTableActivity.this.runOnUiThread(() -> Toast.makeText(
                        RankTableActivity.this, "Failed to get users top",
                        Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void populateListView(List<UserStatistics> body) {
        UserTopAdapter userTopAdapter = new UserTopAdapter(body, this);
        System.out.println(body);
        recyclerView.setAdapter(userTopAdapter);
    }

    private void goBattleList() {
        Intent intent = new Intent(this, BattlesListActivity.class);
        intent.putExtras(getIntent().getExtras());
        startActivity(intent);
    }

    @Override
    public void onItemClicked(UserInterfaceBattleData battleInfo) {

    }

    @Override
    public void onItemClicked(UserStatistics userStatistics) {

    }
}