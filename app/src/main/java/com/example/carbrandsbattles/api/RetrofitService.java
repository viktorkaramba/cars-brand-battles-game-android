package com.example.carbrandsbattles.api;

import com.example.carbrandsbattles.constants.Constants;
import com.google.gson.Gson;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit;

    public RetrofitService(){
        initializeRetrofit();
    }

    public void initializeRetrofit(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.HTTP_HOST)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
