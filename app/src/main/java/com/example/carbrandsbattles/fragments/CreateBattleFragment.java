package com.example.carbrandsbattles.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import com.example.carbrandsbattles.R;
import com.example.carbrandsbattles.activities.BattlesListActivity;
import com.example.carbrandsbattles.api.CarBrandsBattlesApi;
import com.example.carbrandsbattles.api.RetrofitService;
import com.example.carbrandsbattles.constants.Constants;
import com.example.carbrandsbattles.entities.Battle;
import com.example.carbrandsbattles.entities.Brand;
import com.example.carbrandsbattles.entities.ResponseMessage;
import com.example.carbrandsbattles.entities.User;
import com.example.carbrandsbattles.services.RefreshTokenService;
import com.example.carbrandsbattles.services.UserInfoService;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;

import java.util.Objects;

public class CreateBattleFragment extends DialogFragment {

    private EditText userNameEditText, punishmentEditText;
    private ImageView carBrandImageView;
    private Brand brand;
    private User opponent;
    public static CreateBattleFragment newInstance() {
        CreateBattleFragment fragment = new CreateBattleFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_battle,
                container, false);
        Objects.requireNonNull(getDialog()).getWindow()
                .setBackgroundDrawableResource(R.drawable.card_view_rounded);
        UserInfoService.init(CreateBattleFragment.newInstance().getActivity());
        userNameEditText = view.findViewById(R.id.userNameEditText);
        punishmentEditText = view.findViewById(R.id.punishmentEditTextTextMultiLine);
        Button randomCarBrandButton = view.findViewById(R.id.randomCarBrandButton);
        carBrandImageView = view.findViewById(R.id.randomCarBrandImageView);
        Button createBattleButton = view.findViewById(R.id.createBattleButton);
        Button backButton = view.findViewById(R.id.backButton);
        randomCarBrandButton.setOnClickListener(view12 -> getRandomCarBrand());
        createBattleButton.setOnClickListener(view1 -> checkOpponentUsername());
        backButton.setOnClickListener(view13 -> CreateBattleFragment.this.dismiss());
        return view;
    }

    private void checkOpponentUsername() {
        if(userNameEditText.getText().length() == 0){
            userNameEditText.setError("Please input username");
        }
        else if(punishmentEditText.getText().length() == 0){
            punishmentEditText.setError("Please input punishment");
        } else if (carBrandImageView.getDrawable() == null) {
            CreateBattleFragment.this.getActivity().runOnUiThread(() ->
                    Toast.makeText(CreateBattleFragment.this.getContext(),
                            "Please switch car brand",
                            Toast.LENGTH_SHORT).show());
        } else if(userNameEditText.getText().toString().equals(UserInfoService.getProperty("username"))){
            userNameEditText.setError("You cannot pick your username");
        } else{
            RetrofitService retrofitService = new RetrofitService();
            CarBrandsBattlesApi carBrandsBattlesApi = retrofitService.getRetrofit().create(CarBrandsBattlesApi.class);
            carBrandsBattlesApi.getUserByUsername(Constants.getAuthHeader(), userNameEditText.getText().toString())
                    .enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                            if(response.isSuccessful()){
                                if(response.body() != null && response.body().getId()!=0) {
                                    opponent = response.body();
                                    createBattle();
                                }
                                else{
                                    CreateBattleFragment.this.getActivity().runOnUiThread(() ->
                                            Toast.makeText(CreateBattleFragment.this.getContext(),
                                                    "Not found opponent with this username",
                                                    Toast.LENGTH_SHORT).show());
                                }
                            }else{
                                RefreshTokenService<User> refreshTokenService =
                                        new RefreshTokenService<>(CreateBattleFragment.this.getActivity());
                                refreshTokenService.refreshToken(response);
                                refreshTokenService.setRefreshable(() -> checkOpponentUsername());
                                refreshTokenService.getRefreshable().refresh();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            CreateBattleFragment.this.getActivity().runOnUiThread(() ->
                                    Toast.makeText(CreateBattleFragment.this.getContext(),
                                            "Failed to check username",
                                            Toast.LENGTH_SHORT).show());
                            Log.d("response fail", call.toString());
                        }
                    });
        }
    }

    private void getRandomCarBrand() {
        RetrofitService retrofitService = new RetrofitService();
        CarBrandsBattlesApi carBrandsBattlesApi = retrofitService.getRetrofit().create(CarBrandsBattlesApi.class);
        String header = Constants.getAuthHeader();
        carBrandsBattlesApi.getRandomCarBrand(header).enqueue(new Callback<Brand>() {
            @Override
            public void onResponse(Call<Brand> call, retrofit2.Response<Brand> response) {
                if(response.isSuccessful()){
                    brand = response.body();
                    Picasso.get().load(brand.getImageBrand()).resize(400, 400).into(carBrandImageView);
                }else{
                    RefreshTokenService<Brand> refreshTokenService =
                            new RefreshTokenService<>(CreateBattleFragment.this.getActivity());
                    refreshTokenService.refreshToken(response);
                    refreshTokenService.setRefreshable(() -> getRandomCarBrand());
                    refreshTokenService.getRefreshable().refresh();
                }
            }

            @Override
            public void onFailure(Call<Brand> call, Throwable t) {
                CreateBattleFragment.this.getActivity().runOnUiThread(() ->
                        Toast.makeText(CreateBattleFragment.this.getContext(),
                                "Failed to get random car brand",
                                Toast.LENGTH_SHORT).show());
                Log.d("response fail", call.toString());
            }
        });

    }

    private void createBattle() {
        RetrofitService retrofitService = new RetrofitService();
        CarBrandsBattlesApi carBrandsBattlesApi = retrofitService.getRetrofit().create(CarBrandsBattlesApi.class);
        String punishment = punishmentEditText.getText().toString();
        Battle battle = new Battle(0, Integer.parseInt(UserInfoService.getProperty("userId")),
                opponent.getId(), punishment,false, brand.getId());
        carBrandsBattlesApi.createBattle(Constants.getAuthHeader(), battle).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, retrofit2.Response<ResponseMessage> response) {
                if(response.isSuccessful()){
                    CreateBattleFragment.this.dismiss();
                    BattlesListActivity activity = (BattlesListActivity) getActivity();
                    assert activity != null;
                    activity.getBattlesInfo();
                }else{
                    RefreshTokenService<ResponseMessage> refreshTokenService =
                            new RefreshTokenService<>(CreateBattleFragment.this.getActivity());
                    refreshTokenService.refreshToken(response);
                    refreshTokenService.setRefreshable(() -> createBattle());
                    refreshTokenService.getRefreshable().refresh();
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                CreateBattleFragment.this.getActivity().runOnUiThread(() ->
                        Toast.makeText(CreateBattleFragment.this.getContext(),
                                "Failed to create battle",
                                Toast.LENGTH_SHORT).show());
            }
        });
    }
}