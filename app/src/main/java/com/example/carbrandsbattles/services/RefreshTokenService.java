package com.example.carbrandsbattles.services;

import android.content.Context;
import android.util.Log;
import com.example.carbrandsbattles.api.CarBrandsBattlesApi;
import com.example.carbrandsbattles.api.RetrofitService;
import com.example.carbrandsbattles.constants.Constants;
import com.example.carbrandsbattles.entities.ErrorResponse;
import com.example.carbrandsbattles.entities.RefreshTokenInput;
import com.example.carbrandsbattles.entities.RefreshTokenResponse;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

import static com.example.carbrandsbattles.constants.Constants.getAuthHeader;

public class RefreshTokenService<T>{

    private Refreshable refreshable;
    public RefreshTokenService(Context context){
        UserInfoService.init(context);
    }

    public Refreshable getRefreshable() {
        return refreshable;
    }
    public void setRefreshable(Refreshable refreshable) {
        this.refreshable = refreshable;
    }

    public void refreshToken(Response<T> response) {
        try {
            assert response.errorBody() != null;
            String jsonString = response.errorBody().string();
            JSONObject obj = new JSONObject(jsonString);
            ErrorResponse errorResponse = new ErrorResponse(obj.getString("Message"));
            if(errorResponse.getMessage().contains(Constants.TOKEN_EXPIRED)){
                refreshTokenRequest();
            }
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void refreshTokenRequest(){
        RetrofitService retrofitService = new RetrofitService();
        CarBrandsBattlesApi carBrandsBattlesApi = retrofitService.getRetrofit().create(CarBrandsBattlesApi.class);
        carBrandsBattlesApi.refreshToken(getAuthHeader(),
                new RefreshTokenInput(Integer.parseInt(UserInfoService.getProperty("userId"))))
                        .enqueue(new Callback<RefreshTokenResponse>() {
            @Override
            public void onResponse(Call<RefreshTokenResponse> call, Response<RefreshTokenResponse> response) {
                if(response.isSuccessful()){
                    assert response.body() != null;
                    UserInfoService.addProperty("access_token", response.body().getAccessToken());
                }
            }

            @Override
            public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                Log.d("ERROR REFRESH TOKEN", t.getMessage());
            }
        });
    }
}
