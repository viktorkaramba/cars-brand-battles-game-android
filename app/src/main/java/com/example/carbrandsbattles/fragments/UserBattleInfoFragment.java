package com.example.carbrandsbattles.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.carbrandsbattles.R;
import com.example.carbrandsbattles.activities.BattlesListActivity;
import com.example.carbrandsbattles.api.CarBrandsBattlesApi;
import com.example.carbrandsbattles.api.RetrofitService;
import com.example.carbrandsbattles.constants.Constants;
import com.example.carbrandsbattles.entities.FinishBattleInput;
import com.example.carbrandsbattles.entities.ResponseMessage;
import com.example.carbrandsbattles.entities.Score;
import com.example.carbrandsbattles.entities.UserInterfaceBattleData;
import com.example.carbrandsbattles.services.RefreshTokenService;
import com.example.carbrandsbattles.services.UserInfoService;
import retrofit2.Call;
import retrofit2.Callback;

import static com.example.carbrandsbattles.constants.Constants.getAuthHeader;

public class UserBattleInfoFragment extends DialogFragment {

    private TextView userScore1TextView, userScore2TextView, userName1TextView, userName2TextView, brandTextView,
            punishmentTextView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int battleId, scoreId,  playerScore1, playerScore2;
    private  String playerUserName1, playerUserName2, player, brandName, punishment;

    public static UserBattleInfoFragment newInstance() {
        UserBattleInfoFragment fragment = new UserBattleInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_battle_info,
                container, false);
        UserInfoService.init(UserBattleInfoFragment.newInstance().getActivity());
        userName1TextView = view.findViewById(R.id.userName1TextView);
        userName2TextView = view.findViewById(R.id.userName2TextView);
        userScore1TextView = view.findViewById(R.id.userScore1TextView);
        punishmentTextView = view.findViewById(R.id.punishmentTextView);
        brandTextView = view.findViewById(R.id.brandTextView);
        Button finishBattleButton = view.findViewById(R.id.finishBattleButton);
        finishBattleButton.setOnClickListener(view1 -> finishBattle());
        swipeRefreshLayout = view.findViewById(R.id.refreshLayoutUserBattleInfoFragment);
        swipeRefreshLayout.setOnRefreshListener(
                this::getBattleInfo
        );
        userScore2TextView = view.findViewById(R.id.userScore2TextView);
        battleId = Integer.parseInt(getDate(savedInstanceState, "battleId"));
        scoreId = Integer.parseInt(getDate(savedInstanceState, "scoreId"));
        player = getDate(savedInstanceState, "player");
        CardView updateScoreCardView = view.findViewById(R.id.plusCardView);
        updateScoreCardView.setOnClickListener(view3 -> updateScore());
        getBattleInfo();
        // Inflate the layout for this fragment
        return view;
    }

    private void updateScore() {
        RetrofitService retrofitService = new RetrofitService();
        CarBrandsBattlesApi carBrandsBattlesApi = retrofitService.getRetrofit().create(CarBrandsBattlesApi.class);
        int playerScore;
        if(isPlayer1()){
            playerScore = playerScore1 + 1;
        }else{
            playerScore = playerScore2 + 1;
        }
        Score score = new Score(Integer.parseInt(UserInfoService.getProperty("userId")), battleId, playerScore);
        int finalPlayerScore = playerScore;
        carBrandsBattlesApi.updateScore(Constants.getAuthHeader(), scoreId, score).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, retrofit2.Response<ResponseMessage> response) {
                if(response.isSuccessful()){
                    if(response.body() != null) {
                        if(isPlayer1()){
                            userScore1TextView.setText(String.valueOf(finalPlayerScore));
                            playerScore1 = finalPlayerScore;
                        }else{
                            userScore2TextView.setText(String.valueOf(finalPlayerScore));
                            playerScore2 = finalPlayerScore;
                        }
                    }
                }else {
                    RefreshTokenService<ResponseMessage> refreshTokenService =
                            new RefreshTokenService<>(UserBattleInfoFragment.this.requireActivity());
                    refreshTokenService.refreshToken(response);
                    refreshTokenService.setRefreshable(() -> updateScore());
                    refreshTokenService.getRefreshable().refresh();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                UserBattleInfoFragment.this.requireActivity().runOnUiThread(() ->
                        Toast.makeText(UserBattleInfoFragment.this.requireActivity(), "Failed to update score",
                                Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void finishBattle() {
        RetrofitService retrofitService = new RetrofitService();
        CarBrandsBattlesApi carBrandsBattlesApi = retrofitService.getRetrofit().create(CarBrandsBattlesApi.class);
        FinishBattleInput updateBattleInput = new FinishBattleInput();
        updateBattleInput.setFinished(true);
        carBrandsBattlesApi.finishBattle(getAuthHeader(), battleId, updateBattleInput)
                .enqueue(new Callback<ResponseMessage>() {
                    @Override
                    public void onResponse(Call<ResponseMessage> call, retrofit2.Response<ResponseMessage> response) {
                        if(response.isSuccessful()){
                            if(response.body() != null) {
                                goBattleList();
                            }
                        }else {
                            RefreshTokenService<ResponseMessage> refreshTokenService =
                                    new RefreshTokenService<>(UserBattleInfoFragment.this.requireActivity());
                            refreshTokenService.refreshToken(response);
                            refreshTokenService.setRefreshable(() -> finishBattle());
                            refreshTokenService.getRefreshable().refresh();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseMessage> call, Throwable t) {
                        UserBattleInfoFragment.this.requireActivity().runOnUiThread(() ->
                                Toast.makeText(UserBattleInfoFragment.this.requireActivity(), "Failed to finish battle",
                                        Toast.LENGTH_SHORT).show());
                    }
                });
    }

    private void getBattleInfo(){
        RetrofitService retrofitService = new RetrofitService();
        CarBrandsBattlesApi carBrandsBattlesApi = retrofitService.getRetrofit().create(CarBrandsBattlesApi.class);
        carBrandsBattlesApi.getBattlesInfoByBattleId(getAuthHeader(), battleId)
                .enqueue(new Callback<UserInterfaceBattleData>() {
                    @Override
                    public void onResponse(Call<UserInterfaceBattleData> call, retrofit2.Response<UserInterfaceBattleData> response) {
                        swipeRefreshLayout.setRefreshing(false);
                        if(response.isSuccessful()){
                            if(response.body() != null) {
                                setData(response.body());
                            }
                        }else {
                            RefreshTokenService<UserInterfaceBattleData> refreshTokenService =
                                    new RefreshTokenService<>(UserBattleInfoFragment.this.requireActivity());
                            refreshTokenService.refreshToken(response);
                            refreshTokenService.setRefreshable(() -> getBattleInfo());
                            refreshTokenService.getRefreshable().refresh();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserInterfaceBattleData> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        UserBattleInfoFragment.this.requireActivity().runOnUiThread(() ->
                                Toast.makeText(UserBattleInfoFragment.this.requireActivity(),
                                "Failed to get battle info",
                                Toast.LENGTH_SHORT).show());
                    }
                });
    }

    private void setData(UserInterfaceBattleData body) {
        userScore1TextView.setText(String.valueOf(body.getPlayerScore1()));
        userScore2TextView.setText(String.valueOf(body.getPlayerScore2()));
        if(isPlayer1()){
            scoreId = body.getScore1Id();
        }else{
            scoreId = body.getScore2Id();
        }
        playerScore1 = body.getPlayerScore1();
        playerScore2 = body.getPlayerScore2();
        playerUserName1 = body.getPlayer1Username();
        playerUserName2 = body.getPlayer2Username();
        brandName = body.getBrandName();
        punishment = body.getPunishment();
        brandTextView.setText(brandName);
        punishmentTextView.setText(punishment);
        if(isPlayer1()){
            userName2TextView.setText(playerUserName2);
        }
        else {
            userName1TextView.setText(playerUserName1);
            userName2TextView.setText(getResources().getString(R.string.you));
        }
    }

    private boolean isPlayer1(){
        if(player.equals("first")){
            return true;
        }else{
            return false;
        }
    }

    public String getDate(Bundle savedInstanceState, String key){
        String result;
        if (savedInstanceState == null) {
            Bundle extras = requireActivity().getIntent().getExtras();
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

    private void goBattleList() {
        Intent intent = new Intent(UserBattleInfoFragment.this.getActivity(), BattlesListActivity.class);
        startActivity(intent);
    }
}
