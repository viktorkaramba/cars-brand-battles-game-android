package com.example.carbrandsbattles.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.carbrandsbattles.R;
import com.example.carbrandsbattles.api.CarBrandsBattlesApi;
import com.example.carbrandsbattles.api.RetrofitService;
import com.example.carbrandsbattles.entities.UserInterfaceBattleData;
import com.example.carbrandsbattles.services.RefreshTokenService;
import com.example.carbrandsbattles.services.UserInfoService;
import retrofit2.Call;
import retrofit2.Callback;

import static com.example.carbrandsbattles.constants.Constants.getAuthHeader;

public class BattleInfoFragment extends DialogFragment {

    private TextView userScore1TextView, userScore2TextView, userName1TextView, userName2TextView, brandTextView,
            punishmentTextView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int battleId;
    private boolean isHistory = false;
    public static BattleInfoFragment newInstance() {
        BattleInfoFragment fragment = new BattleInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_battle_info,
                container, false);
        UserInfoService.init(BattleInfoFragment.newInstance().getActivity());
        userName1TextView = view.findViewById(R.id.userName1TextView);
        userName2TextView = view.findViewById(R.id.userName2TextView);
        userScore1TextView = view.findViewById(R.id.userScore1TextView);
        punishmentTextView = view.findViewById(R.id.punishmentTextView);
        brandTextView = view.findViewById(R.id.brandTextView);
        swipeRefreshLayout = view.findViewById(R.id.refreshLayoutBattleInfoFragment);
        swipeRefreshLayout.setOnRefreshListener(
                this::getBattleInfo
        );
        userScore2TextView = view.findViewById(R.id.userScore2TextView);
        battleId = Integer.parseInt(getDate(savedInstanceState, "battleId"));
        System.out.println(battleId);
        if(getDate(savedInstanceState,"history").equals("yes")){
            getUserHistoryBattleInfo();
        }else{
            getBattleInfo();
        }
        // Inflate the layout for this fragment
        return view;
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
                                    new RefreshTokenService<>(BattleInfoFragment.this.requireActivity());
                            refreshTokenService.refreshToken(response);
                            refreshTokenService.setRefreshable(() -> getBattleInfo());
                            refreshTokenService.getRefreshable().refresh();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserInterfaceBattleData> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        BattleInfoFragment.this.requireActivity().runOnUiThread(() ->
                                Toast.makeText(BattleInfoFragment.this.requireActivity(),
                                        "Failed to get battle info",
                                        Toast.LENGTH_SHORT).show());
                    }
                });
    }

    private void getUserHistoryBattleInfo(){
        RetrofitService retrofitService = new RetrofitService();
        CarBrandsBattlesApi carBrandsBattlesApi = retrofitService.getRetrofit().create(CarBrandsBattlesApi.class);
        carBrandsBattlesApi.getUserHistoryByBattleId(getAuthHeader(), battleId)
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
                                    new RefreshTokenService<>(BattleInfoFragment.this.requireActivity());
                            refreshTokenService.refreshToken(response);
                            refreshTokenService.setRefreshable(() -> getBattleInfo());
                            refreshTokenService.getRefreshable().refresh();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserInterfaceBattleData> call, Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        BattleInfoFragment.this.requireActivity().runOnUiThread(() ->
                                Toast.makeText(BattleInfoFragment.this.requireActivity(),
                                        "Failed to get battle info",
                                        Toast.LENGTH_SHORT).show());
                    }
                });
    }

    private void setData(UserInterfaceBattleData body) {
        battleId = body.getBattleId();
        userScore1TextView.setText(String.valueOf(body.getPlayerScore1()));
        userScore2TextView.setText(String.valueOf(body.getPlayerScore2()));
        brandTextView.setText(body.getBrandName());
        punishmentTextView.setText(body.getPunishment());
        userName1TextView.setText(body.getPlayer1Username());
        userName2TextView.setText(body.getPlayer2Username());
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

}
