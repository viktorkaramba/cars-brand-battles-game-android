package com.example.carbrandsbattles.api;

import com.example.carbrandsbattles.entities.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface CarBrandsBattlesApi {

    @POST("/auth/sign-up")
    Call<LoginResponse> signUp(@Body User user);

    @POST("/auth/sign-in")
    Call<LoginResponse> signIn(@Body SignInInput signInInput);

    @POST("/refresh-token")
    Call<RefreshTokenResponse> refreshToken(@Header("Authorization") String header,
                                       @Body RefreshTokenInput refreshTokenInput);

    @POST("/auth/logout")
    Call<ResponseMessage> logout(@Header("Authorization") String header);

    @GET("/api/users/{username}")
    Call<User> getUserByUsername(@Header("Authorization") String header,
                                                          @Path(value ="username", encoded = true) String username);

    @GET("/api/user-interface-data")
    Call<List<UserInterfaceBattleData>> getBattlesInfo(@Header("Authorization") String header);

    @GET("/api/user-interface-data/{id}")
    Call<UserInterfaceBattleData> getBattlesInfoByBattleId(@Header("Authorization") String header,
                                                           @Path(value ="id", encoded = true) Integer id);

    @GET("/api/users-history")
    Call<List<UserInterfaceBattleData>> getUserHistory(@Header("Authorization") String header);

    @GET("/api/users-history/{id}")
    Call<UserInterfaceBattleData> getUserHistoryByBattleId(@Header("Authorization") String header,
                                                       @Path(value ="id", encoded = true) Integer id);

    @GET("/api/brands/random")
    Call<Brand> getRandomCarBrand(@Header("Authorization") String header);

    @POST("/api/battles/")
    Call<ResponseMessage> createBattle(@Header("Authorization") String header, @Body Battle battle);

    @PUT("/api/battles/{id}")
    Call<ResponseMessage> finishBattle(@Header("Authorization") String header,
                                       @Path(value ="id", encoded = true) Integer id,
                                       @Body FinishBattleInput updateBattleInput);

    @PUT("/api/scores/{id}")
    Call<ResponseMessage> updateScore(@Header("Authorization") String header,
                                      @Path(value ="id", encoded = true) Integer id,
                                      @Body Score score);

    @GET("/api/user-statistics/by-score")
    Call<List<UserStatistics>> getUserStatistics(@Header("Authorization") String header);
}

