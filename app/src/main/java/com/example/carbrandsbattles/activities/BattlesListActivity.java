package com.example.carbrandsbattles.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.carbrandsbattles.R;
import com.example.carbrandsbattles.adapters.BattleInfoAdapter;
import com.example.carbrandsbattles.adapters.SelectListener;
import com.example.carbrandsbattles.api.CarBrandsBattlesApi;
import com.example.carbrandsbattles.api.RetrofitService;
import com.example.carbrandsbattles.entities.ResponseMessage;
import com.example.carbrandsbattles.entities.UserInterfaceBattleData;
import com.example.carbrandsbattles.entities.UserStatistics;
import com.example.carbrandsbattles.fragments.CreateBattleFragment;
import com.example.carbrandsbattles.services.RefreshTokenService;
import com.example.carbrandsbattles.services.UserInfoService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

import static com.example.carbrandsbattles.constants.Constants.getAuthHeader;

public class BattlesListActivity extends AppCompatActivity implements SelectListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwitchCompat battleFilterSwitch;
    private FloatingActionButton addBattleFloatingActionButton;
    private ImageView userTopButton, logoutImageView, userHistoryImageView;
    private List<UserInterfaceBattleData> userInterfaceBattleDataList;

    private boolean isHistory = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battles_list);
        recyclerView = findViewById(R.id.battlesInfoList);
        swipeRefreshLayout = findViewById(R.id.refreshLayoutBattlesListActivity);
        battleFilterSwitch = findViewById(R.id.battleFilterSwitch);
        battleFilterSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                battleFilterSwitch.setText(getResources().getString(R.string.my_battles));
                populateListView(filterBattles());
            }else {
                battleFilterSwitch.setText(getResources().getString(R.string.all_battles));
                populateListView(userInterfaceBattleDataList);
            }
        });
        userTopButton = findViewById(R.id.userTopButton);
        logoutImageView = findViewById(R.id.logoutImageView);
        userHistoryImageView = findViewById(R.id.userHistoryImageView);
        userHistoryImageView.setOnClickListener(view -> {
            if(isHistory){
                userHistoryImageView.setImageResource(R.drawable.history);
                getBattlesInfo();
            }else{
                userHistoryImageView.setImageResource(R.drawable.today);
                getUserHistory();
            }
        });
        userTopButton.setOnClickListener(view -> goUserTop());
        logoutImageView.setOnClickListener(view -> logout());
        swipeRefreshLayout.setOnRefreshListener(
                this::getBattlesInfo
        );
        addBattleFloatingActionButton = findViewById(R.id.addBattleFloatingActionButton);
        addBattleFloatingActionButton.setOnClickListener(view -> {
            showCreateBattleFragment();
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Intent intent = getIntent();
        if(intent.hasExtra("history")){
            if(getDate(savedInstanceState,"history").equals("yes")){
                userHistoryImageView.setImageResource(R.drawable.today);
                getUserHistory();
            }else{
                getBattlesInfo();
                userHistoryImageView.setImageResource(R.drawable.history);
            }
        }
        else {
            getBattlesInfo();
        }
    }


    private void showCreateBattleFragment() {
        CreateBattleFragment createBattleFragment = new CreateBattleFragment();
        createBattleFragment.setCancelable(false);
        createBattleFragment.show(getSupportFragmentManager(),"Create New Battle");
    }

    public void getBattlesInfo() {
        RetrofitService retrofitService = new RetrofitService();
        CarBrandsBattlesApi carBrandsBattlesApi = retrofitService.getRetrofit().create(CarBrandsBattlesApi.class);
        carBrandsBattlesApi.getBattlesInfo(getAuthHeader()).enqueue(new Callback<List<UserInterfaceBattleData>>() {
            @Override
            public void onResponse(Call<List<UserInterfaceBattleData>> call, Response<List<UserInterfaceBattleData>> response) {
                swipeRefreshLayout.setRefreshing(false);
                if(response.isSuccessful()){
                    if(response.body() != null) {
                        isHistory = false;
                        userInterfaceBattleDataList = response.body();
                        if(battleFilterSwitch.isChecked()){
                            populateListView(filterBattles());
                        }else {
                            populateListView(response.body());
                        }
                    }
                }else{
                    RefreshTokenService<List<UserInterfaceBattleData>> refreshTokenService =
                            new RefreshTokenService<>(BattlesListActivity.this);
                    refreshTokenService.refreshToken(response);
                    refreshTokenService.setRefreshable(() -> getBattlesInfo());
                    refreshTokenService.getRefreshable().refresh();
                }
            }

            @Override
            public void onFailure(Call<List<UserInterfaceBattleData>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                BattlesListActivity.this.runOnUiThread(() -> Toast.makeText(
                        BattlesListActivity.this, "Failed to get data",
                        Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void getUserHistory() {
        RetrofitService retrofitService = new RetrofitService();
        CarBrandsBattlesApi carBrandsBattlesApi = retrofitService.getRetrofit().create(CarBrandsBattlesApi.class);
        carBrandsBattlesApi.getUserHistory(getAuthHeader()).enqueue(new Callback<List<UserInterfaceBattleData>>() {
            @Override
            public void onResponse(Call<List<UserInterfaceBattleData>> call, Response<List<UserInterfaceBattleData>> response) {
                swipeRefreshLayout.setRefreshing(false);
                if(response.isSuccessful()){
                    if(response.body() != null) {
                        isHistory = true;
                        userInterfaceBattleDataList = response.body();
                        if(battleFilterSwitch.isChecked()){
                            populateListView(filterBattles());
                        }else {
                            populateListView(userInterfaceBattleDataList);
                        }
                    }
                }else{
                    RefreshTokenService<List<UserInterfaceBattleData>> refreshTokenService =
                            new RefreshTokenService<>(BattlesListActivity.this);
                    refreshTokenService.refreshToken(response);
                    refreshTokenService.setRefreshable(() -> getBattlesInfo());
                    refreshTokenService.getRefreshable().refresh();
                }
            }

            @Override
            public void onFailure(Call<List<UserInterfaceBattleData>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                BattlesListActivity.this.runOnUiThread(() -> Toast.makeText(
                        BattlesListActivity.this, "Failed to get data",
                        Toast.LENGTH_SHORT).show());
            }
        });
    }


    private void logout() {
        RetrofitService retrofitService = new RetrofitService();
        CarBrandsBattlesApi carBrandsBattlesApi = retrofitService.getRetrofit().create(CarBrandsBattlesApi.class);
        carBrandsBattlesApi.logout(getAuthHeader()).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                if(response.isSuccessful()){
                    if(response.body() != null) {
                        goLogin();
                        UserInfoService.clear();
                    }
                }else{
                    RefreshTokenService<ResponseMessage> refreshTokenService =
                            new RefreshTokenService<>(BattlesListActivity.this);
                    refreshTokenService.refreshToken(response);
                    refreshTokenService.setRefreshable(() -> logout());
                    refreshTokenService.getRefreshable().refresh();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                BattlesListActivity.this.runOnUiThread(() -> Toast.makeText(
                        BattlesListActivity.this, "Failed to get data",
                        Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void populateListView(List<UserInterfaceBattleData> body) {
        BattleInfoAdapter battleInfoAdapter = new BattleInfoAdapter(body, this);
        recyclerView.setAdapter(battleInfoAdapter);
    }

    private List<UserInterfaceBattleData> filterBattles(){
        List<UserInterfaceBattleData> filterList = new ArrayList<>();
        for(UserInterfaceBattleData userInterfaceBattleData: userInterfaceBattleDataList){
            if(userInterfaceBattleData.getPlayer1Username().equals(UserInfoService.getProperty("username")) ||
                    userInterfaceBattleData.getPlayer2Username().equals(UserInfoService.getProperty("username"))){
                filterList.add(userInterfaceBattleData);
            }
        }
        return filterList;
    }

    @Override
    public void onItemClicked(UserInterfaceBattleData battleInfo) {
        if (battleInfo.getPlayer1Username().equals(UserInfoService.getProperty("username")) && !isHistory) {
            Intent intent = new Intent(this, BattleActivity.class);
            intent.putExtra("battleId", String.valueOf(battleInfo.getBattleId()));
            intent.putExtra("scoreId", String.valueOf(battleInfo.getScore1Id()));
            intent.putExtra("player", "first");
            intent.putExtra("info", "no");
            startActivity(intent);
        }else if(battleInfo.getPlayer2Username().equals(UserInfoService.getProperty("username")) && !isHistory) {
            Intent intent = new Intent(this, BattleActivity.class);
            intent.putExtra("battleId", String.valueOf(battleInfo.getBattleId()));
            intent.putExtra("scoreId", String.valueOf(battleInfo.getScore2Id()));
            intent.putExtra("player", "second");
            intent.putExtra("info", "no");
            startActivity(intent);
        }else{
            System.out.println(battleInfo);
            Intent intent = new Intent(this, BattleActivity.class);
            intent.putExtra("battleId", String.valueOf(battleInfo.getBattleId()));
            intent.putExtra("info", "yes");
            if(isHistory) {
                intent.putExtra("history", "yes");
            }else {
                intent.putExtra("history", "no");
            }
            startActivity(intent);
        }
    }

    private void goUserTop() {
        Intent intent = new Intent(this, RankTableActivity.class);
        if(isHistory) {
            intent.putExtra("history", "yes");
        }else {
            intent.putExtra("history", "no");
        }
        startActivity(intent);
    }

    private void goLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public String getDate(Bundle savedInstanceState, String key){
        String result;
        if (savedInstanceState == null) {
            Bundle extras = this.getIntent().getExtras();
            if(extras == null) {
                result= null;
            } else {
                result= extras.getString(key);
            }
        } else {
            result= (String) savedInstanceState.getSerializable(key);
        }
        return result;
    }
    @Override
    public void onItemClicked(UserStatistics userStatistics) {

    }
}